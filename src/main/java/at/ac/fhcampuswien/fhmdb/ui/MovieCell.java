package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.StringJoiner;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genre = new Label();
    private final Label rating_year = new Label();
    private final VBox layout = new VBox(title, detail, genre, rating_year);

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null);
            setGraphic(null);
        } else {
            this.getStyleClass().add("movie-cell");
            title.setText(movie.getTitle());
            detail.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );

            // adds Genres of a Movie to the MovieCell
            if (movie.getGenres().isEmpty()) {
                genre.setText("");
            } else {
                StringJoiner joiner = new StringJoiner(", ");
                for (Genre genre : movie.getGenres()) {
                    joiner.add(genre.toString());
                }
                genre.setText(joiner.toString());
            }

            String ratingText = movie.getRating() != null
                    ? String.format("Rating: %.1f", movie.getRating().doubleValue())
                    : "Rating: N/A";
            String yearText = "Year: " + movie.getReleaseYear();
            rating_year.setText(ratingText + " | " + yearText);
            rating_year.getStyleClass().add("text-white");

            // genre text style
            genre.getStyleClass().add("genre-text");

            // color scheme
            title.getStyleClass().add("text-yellow");
            detail.getStyleClass().add("text-white");
            layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

            // layout
            title.fontProperty().set(title.getFont().font(20));
            detail.setMaxWidth(this.getScene().getWidth() - 30);
            detail.setWrapText(true);
            layout.setPadding(new Insets(10));
            layout.spacingProperty().set(10);
            layout.alignmentProperty().set(javafx.geometry.Pos.CENTER_LEFT);
            setGraphic(layout);
        }
    }
}

