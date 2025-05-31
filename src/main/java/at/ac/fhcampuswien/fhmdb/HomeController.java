package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
import at.ac.fhcampuswien.fhmdb.database.MovieRepository;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.MovieAPI;
import at.ac.fhcampuswien.fhmdb.ui.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import at.ac.fhcampuswien.fhmdb.observer.Observer;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import at.ac.fhcampuswien.fhmdb.state.*;

public class HomeController implements Initializable, Observer {
    @FXML public JFXButton searchBtn;
    @FXML public TextField searchField;
    @FXML public JFXListView<Movie> movieListView;
    @FXML public JFXComboBox<Genre> genreComboBox;
    @FXML public JFXComboBox<String> releaseYearComboBox;
    @FXML public JFXComboBox<String> ratingComboBox;
    @FXML public JFXButton sortBtn;
    @FXML public JFXButton clearBtn;

    public List<Movie> allMovies = new ArrayList<>();

    private ObservableList<Movie> observableMovies = FXCollections.observableArrayList();


    private SortContext sortContext = new SortContext(); // State Pattern Context
    private boolean isAscending = true;  // Steuert UI-Text und State-Umschaltung

    private WatchlistRepository watchlistRepo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            watchlistRepo = WatchlistRepository.getInstance();
            watchlistRepo.addObserver(this);
        } catch (DatabaseException e) {
            showAlert(Alert.AlertType.ERROR, "Fehler", "Watchlist konnte nicht initialisiert werden.");
        }

        // Filme laden mit Cache‑Fallback
        try {
            allMovies = loadMoviesWithCache();
        } catch (MovieApiException | DatabaseException e) {
            showAlert(Alert.AlertType.ERROR,
                    "Filme konnten nicht geladen werden",
                    "Bitte überprüfe deine Internetverbindung und versuche es später erneut.\n" +
                            "Die App kann weiterhin genutzt werden, aber einige Film‑Daten fehlen möglicherweise.");
            allMovies = Collections.emptyList();  // Fallback
            System.err.println("Technische Fehlermeldung beim Hinzufügen zur Watchlist: " + e.getMessage());
            e.printStackTrace();
        }

        observableMovies.setAll(allMovies);
        sortContext.applySort(observableMovies);

        ClickEventHandler<Movie> addToWatchlistHandler = movie -> {
            try {
                watchlistRepo.add(movie);
                return true;
            } catch (DatabaseException ex) {
                showAlert(Alert.AlertType.ERROR,
                        "Speicherfehler",
                        "Beim Speichern in der Watchlist ist ein Fehler aufgetreten.\n" +
                                "Bitte versuche es später erneut.");
                System.err.println("Technische Fehlermeldung beim Hinzufügen zur Watchlist: " + ex.getMessage());
                return false;
            }
        };

        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(view -> new MovieCell(addToWatchlistHandler, true));

        genreComboBox.getItems().addAll(getAllGenres());
        releaseYearComboBox.getItems().addAll(getAllReleaseYears());
        for (int i = 0; i <= 10; i++) {
            ratingComboBox.getItems().add(String.valueOf(i));
        }

        searchField.setOnAction(e -> triggerSearch());
        searchBtn.setOnAction(e -> triggerSearch());

        // Button-Handler mit StatePattern angepasst
        sortBtn.setOnAction(e -> {
            if (isAscending) {
                sortContext.setState(new AscSortState());
                sortBtn.setText("Sort (asc)");
            } else {
                sortContext.setState(new DescSortState());
                sortBtn.setText("Sort (desc)");
            }
            isAscending = !isAscending;
            sortContext.applySort(observableMovies);
        });

        clearBtn.setOnAction(e -> {
            searchField.clear();
            genreComboBox.getSelectionModel().clearSelection();
            releaseYearComboBox.getSelectionModel().clearSelection();
            ratingComboBox.getSelectionModel().clearSelection();
            refreshMovies();
            sortContext.applySort(observableMovies);
            clearBtn.setVisible(false);
        });
    }

    @Override
    public void update(String message) {
        showAlert(Alert.AlertType.INFORMATION, "Watchlist", message);
    }

    private List<Movie> loadMoviesWithCache() {
        try {
            List<Movie> fresh = MovieAPI.getMovies(null, null, null, null);
            MovieRepository.getInstance().addAllMovies(fresh);
            return fresh;
        } catch (MovieApiException | DatabaseException e) {
            List<Movie> cached = MovieEntity.toMovies(
                    MovieRepository.getInstance().getAllMovies());
            if (cached.isEmpty()) {
                throw e;
            }
            return cached;
        }
    }

    private void triggerSearch() {
        String searchText = searchField.getText();
        int idx = genreComboBox.getSelectionModel().getSelectedIndex();
        Genre genre = idx >= 0 ? Genre.values()[idx] : null;
        String releaseYear = releaseYearComboBox.getSelectionModel().getSelectedItem() != null
                ? releaseYearComboBox.getSelectionModel().getSelectedItem() : "";
        String ratingFrom = ratingComboBox.getSelectionModel().getSelectedItem() != null
                ? ratingComboBox.getSelectionModel().getSelectedItem() : "";

        try {
            List<Movie> fromApi = MovieAPI.getMovies(searchText, genre, releaseYear, ratingFrom);
            observableMovies.setAll(fromApi);
            sortContext.applySort(observableMovies);
        } catch (MovieApiException e) {
            showAlert(Alert.AlertType.WARNING,
                    "Suche fehlgeschlagen",
                    "Während der Suche gab es ein Problem.\n" +
                            "Deine bisher angezeigte Filmliste bleibt erhalten.");
        }
        clearBtn.setVisible(!observableMovies.equals(allMovies));
    }

    public void filterMovies(String searchText, Genre genre) {
        List<Movie> filtered = new ArrayList<>();
        for (Movie movie : allMovies) {
            boolean matchesGenre = genre == null || movie.getGenres().contains(genre);
            if (matchesGenre) {
                boolean inTitle = isMatching(searchText, movie.getTitle());
                boolean inDesc  = isMatching(searchText, movie.getDescription());
                if (inTitle || inDesc) filtered.add(movie);
            }
        }
        observableMovies.setAll(filtered);
    }

    public void refreshMovies() {
        observableMovies.setAll(allMovies);
        sortContext.applySort(observableMovies);
    }

    public List<Movie> getShownMovies() {
        return new ArrayList<>(observableMovies);
    }

    private List<String> getAllReleaseYears() {
        return allMovies.stream()
                .map(m -> String.valueOf(m.getReleaseYear()))
                .distinct()
                .sorted()
                .toList();
    }

    private List<Genre> getAllGenres() {
        return allMovies.stream()
                .flatMap(m -> m.getGenres().stream())
                .distinct()
                .sorted(Comparator.comparing(Enum::name))
                .toList();
    }

    private static boolean isMatching(String searchText, String origin) {
        return searchText == null || searchText.isEmpty()
                || origin.toLowerCase().contains(searchText.toLowerCase());
    }

    private void showAlert(Alert.AlertType type, String title, String text) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(text);
        a.showAndWait();
    }

    // Java Streams

    public String getMostPopularActor(List<Movie> movies) {
        if (movies != null) {
            return movies.stream()
                    .flatMap(movie -> movie.getMainCast().stream())
                    .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()))
                    .entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("");
        }
        return null;
    }

    public int getLongestMovieTitle(List<Movie> movies) {
        if (movies != null) {
            return movies.stream()
                    .map(Movie::getTitle)
                    .mapToInt(String::length)
                    .max()
                    .orElse(0);
        }
        return -1;
    }

    public long countMoviesFrom(List<Movie> movies, String director) {
        if (movies != null) {
            return movies.stream()
                    .filter(movie -> movie.getDirectors() != null && movie.getDirectors().contains(director))
                    .count();
        }
        return -1;
    }

    public List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        if (movies != null) {
            return movies.stream()
                    .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                    .collect(Collectors.toList());
        }
        return null;
    }
}
