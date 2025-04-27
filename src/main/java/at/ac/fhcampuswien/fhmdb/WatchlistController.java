package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.MovieEntity;
import at.ac.fhcampuswien.fhmdb.database.MovieRepository;
import at.ac.fhcampuswien.fhmdb.database.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class WatchlistController implements Initializable {

    @FXML
    public JFXListView<Movie> watchlistListView;
    @FXML
    public JFXButton HomeBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Daten aus Repositories holen
            WatchlistRepository watchlistRepo = new WatchlistRepository();
            MovieRepository movieRepo = new MovieRepository();

            // Liste mit gespeicherten API-IDs
            List<String> watchlistIds = java.util.Optional.ofNullable(watchlistRepo.getAll())
                    .orElse(List.of())
                    .stream()
                    .map(w -> w.apiId)
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.toList());

            // Alle Filme aus der Datenbank holen
            List<Movie> allMovies = java.util.Optional.ofNullable(MovieEntity.toMovies(movieRepo.getAllMovies()))
                    .filter(java.util.Objects::nonNull)
                    .orElse(List.of());

            // Nur Filme anzeigen, die in der Watchlist sind
            List<Movie> filtered = allMovies.stream()
                    .filter(m -> watchlistIds.contains(m.getId()))
                    .collect(Collectors.toList());

            // Click-Handler zum Entfernen definieren
            ClickEventHandler<Movie> removeHandler = movie -> {
                try {
                    watchlistRepo.removeFromWatchlist(movie.getId());
                    initialize(null, null); // Liste neu laden nach Entfernen
                } catch (DatabaseException e) {
                    showAlert(Alert.AlertType.ERROR,
                            "Entfernen fehlgeschlagen",
                            "Beim Entfernen von „" + movie.getTitle() +
                                    "“ ist ein Fehler aufgetreten.\nBitte versuche es später erneut.");
                }
            };

            // UI mit Daten befüllen
            watchlistListView.setItems(FXCollections.observableArrayList(filtered));
            watchlistListView.setCellFactory(view -> new MovieCell(removeHandler, true));

            // Hinweis falls die Watchlist leer ist
            if (filtered.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION,
                        "Deine Watchlist ist leer",
                        "Füge Filme zur Watchlist hinzu, indem du auf 'Watchlist' beim Film klickst!");
            }
        } catch (DatabaseException e) {
            showAlert(Alert.AlertType.ERROR,
                    "Watchlist konnte nicht geladen werden",
                    "Es gab ein Problem beim Zugriff auf deine lokale Watchlist‑Datenbank.\n" +
                            "Bitte prüfe, ob die App Schreib‑ und Leserechte für den Speicherordner besitzt " +
                            "und genügend freier Speicherplatz vorhanden ist. " +
                            "Starte die App neu, wenn das Problem weiterhin besteht.");
        }

        // Rücknavigation zum HomeScreen
        HomeBtn.setOnAction(e -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/at/ac/fhcampuswien/fhmdb/home-view.fxml"));
                Scene homeScene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) HomeBtn.getScene().getWindow();
                stage.setScene(homeScene);
                stage.show();
            } catch (IOException ex) {
                showAlert(Alert.AlertType.ERROR,
                        "Fehler beim Wechseln",
                        "Der HomeScreen konnte nicht geladen werden.\nBitte versuche es später erneut.");
            }
        });
    }

    // Utility‑Methode für JFXAlerts
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
