package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "watchlist")
public class WatchlistMovieEntity {
    @DatabaseField(generatedId = true)
    public long id;

    @DatabaseField
    public String apiId;

    public WatchlistMovieEntity() {}

    public WatchlistMovieEntity(String apiId) {
        this.apiId = apiId;
    }
}
