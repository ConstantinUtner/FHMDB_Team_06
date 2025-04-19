package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.database.DatabaseManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class FhmdbApplication extends Application {
    @Override
    public void start(Stage stage) {
        // 1) Datenbank initialisieren
        try {
            DatabaseManager.createConnectionSource();
            DatabaseManager.createTables();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR,
                    "Datenbankfehler",
                    "Die Anwendung konnte die lokale Datenbank nicht öffnen.\n" +
                            "Bitte überprüfe, ob du Schreib‑ und Leserechte im Programmordner hast.\n" +
                            "Die App wird nun beendet.");
            Platform.exit();
            return;
        }

        // 2) GUI laden
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FhmdbApplication.class.getResource("main.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 900, 620);
            scene.getStylesheets().add(
                    Objects.requireNonNull(FhmdbApplication.class.getResource("styles.css")).toExternalForm()
            );
            stage.setTitle("FHMDb!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR,
                    "Startfehler",
                    "Die Benutzeroberfläche konnte nicht geladen werden.\n" +
                            "Bitte starte die App neu oder kontaktiere den Support, falls das Problem besteht.");
            Platform.exit();
        }
    }

    public static void main(String[] args) {
        launch();
    }

    // Utility‑Methode JFXAlerts
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
