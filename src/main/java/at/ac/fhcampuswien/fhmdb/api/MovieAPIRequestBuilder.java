package at.ac.fhcampuswien.fhmdb.api;

public class MovieAPIRequestBuilder {
    private final StringBuilder urlBuilder;

    public MovieAPIRequestBuilder(String base) {
        this.urlBuilder = new StringBuilder(base);
    }

    public MovieAPIRequestBuilder query(String query) {
        addParam("query", query);
        return this;
    }

    public MovieAPIRequestBuilder genre(String genre) {
        addParam("genre", genre);
        return this;
    }

    public MovieAPIRequestBuilder releaseYear(String year) {
        addParam("releaseYear", year);
        return this;
    }

    public MovieAPIRequestBuilder ratingFrom(String rating) {
        addParam("ratingFrom", rating);
        return this;
    }

    public String build() {
        return urlBuilder.toString();
    }

    private void addParam(String key, String value) {
        if (value == null || value.isEmpty()) return; // Nichts tun, wenn leer
        if (urlBuilder.toString().contains("?")) {
            urlBuilder.append("&");
        } else {
            urlBuilder.append("?");
        }
        urlBuilder.append(key).append("=").append(value);
    }
}
