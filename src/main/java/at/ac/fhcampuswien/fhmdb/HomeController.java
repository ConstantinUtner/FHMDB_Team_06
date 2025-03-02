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
import java.util.Collections;
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
    private boolean isAscending;

    @FXML
    public JFXButton clearBtn;

    public List<Movie> allMovies = Movie.initializeMovies();

    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableMovies.addAll(sort(allMovies,true)); // add dummy data to observable list
        isAscending = true;

        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        genreComboBox.getItems().addAll(Genre.values());
        genreComboBox.setPromptText("Filter by Genre");
        searchBtn.setOnAction(actionEvent -> {
            clearBtn.setVisible(true);
            String searchText = searchField.getText();

            int idx = genreComboBox.getSelectionModel().getSelectedIndex();
            Genre genre = null;
            if (idx >= 0) {
                genre = Genre.values()[idx];
            }
            else if(searchText.isEmpty()){
                clearBtn.setVisible(false);
            }
            observableMovies.setAll(sort(filter(searchText, genre), isAscending));
        });

        // Clear Button
        clearBtn.setOnAction(actionEvent -> {
            observableMovies.setAll(sort(allMovies, isAscending));
            genreComboBox.getSelectionModel().clearSelection();
            searchField.clear();
            clearBtn.setVisible(false);
        });

        // Sort button:
        sortBtn.setOnAction(actionEvent -> {
            if(!isAscending) {
                isAscending = true;
                sortBtn.setText("Sort (desc)");
            } else {
                isAscending = false;
                sortBtn.setText("Sort (asc)");
            }
            observableMovies.setAll(sort(observableMovies, isAscending));
        });
    }

    // Sort observableMovies:
    public List<Movie> sort(List<Movie> movieList, boolean isAscending) {
        List<Movie> sortedList = new ArrayList<>(movieList);
        Collections.sort(sortedList);
        if(!isAscending) {
            Collections.reverse(sortedList);
        }
        return sortedList;
    }

    // Help method for filter method
    private static boolean isMatchingSearchText(String searchText, String origin) {
        return searchText == null || searchText.isEmpty() || origin.toLowerCase().contains(searchText.toLowerCase());
    }

    public List<Movie> filter(String searchText, Genre genre) {

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

        return filteredMovies;
    }
}