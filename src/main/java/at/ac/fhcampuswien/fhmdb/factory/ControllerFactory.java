package at.ac.fhcampuswien.fhmdb.factory;

import at.ac.fhcampuswien.fhmdb.HomeController;
import at.ac.fhcampuswien.fhmdb.WatchlistController;
import at.ac.fhcampuswien.fhmdb.MainController;
import javafx.util.Callback;

public class ControllerFactory implements Callback<Class<?>, Object> {

    private HomeController homeController;
    private WatchlistController watchlistController;
    private MainController mainController;

    @Override
    public Object call(Class<?> aClass) {
        try {
            if (aClass == HomeController.class) {
                if (homeController == null) {
                    homeController = new HomeController();
                }
                return homeController;
            }
            if (aClass == WatchlistController.class) {
                if (watchlistController == null) {
                    watchlistController = new WatchlistController();
                }
                return watchlistController;
            }
            if (aClass == MainController.class) {
                if (mainController == null) {
                    mainController = new MainController();
                }
                return mainController;
            }
            // Fallback: alle anderen Klassen normal instanziieren
            return aClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
