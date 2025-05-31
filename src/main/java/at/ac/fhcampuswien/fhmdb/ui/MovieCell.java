package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.StringJoiner;


public class MovieCell extends ListCell<Movie> {

    private final boolean isAddMode;


    // Text Box
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genre = new Label();
    private final Label rating_year = new Label();
    private final VBox textLayout = new VBox(title, detail, genre, rating_year);

    // Button Box
    private final Button showDetailsButton = new Button("Show Details");
    private final Button watchlistButton = new Button("To Watchlist");
    private final HBox buttonLayout = new HBox(showDetailsButton, watchlistButton);

    // Main Box für Text- & Button Box
    private final HBox mainLayout = new HBox();

    // MovieCell Konstruktor
    public MovieCell(ClickEventHandler<Movie> onWatchlistClicked, boolean isAddMode) {
        this.isAddMode = isAddMode;

        // Text Box Layout
        textLayout.setSpacing(10);
        textLayout.setPadding(new Insets(10));
        textLayout.setAlignment(Pos.CENTER_LEFT);

        // Button Box Layout
        buttonLayout.setSpacing(10);
        buttonLayout.setAlignment(Pos.CENTER_RIGHT);
        buttonLayout.setPadding(new Insets(10));

        // Main Layout
        HBox.setHgrow(textLayout, Priority.ALWAYS);
        mainLayout.getChildren().addAll(textLayout, buttonLayout);
        mainLayout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));
        mainLayout.setAlignment(Pos.CENTER_LEFT);
        mainLayout.setSpacing(20);

        // Button-Styles
        showDetailsButton.getStyleClass().add("button-cell");
        watchlistButton.getStyleClass().add("button-cell");

        // Handler-Zuweisung per Lambda
        watchlistButton.setOnAction(event -> {
            if (getItem() != null && onWatchlistClicked != null) {
                onWatchlistClicked.onClick(getItem());
                if (isAddMode) {
                    watchlistButton.setText("Added to Watchlist");
                    watchlistButton.setOpacity(0.5);
                }
            }
        });
    }

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Titel
            title.setText(movie.getTitle());

            // Beschreibung
            detail.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );
            detail.setWrapText(true);
            detail.setMaxWidth(500);

            // Genres
            if (movie.getGenres().isEmpty()) {
                genre.setText("");
            } else {
                StringJoiner joiner = new StringJoiner(", ");
                for (Genre genre : movie.getGenres()) {
                    joiner.add(genre.toString());
                }
                genre.setText(joiner.toString());
            }

            // Bewertung und Jahr
            String ratingText = movie.getRating() != null
                    ? String.format("Rating: %.1f", movie.getRating().doubleValue())
                    : "Rating: N/A";

            String yearText = "Year: " + movie.getReleaseYear();
            rating_year.setText(ratingText + " | " + yearText);

            // Styles
            title.getStyleClass().add("title-cell");
            detail.getStyleClass().add("text-white");
            genre.getStyleClass().add("genre-text");
            rating_year.getStyleClass().add("text-white");

            //button text
            if (isAddMode) {
                boolean isIn = WatchlistRepository.getInstance().isInWatchlist(movie);
                watchlistButton.setText(isIn ? "Added to Watchlist" : "To Watchlist");
                watchlistButton.setOpacity(isIn ? 0.5 : 1.0);
                watchlistButton.setDisable(false);
            } else {
                watchlistButton.setText("Remove");
                watchlistButton.setOpacity(1.0);
                watchlistButton.setDisable(false);
            }
            // Layout als Cell-Grafik anzeigen
            setGraphic(mainLayout);
        }
    }
}
