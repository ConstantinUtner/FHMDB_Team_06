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

    public List<Movie> allMovies = Movie.initializeMovies();

    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableMovies.addAll(allMovies);         // add dummy data to observable list

        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        // TODO add genre filter items with genreComboBox.getItems().addAll(...)
        genreComboBox.getItems().addAll(Genre.values());
        genreComboBox.setPromptText("Filter by Genre");
        searchBtn.setOnAction(actionEvent -> {
            String searchText = searchField.getText();

            int idx = genreComboBox.getSelectionModel().getSelectedIndex();
            Genre genre = null;
            if (idx >= 0) {
                genre = Genre.values()[idx];
            }
            observableMovies.setAll(filter(searchText, genre));
        });

        // TODO add event handlers to buttons and call the regarding methods
        // either set event handlers in the fxml file (onAction) or add them here

        // Sort button example:
        sortBtn.setOnAction(actionEvent -> {
            if(sortBtn.getText().equals("Sort (asc)")) {
                // TODO sort observableMovies ascending
                sortBtn.setText("Sort (desc)");
            } else {
                // TODO sort observableMovies descending
                sortBtn.setText("Sort (asc)");
            }
        });
    }


    public List<Movie> filter(String searchText, Genre genre) {

        if ((searchText == null || searchText.isEmpty()) && genre == null) {
            return allMovies;
        }

        List<Movie> filteredMovies = new ArrayList<>();

        for (Movie movie : allMovies) {
            boolean titleMatchesSearchText = searchText == null || searchText.isEmpty() || movie.getTitle().toLowerCase().contains(searchText.toLowerCase());
            boolean descriptionMatchesSearchText = searchText == null || searchText.isEmpty() || movie.getDescription().toLowerCase().contains(searchText.toLowerCase());
            boolean genreMatchesGenre = genre == null || movie.getGenres().contains(genre);

            if ((titleMatchesSearchText || descriptionMatchesSearchText) && genreMatchesGenre) {
                filteredMovies.add(movie);
            }
        }

        return filteredMovies;
    }
}