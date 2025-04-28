package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseManager {

    // Attribute
    private static final String DB_URL = "jdbc:h2:file:./db/fhmdb";
    private static final String username = "sa";
    private static final String password = "";

    private static ConnectionSource conn;

    private static Dao<MovieEntity, Long> movieDao;
    private static Dao<WatchlistMovieEntity, Long> watchlistDao;

    //Methoden

    public static void createConnectionSource() throws SQLException {
        if (conn == null) {
            conn = new JdbcConnectionSource(DB_URL, username, password);
        }
    }

    public static ConnectionSource getConnectionSource() throws SQLException {
        if (conn == null) {
            createConnectionSource();
        }
        return conn;
    }

    public static void createTables() throws SQLException {
        if (conn == null) {
            createConnectionSource();
        }

        // Tabellen anlegen (wenn sie noch nicht existieren)
        TableUtils.createTableIfNotExists(conn, MovieEntity.class);
        TableUtils.createTableIfNotExists(conn, WatchlistMovieEntity.class);
    }

    public static Dao<MovieEntity, Long> getMovieDao() throws SQLException {
        if (movieDao == null) {
            movieDao = DaoManager.createDao(getConnectionSource(), MovieEntity.class);
        }
        return movieDao;
    }

    public static Dao<WatchlistMovieEntity, Long> getWatchlistDao() throws SQLException {
        if (watchlistDao == null) {
            watchlistDao = DaoManager.createDao(getConnectionSource(), WatchlistMovieEntity.class);
        }
        return watchlistDao;
    }
}
