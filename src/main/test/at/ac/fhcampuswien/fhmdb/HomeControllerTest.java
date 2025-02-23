package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class HomeControllerTest {
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        homeController = new HomeController();

        // GIVEN: Replace allMovies with test list
        homeController.allMovies = List.of(
                new Movie("A Silent Voice", "A young man is ostracized by his classmates after bullying a deaf girl so severely that she eventually moves away. Years later, he sets out on a journey to seek forgiveness.", List.of(Genre.ANIMATION, Genre.DRAMA, Genre.ROMANCE)),
                new Movie("Interstellar", "A team of researchers travels through a wormhole in the universe in search of new worlds to ensure the survival of humanity.", List.of(Genre.SCIENCE_FICTION, Genre.ACTION, Genre.DRAMA)),
                new Movie ("Saw", "Two strangers wake up in a room with no memory of how they got there, only to discover they are pawns in the deadly game of a notorious serial killer.", List.of(Genre.HORROR,Genre.MYSTERY))
        );
    }

    @AfterEach
    void tearDown() {
        homeController = null;
    }

    // Tests for search text filter

    @Test
    void filterMovies_bySearchtext() {
        // WHEN
        List<Movie> actual = homeController.filter("Interstellar", null);

        // THEN
        List<Movie> expected = List.of(homeController.allMovies.get(1));
        assertEquals(1, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    void filterMovies_byPartialSearchtext() {
        // WHEN
        List<Movie> actual = homeController.filter("Interst", null);

        // THEN
        List<Movie> expected = List.of(homeController.allMovies.get(1));
        assertEquals(1, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    void filterMovies_bySearchtext_ignore_uppercase() {
        // WHEN
        List<Movie> actual = homeController.filter("InTeRsTeLlAr", null);

        // THEN
        List<Movie> expected = List.of(homeController.allMovies.get(1));
        assertEquals(1, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    void filter_searches_search_text_by_description() {
        // WHEN
        List<Movie> actual = homeController.filter("of researchers travels", null);

        // THEN
        List<Movie> expected = List.of(homeController.allMovies.get(1));
        assertEquals(1, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    void filter_searchText_does_not_exist_in_title_or_description() {
        // WHEN
        List<Movie> actual = homeController.filter("XYZ", null);

        // THEN
        List<Movie> expected = List.of();
        assertEquals(0, actual.size());
        assertIterableEquals(expected, actual);
    }

    // Tests for genre filter

    @Test
    void filterMovies_byGenre() {
        // WHEN
        List<Movie> actual = homeController.filter("", Genre.DRAMA);

        // THEN
        List<Movie> expected = List.of(homeController.allMovies.get(0), homeController.allMovies.get(1));
        assertEquals(2, actual.size());
        assertIterableEquals(expected, actual);
    }

    // Tests for combination of search text and genre

    @Test
    void filterMovies_bySearchtext_and_genre() {
        // WHEN
        List<Movie> actual = homeController.filter("Interstellar", Genre.ACTION);

        // THEN
        List<Movie> expected = List.of(homeController.allMovies.get(1));
        assertEquals(1, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    void filter_returns_emptyList_when_no_movie_matches_title_and_genre() {
        // WHEN
        List<Movie> actual = homeController.filter("Interstellar", Genre.MYSTERY);

        // THEN
        List<Movie> expected = List.of();
        assertEquals(0, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    void filter_combination_of_non_existing_searchText_and_existing_genre() {
        // WHEN
        List<Movie> actual = homeController.filter("XYZ", Genre.DRAMA);

        // THEN
        List<Movie> expected = List.of();
        assertEquals(0, actual.size());
        assertIterableEquals(expected, actual);
    }

    // Tests for edge cases

    @Test
    void filter_isEmpty() {
        // WHEN
        List<Movie> actual = homeController.filter("", null);

        // THEN
        List<Movie> expected = homeController.allMovies;
        assertEquals(3, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    void filter_bySearchText_isNull() {
        // WHEN
        List<Movie> actual = homeController.filter(null, null);

        // THEN
        List<Movie> expected = homeController.allMovies;
        assertEquals(3, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void test_filter_with_empty_dataSource() {
        // GIVEN: Replace allMovies with empty list
        homeController.allMovies = List.of();

        // WHEN
        List<Movie> actualWithoutSearchTextAndGenre = homeController.filter("", null);
        List<Movie> actualWithSearchTextOnly = homeController.filter("Interstellar", null);
        List<Movie> actualWithGenreOnly = homeController.filter("", Genre.DRAMA);
        List<Movie> actualWithSearchTextAndGenre = homeController.filter("Interstellar", Genre.DRAMA);

        // THEN
        assertTrue(actualWithoutSearchTextAndGenre.isEmpty());
        assertTrue(actualWithSearchTextOnly.isEmpty());
        assertTrue(actualWithGenreOnly.isEmpty());
        assertTrue(actualWithSearchTextAndGenre.isEmpty());
    }
}