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

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class HomeController implements Initializable {
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
    private boolean ascending = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Filme laden mit Cache‑Fallback
        try {
            allMovies = loadMoviesWithCache();
        } catch (MovieApiException | DatabaseException e) {
            showAlert(Alert.AlertType.ERROR,
                    "Filme konnten nicht geladen werden",
                    "Bitte überprüfe deine Internetverbindung und versuche es später erneut.\n" +
                            "Die App kann weiterhin genutzt werden, aber einige Film‑Daten fehlen möglicherweise.");
            allMovies = Collections.emptyList();  // Fallback
        }

        observableMovies.setAll(allMovies);
        sortMovies(ascending);

        ClickEventHandler<Movie> addToWatchlistHandler = movie -> {
            try {
                WatchlistRepository watchlistRepo = new WatchlistRepository();
                if (watchlistRepo.add(movie)) {
                    return true;
                }
                else{ //Already in list
                    watchlistRepo.removeFromWatchlist(movie.getId());
                    return false;
                }

            } catch (DatabaseException ex) {
                showAlert(Alert.AlertType.ERROR,
                        "Speicherfehler",
                        "Beim Speichern in der Watchlist ist ein Fehler aufgetreten.\n" +
                                "Bitte versuche es später erneut.");
            }
            return false;
        };

        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(view -> new MovieCell(addToWatchlistHandler));

        genreComboBox.getItems().addAll(getAllGenres());
        releaseYearComboBox.getItems().addAll(getAllReleaseYears());
        for (int i = 0; i <= 10; i++) {
            ratingComboBox.getItems().add(String.valueOf(i));
        }

        searchField.setOnAction(e -> triggerSearch());
        searchBtn.setOnAction(e -> triggerSearch());
        sortBtn.setOnAction(e -> {
            ascending = !ascending;
            sortMovies(ascending);
            sortBtn.setText(ascending ? "Sort (asc)" : "Sort (desc)");
        });
        clearBtn.setOnAction(e -> {
            searchField.clear();
            genreComboBox.getSelectionModel().clearSelection();
            releaseYearComboBox.getSelectionModel().clearSelection();
            ratingComboBox.getSelectionModel().clearSelection();
            refreshMovies();
            sortMovies(ascending);
            clearBtn.setVisible(false);
        });
    }

    private List<Movie> loadMoviesWithCache() {
        try {
            List<Movie> fresh = MovieAPI.getMovies(null, null, null, null);
            new MovieRepository().addAllMovies(fresh);
            return fresh;
        } catch (MovieApiException | DatabaseException e) {
            List<Movie> cached = MovieEntity.toMovies(
                    new MovieRepository().getAllMovies());
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
            sortMovies(ascending);
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

    public void sortMovies(boolean ascending) {
        if (observableMovies.isEmpty()) return;
        if (ascending) FXCollections.sort(observableMovies);
        else FXCollections.sort(observableMovies, Comparator.reverseOrder());
    }

    public void refreshMovies() {
        observableMovies.setAll(allMovies);
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
