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
    public void setUp() {
        homeController = new HomeController();

        // GIVEN: Replace allMovies with test list
        homeController.allMovies = List.of(
                new Movie("A Silent Voice", "A young man is ostracized by his classmates after bullying a deaf girl so severely that she eventually moves away. Years later, he sets out on a journey to seek forgiveness.", List.of(Genre.ANIMATION, Genre.DRAMA, Genre.ROMANCE)),
                new Movie("Interstellar", "A team of researchers travels through a wormhole in the universe in search of new worlds to ensure the survival of humanity.", List.of(Genre.SCIENCE_FICTION, Genre.ACTION, Genre.DRAMA)),
                new Movie ("Saw", "Two strangers wake up in a room with no memory of how they got there, only to discover they are pawns in the deadly game of a notorious serial killer.", List.of(Genre.HORROR,Genre.MYSTERY))
        );
        homeController.refreshMovies();
        homeController.sortMovies(true);
    }

    @AfterEach
    public void tearDown() {
        homeController = null;
    }
  
    @Test
    public void sortMoviesAscending() {
        homeController.sortMovies(true);

        assertEquals("A Silent Voice", homeController.getShownMovies().get(0).getTitle());
        assertEquals("Interstellar", homeController.getShownMovies().get(1).getTitle());
        assertEquals("Saw", homeController.getShownMovies().get(2).getTitle());
    }

    @Test
    public void sortMoviesDescending() {
        homeController.sortMovies(false);

        assertEquals("Saw", homeController.getShownMovies().get(0).getTitle());
        assertEquals("Interstellar", homeController.getShownMovies().get(1).getTitle());
        assertEquals("A Silent Voice", homeController.getShownMovies().get(2).getTitle());
    }

    @Test
    public void sortingMovies() {
        // ascending
        homeController.sortMovies(true);
        assertEquals("A Silent Voice", homeController.getShownMovies().get(0).getTitle());

        // descending
        homeController.sortMovies(false);
        assertEquals("Saw", homeController.getShownMovies().get(0).getTitle());
    }

    @Test
    public void sortMoviesOnEmptyList() {
        // list stays empty when sorted empty
        homeController.allMovies = List.of();
        homeController.refreshMovies();

        homeController.sortMovies(true);

        assertTrue(homeController.getShownMovies().isEmpty());
    }

    // Tests for search text filter

    @Test
    public void filterMovies_bySearchtext() {
        // WHEN
        homeController.filterMovies("Interstellar", null);
        List<Movie> actual = homeController.getShownMovies();

        // THEN
        List<Movie> expected = List.of(homeController.allMovies.get(1));
        assertEquals(1, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void filterMovies_byPartialSearchtext() {
        // WHEN
        homeController.filterMovies("Interst", null);
        List<Movie> actual = homeController.getShownMovies();

        // THEN
        List<Movie> expected = List.of(homeController.allMovies.get(1));
        assertEquals(1, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void filterMovies_bySearchtext_ignore_uppercase() {
        // WHEN
        homeController.filterMovies("InTeRsTeLlAr", null);
        List<Movie> actual = homeController.getShownMovies();

        // THEN
        List<Movie> expected = List.of(homeController.allMovies.get(1));
        assertEquals(1, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void filter_searches_search_text_by_description() {
        // WHEN
        homeController.filterMovies("of researchers travels", null);
        List<Movie> actual = homeController.getShownMovies();

        // THEN
        List<Movie> expected = List.of(homeController.allMovies.get(1));
        assertEquals(1, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void filter_searchText_does_not_exist_in_title_or_description() {
        // WHEN
        homeController.filterMovies("XYZ", null);
        List<Movie> actual = homeController.getShownMovies();

        // THEN
        List<Movie> expected = List.of();
        assertEquals(0, actual.size());
        assertIterableEquals(expected, actual);
    }

    // Tests for genre filter

    @Test
    public void filterMovies_byGenre() {
        // WHEN
        homeController.filterMovies("", Genre.DRAMA);
        List<Movie> actual = homeController.getShownMovies();

        // THEN
        List<Movie> expected = List.of(homeController.allMovies.get(0), homeController.allMovies.get(1));
        assertEquals(2, actual.size());
        assertIterableEquals(expected, actual);
    }

    // Tests for combination of search text and genre

    @Test
    public void filterMovies_bySearchtext_and_genre() {
        // WHEN
        homeController.filterMovies("Interstellar", Genre.ACTION);
        List<Movie> actual = homeController.getShownMovies();

        // THEN
        List<Movie> expected = List.of(homeController.allMovies.get(1));
        assertEquals(1, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void filter_returns_emptyList_when_no_movie_matches_title_and_genre() {
        // WHEN
        homeController.filterMovies("Interstellar", Genre.MYSTERY);
        List<Movie> actual = homeController.getShownMovies();

        // THEN
        List<Movie> expected = List.of();
        assertEquals(0, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void filter_combination_of_non_existing_searchText_and_existing_genre() {
        // WHEN
        homeController.filterMovies("XYZ", Genre.DRAMA);
        List<Movie> actual = homeController.getShownMovies();

        // THEN
        List<Movie> expected = List.of();
        assertEquals(0, actual.size());
        assertIterableEquals(expected, actual);
    }

    // Tests for edge cases

    @Test
    public void filter_isEmpty() {
        // WHEN
        homeController.filterMovies("", null);
        List<Movie> actual = homeController.getShownMovies();

        // THEN
        List<Movie> expected = homeController.allMovies;
        assertEquals(3, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void filter_bySearchText_isNull() {
        // WHEN
        homeController.filterMovies(null, null);
        List<Movie> actual = homeController.getShownMovies();

        // THEN
        List<Movie> expected = homeController.allMovies;
        assertEquals(3, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void filter_with_empty_dataSource() {
        // GIVEN: Replace allMovies with empty list
        homeController.allMovies = List.of();

        // WHEN
        homeController.filterMovies("", null);
        List<Movie> actualWithoutSearchTextAndGenre = homeController.getShownMovies();
        assertTrue(actualWithoutSearchTextAndGenre.isEmpty());

        homeController.filterMovies("Interstellar", null);
        List<Movie> actualWithSearchTextOnly = homeController.getShownMovies();
        assertTrue(actualWithSearchTextOnly.isEmpty());

        homeController.filterMovies("", Genre.DRAMA);
        List<Movie> actualWithGenreOnly = homeController.getShownMovies();
        assertTrue(actualWithGenreOnly.isEmpty());

        homeController.filterMovies("Interstellar", Genre.DRAMA);
        List<Movie> actualWithSearchTextAndGenre = homeController.getShownMovies();
        assertTrue(actualWithSearchTextAndGenre.isEmpty());
    }
}