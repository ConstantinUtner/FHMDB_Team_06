package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.exceptions.DatabaseException;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class MovieRepository {

    private static MovieRepository instance;
    private final Dao<MovieEntity, Long> dao;

    // Konstruktor – holt das DAO vom DatabaseManager
    private MovieRepository() throws DatabaseException {
        try {
            this.dao = DatabaseManager.getMovieDao();
        } catch (SQLException e) {
            throw new DatabaseException("MovieRepository: DAO für Filme konnte nicht initialisiert werden. " +
                    "Bitte überprüfe deine Datenbankverbindung und ORMLite-Einstellungen.", e);
        }
    }

    public static MovieRepository getInstance() throws DatabaseException {
        if (instance == null) {
            instance = new MovieRepository();
        }
        return instance;
    }

    // Gibt alle gespeicherten Filme aus der Tabelle zurück
    public List<MovieEntity> getAllMovies() throws DatabaseException {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new DatabaseException("MovieRepository: Fehler beim Lesen aller Filme aus der Datenbank. " +
                    "Möglicherweise ist die Tabelle nicht erstellt oder die DB-Verbindung unterbrochen.", e);
        }
    }

    // Gibt den ersten Film zurück
    public MovieEntity getMovie() throws DatabaseException {
        try {
            List<MovieEntity> all = dao.queryForAll();
            return all.isEmpty() ? null : all.get(0);
        } catch (SQLException e) {
            throw new DatabaseException("MovieRepository: Fehler beim Auslesen eines Films aus der DB. " +
                    "Überprüfe die Existenz der Tabelle und deine SQL-Abfragen.", e);
        }
    }

    // Fügt mehrere Filme hinzu (aus Movie → MovieEntity), wenn sie noch nicht existieren
    public int addAllMovies(List<Movie> movies) throws DatabaseException {
        try {
            int count = 0;
            for (Movie movie : movies) {
                MovieEntity existing = dao.queryBuilder()
                        .where().eq("apiId", movie.getId())
                        .queryForFirst();
                if (existing == null) {
                    dao.create(new MovieEntity(movie));
                    count++;
                }
            }
            return count;
        } catch (SQLException e) {
            throw new DatabaseException("MovieRepository: Fehler beim Speichern der Filme in der DB. " +
                    "Überprüfe, ob die API-IDs eindeutig sind, ob der Speicherplatz ausreicht oder " +
                    "die Verbindung zur DB besteht.", e);
        }
    }
}
