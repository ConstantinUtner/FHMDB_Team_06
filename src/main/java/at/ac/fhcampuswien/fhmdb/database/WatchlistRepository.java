package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.observer.Observer;
import at.ac.fhcampuswien.fhmdb.observer.Observable;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WatchlistRepository implements Observable {

    private static WatchlistRepository instance;
    private final Dao<WatchlistMovieEntity, Long> dao;
    private final List<Observer> observers = new ArrayList<>();

    // Konstruktor – holt das DAO vom DatabaseManager
    private WatchlistRepository() throws DatabaseException{
        try {
            this.dao = DatabaseManager.getWatchlistDao();
        } catch (SQLException e) {
            throw new DatabaseException("WatchlistRepository: DAO für Watchlist konnte nicht initialisiert werden. " +
                    "Bitte prüfe die Datenbankverbindung und ORMLite-Konfiguration.", e);
        }
    }

    public static WatchlistRepository getInstance() throws DatabaseException {
        if (instance == null) {
            instance = new WatchlistRepository();
        }
        return instance;
    }

    @Override
    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    // Gibt alle gespeicherten Watchlist-Einträge zurück
    public List<WatchlistMovieEntity> getAll() throws DatabaseException {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new DatabaseException("WatchlistRepository: Fehler beim Auslesen aller Watchlist-Einträge. " +
                    "Möglicherweise existiert die Tabelle nicht oder die Verbindung zur DB ist unterbrochen.", e);
        }
    }


    // Fügt einen Film zur Watchlist hinzu, wenn er noch nicht existiert
    public boolean add(Movie movie) throws DatabaseException {
        try {
            WatchlistMovieEntity existing = dao.queryBuilder()
                    .where().eq("apiId", movie.getId())
                    .queryForFirst();
            if (existing == null) {
                dao.create(new WatchlistMovieEntity(movie.getId()));
                notifyObservers("Movie successfully added to watchlist");
                return true;
            }
            notifyObservers("Movie already on watchlist");
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("WatchlistRepository: Fehler beim Hinzufügen des Films '" + movie.getTitle() +
                    "' (API-ID: " + movie.getId() + ") zur Watchlist. Prüfe, ob der Eintrag bereits existiert oder ob " +
                    "die DB-Verbindung funktioniert.", e);
        }
    }

    public int removeFromWatchlist(String apiId) throws DatabaseException{
        try {
            var deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq("apiId", apiId);
            return dao.delete(deleteBuilder.prepare());
        } catch (SQLException e) {
            throw new DatabaseException("WatchlistRepository: Fehler beim Löschen des Films mit API-ID '" + apiId +
                    "' aus der Watchlist. Prüfe die korrekte API-ID und DB-Verbindung.", e);
        }
    }

    public boolean isInWatchlist(Movie movie) throws DatabaseException {
        try {
            WatchlistMovieEntity existing = dao.queryBuilder()
                    .where().eq("apiId", movie.getId())
                    .queryForFirst();
            return existing != null;
        } catch (SQLException e) {
            throw new DatabaseException("WatchlistRepository: Fehler beim Überprüfen des Films '" + movie.getTitle() +
                    "' (API-ID: " + movie.getId() + ") zur Watchlist. Prüfe, ob der Eintrag bereits existiert oder ob " +
                    "die DB-Verbindung funktioniert.", e);
        }
    }

}
