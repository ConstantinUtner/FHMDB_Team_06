package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.MovieAPI;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.Comparator;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXComboBox releaseYearComboBox;

    @FXML
    public JFXComboBox ratingComboBox;

    @FXML
    public JFXButton sortBtn;

    @FXML
    public JFXButton clearBtn;

    @FXML
    private Label popularActorLabel;

    public List<Movie> allMovies = MovieAPI.getMovies(null, null, null, null);

    private ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes
    private boolean ascending = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // initialize UI stuff
        observableMovies.setAll(allMovies);
        sortMovies(ascending);

        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        // Fill ComboBoxes
        genreComboBox.getItems().addAll(Genre.values());
        releaseYearComboBox.getItems().addAll(getAllReleaseYears());

        for (int i = 0; i <= 10; i++) {
            ratingComboBox.getItems().add(String.valueOf(i));
        }

        searchField.setOnAction(event -> triggerSearch()); // triggerSearch() will be called if enter is pressed

        searchBtn.setOnAction(actionEvent -> triggerSearch());

        sortBtn.setOnAction(actionEvent -> {
            ascending = !ascending;
            sortMovies(ascending);
            if (ascending) {
                sortBtn.setText("Sort (asc)");
            }
            else {
                sortBtn.setText("Sort (desc)");
            }
        });

        clearBtn.setOnAction(actionEvent -> {
            searchField.clear();
            genreComboBox.getSelectionModel().clearSelection();
            releaseYearComboBox.getSelectionModel().clearSelection();
            ratingComboBox.getSelectionModel().clearSelection();
            refreshMovies();
            sortMovies(ascending);
            clearBtn.setVisible(false);
        });
    }

    private void triggerSearch() {
            String searchText = searchField.getText();

            int idx = genreComboBox.getSelectionModel().getSelectedIndex();

            Genre genre = idx >= 0 ? Genre.values()[idx] : null;

            String releaseYear = releaseYearComboBox.getSelectionModel().getSelectedItem() != null
                    ? releaseYearComboBox.getSelectionModel().getSelectedItem().toString()
                    : "";

            String ratingFrom = ratingComboBox.getSelectionModel().getSelectedItem() != null
                    ? ratingComboBox.getSelectionModel().getSelectedItem().toString()
                    : "";


                List<Movie> moviesFromAPI = MovieAPI.getMovies(searchText, genre, releaseYear, ratingFrom);
                observableMovies.setAll(moviesFromAPI);
                sortMovies(ascending);
                clearBtn.setVisible(observableMovies.size() != allMovies.size());
    }

    // Help method for filter method
    private static boolean isMatchingSearchText(String searchText, String origin) {
        return searchText == null || searchText.isEmpty() || origin.toLowerCase().contains(searchText.toLowerCase());
    }

    public void filterMovies(String searchText, Genre genre) {
        List<Movie> filteredMovies = new ArrayList<>();

        for (Movie movie : allMovies) {
            boolean genreMatchesGenre = genre == null || movie.getGenres().contains(genre);

            if (genreMatchesGenre) {
                boolean titleMatchesSearchText = isMatchingSearchText(searchText, movie.getTitle()) ;
                boolean descriptionMatchesSearchText = isMatchingSearchText(searchText, movie.getDescription());

                if (titleMatchesSearchText || descriptionMatchesSearchText) {
                    filteredMovies.add(movie);
                }
            }
        }
        observableMovies.setAll(filteredMovies);
    }

    public void sortMovies(boolean ascending) {
        if (observableMovies.isEmpty())
            return;

        if (ascending) {
            FXCollections.sort(observableMovies);
        } else {
            FXCollections.sort(observableMovies, Comparator.reverseOrder());
        }
    }

    public void refreshMovies() {
        observableMovies.setAll(allMovies);
    }

    public List<Movie> getShownMovies(){
        return new ArrayList<>(observableMovies);
    }

    private List<String> getAllReleaseYears() {
        return allMovies.stream()
                // Takes the releaseYear of each movie, turns it into a String and creates a new Stream with these Strings
                .map(movie -> String.valueOf(movie.getReleaseYear()))
                // Removes duplicate years
                .distinct()
                // Sorts the years in ascending order (as Strings)
                .sorted()
                // Collects the result into a list
                .toList();
    }

    public int getLongestMovieTitle(List<Movie> movies) {
        return movies.stream()
                .filter(m -> m.getTitle() != null)
                .mapToInt(m -> m.getTitle().length())
                .max()
                .orElse(0);
    }

    @FXML
    private void onPopularActorClicked() {
        String popularActor = getMostPopularActor(allMovies);
        if (popularActor != null) {
            popularActorLabel.setText("Most popular actor: " + popularActor);
        } else {
            popularActorLabel.setText("No actors found");
        }
    }

    public String getMostPopularActor(List<Movie> movies) {
        return movies.stream()
                .filter(m -> m.getMainCast() != null)
                .flatMap(m -> m.getMainCast().stream())
                .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()))
                .entrySet().stream()
                .max((e1, e2) -> Long.compare(e1.getValue(), e2.getValue()))
                .map(entry -> entry.getKey())
                .orElse(null);
    }
}