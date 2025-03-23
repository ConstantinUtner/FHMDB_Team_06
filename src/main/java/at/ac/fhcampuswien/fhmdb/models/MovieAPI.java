package at.ac.fhcampuswien.fhmdb.models;

import com.google.gson.Gson;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MovieAPI {

    private static final String BASE_URL = "https://prog2.fh-campuswien.ac.at/movies";

    // URL Builder
    private static String buildUrl(String query, Genre genre, String releaseYear, String ratingFrom) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(BASE_URL), "Error: BASE_URL is invalid. Please check the URL.").newBuilder();

        if (query != null && !query.isEmpty()) {
            urlBuilder.addQueryParameter("query", query);
        }
        if (genre != null) {
            urlBuilder.addQueryParameter("genre", genre.name());
        }
        if (releaseYear != null && !releaseYear.isEmpty()) {
            urlBuilder.addQueryParameter("releaseYear", releaseYear);
        }
        if (ratingFrom != null && !ratingFrom.isEmpty()) {
            urlBuilder.addQueryParameter("ratingFrom", ratingFrom);
        }

        return urlBuilder.build().toString();
    }

    // Get Movies from API
    public static List<Movie> getMovies(String query, Genre genre, String releaseYear, String ratingFrom) {
        OkHttpClient client = new OkHttpClient();
        String url = buildUrl(query, genre, releaseYear, ratingFrom);

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "http.agent")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) { // If the HTTP response is not in the 2xx range
                System.out.println("Could not connect to the API. (HTTP error code: " + response.code() + ")");
                return new ArrayList<>();
            }
            String json = response.body().string(); // Get the response body as a JSON string
            Gson gson = new Gson();
            Movie[] movies = gson.fromJson(json, Movie[].class); // Deserialize JSON into array of Movie objects
            return Arrays.asList(movies);
        } catch (IOException e) {
            System.out.println("Could not connect to the API. Please check your internet connection.");
            e.printStackTrace();
            return new ArrayList<>(); // Return empty list on network failure
        }
    }

}

