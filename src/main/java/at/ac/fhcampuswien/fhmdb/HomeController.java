package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import java.util.Comparator;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
    public JFXButton sortBtn;

    @FXML
    public JFXButton shuffleBtn;

    @FXML
    public JFXButton clearBtn;

    public List<Movie> allMovies = Movie.initializeMovies();

    private ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes
    private boolean ascending = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableMovies.addAll(allMovies);         // add dummy data to observable list
        sortMovies(ascending);

        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        genreComboBox.getItems().addAll(Genre.values());
        genreComboBox.setPromptText("Filter by Genre");

        searchField.setOnAction(event -> triggerSearch());

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
            refreshMovies();
            sortMovies(ascending);
            clearBtn.setVisible(false);
        });

        shuffleBtn.setOnAction(actionEvent -> shuffleMovies());
    }

    private void triggerSearch() {
        String searchText = searchField.getText();
        int idx = genreComboBox.getSelectionModel().getSelectedIndex();
        Genre genre = null;
        if (idx >= 0) {
            genre = Genre.values()[idx];
        }
        filterMovies(searchText, genre);
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

    public void shuffleMovies() {
        FXCollections.shuffle(observableMovies);
    }

    public List<Movie> getShownMovies(){
        return new ArrayList<>(observableMovies);
    }
}