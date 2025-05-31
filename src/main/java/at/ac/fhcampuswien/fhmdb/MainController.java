package at.ac.fhcampuswien.fhmdb;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

import at.ac.fhcampuswien.fhmdb.factory.ControllerFactory;

public class MainController {

    private final ControllerFactory controllerFactory = new ControllerFactory();

    @FXML
    private StackPane mainContent;

    @FXML
    private VBox sidebar;

    @FXML
    private Label hamburgerIcon;

    private boolean isSidebarVisible = false;

    @FXML
    public void initialize() {
        loadHomeView();
    }

    @FXML
    public void toggleSidebar() {
        isSidebarVisible = !isSidebarVisible;
        sidebar.setVisible(isSidebarVisible);
        sidebar.setManaged(isSidebarVisible);
        hamburgerIcon.setText(isSidebarVisible ? "✖" : "☰");
    }

    @FXML
    public void loadHomeView() {
        loadView("home-view.fxml");
        hideSidebar();
    }

    @FXML
    public void loadWatchlistView() {
        loadView("watchlist-view.fxml");
        hideSidebar();
    }

    private void hideSidebar() {
        isSidebarVisible = false;
        sidebar.setVisible(false);
        sidebar.setManaged(false);
        hamburgerIcon.setText("☰");
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            loader.setControllerFactory(controllerFactory);
            Pane view = loader.load();
            mainContent.getChildren().setAll(view);
        } catch (IOException e) {
            System.err.println("Could not load view: " + fxmlFile);
            e.printStackTrace();
        }
    }
}
