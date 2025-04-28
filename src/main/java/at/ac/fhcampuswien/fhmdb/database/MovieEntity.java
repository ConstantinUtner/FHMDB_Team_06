package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@DatabaseTable(tableName = "movies")
public class MovieEntity {

    @DatabaseField(generatedId = true)
    public long id;

    @DatabaseField
    public String apiId;

    @DatabaseField
    public String title;

    @DatabaseField
    public String description;

    @DatabaseField
    public String genres;

    @DatabaseField
    public int releaseYear;

    @DatabaseField
    public String imgUrl;

    @DatabaseField
    public int lengthInMinutes;

    @DatabaseField
    public double rating;

    // No-Args Constructor (ORMLite benötigt diesen)
    public MovieEntity() {
    }

    // Konstruktor für Entity aus Movie
    public MovieEntity(Movie movie) {
        this.apiId = movie.getId();
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.genres = genresToString(movie.getGenres());
        this.releaseYear = movie.getReleaseYear();
        this.imgUrl = movie.getImgUrl();
        this.lengthInMinutes = movie.getLengthInMinutes();
        this.rating = movie.getRating() != null ? movie.getRating().doubleValue() : 0.0; // .doubleValue() sagt Java -> dieses Number Objekt (movie.getRating()) ist ein double
    }

    // Genres -> Komma-separierter String
    public static String genresToString(List<Genre> genres) {
        return genres.stream()
                .map(genre -> genre.name())
                .collect(Collectors.joining(","));
    }

    // Konvertiert Movie -> MovieEntity Liste
    public static List<MovieEntity> fromMovies(List<Movie> movies) {
        return movies.stream()
                .map(movie -> new MovieEntity(movie))
                .collect(Collectors.toList());
    }

    // Konvertiert MovieEntity -> Movie Liste
    public static List<Movie> toMovies(List<MovieEntity> movieEntities) {
        return movieEntities.stream()  // Starte einen Stream über die List<MovieEntity>
            .map(entity -> { // Für jede einzelne MovieEntity mache ...

                List<Genre> genreList = Arrays.stream(entity.genres.split(","))  // "DRAMA,COMEDY" → ["ACTION", "COMEDY"]
                        .map(genre -> genre.trim()) // → entfernt Leerzeichen " DRAMA " → "DRAMA"
                        .map(genre -> Genre.valueOf(genre)) // → konvertiere String zu Enum: "DRAMA" → Genre.DRAMA
                        .collect(Collectors.toList()); // → List<Genre>

                // Erstelle ein neues Movie-Objekt mit allen Werten
                return new Movie(
                        entity.title,
                        entity.description,
                        genreList, // Die genreList, die wir gerade erstellt haben
                        entity.apiId,
                        entity.releaseYear,
                        entity.imgUrl,
                        entity.lengthInMinutes,
                        new ArrayList<>(), // Leere Liste für directors (nicht in DB gespeichert)
                        new ArrayList<>(), // Leere Liste für writers
                        new ArrayList<>(), // Leere Liste für mainCast
                        entity.rating // Autoboxing von double -> Double (= Subtyp von Number, deshalb ok)
                );
            })
            .collect(Collectors.toList());  // Sammle alle zurückgegebenen Movie-Objekte in einer neuen List<Movie>
    }
}
