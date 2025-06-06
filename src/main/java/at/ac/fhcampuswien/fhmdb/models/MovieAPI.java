package at.ac.fhcampuswien.fhmdb.models;

import at.ac.fhcampuswien.fhmdb.exceptions.MovieApiException;  // ✅ GEÄNDERT: Exception importiert
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import at.ac.fhcampuswien.fhmdb.api.MovieAPIRequestBuilder;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MovieAPI {

    private static final String BASE_URL = "https://prog2.fh-campuswien.ac.at/movies";

    // URL Builder
    private static String buildUrl(String query, Genre genre, String releaseYear, String ratingFrom) {
        return new MovieAPIRequestBuilder(BASE_URL)
                .query(query)
                .genre(genre != null ? genre.name() : null)
                .releaseYear(releaseYear)
                .ratingFrom(ratingFrom)
                .build();
    }


    // Get Movies from API
    public static List<Movie> getMovies(String query, Genre genre, String releaseYear, String ratingFrom) throws MovieApiException {
        OkHttpClient client = new OkHttpClient();
        String url = buildUrl(query, genre, releaseYear, ratingFrom);

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "http.agent")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) { // If the HTTP response is not in the 2xx range
                // Exception bei HTTP-Fehler
                throw new MovieApiException(
                        "API-Fehler: HTTP " + response.code(),
                        null
                );
            }
            String json = response.body().string(); // Get the response body as a JSON string
            Gson gson = new Gson();
            Movie[] movies = gson.fromJson(json, Movie[].class); // Deserialize JSON into array of Movie objects
            return Arrays.asList(movies);
        } catch (IOException e) {
            // Exception bei Netzwerkfehler
            throw new MovieApiException(
                    "Netzwerkfehler beim Abrufen der Filme", e
            );
        }
    }

}
