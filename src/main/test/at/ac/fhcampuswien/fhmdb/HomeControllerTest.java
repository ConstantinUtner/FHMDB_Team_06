package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class HomeControllerTest {
    private HomeController homeController;

    @BeforeEach
    public void setUp() {
        homeController = new HomeController();

        // GIVEN: Replace allMovies with test list
        homeController.allMovies = List.of(
                new Movie(
                        "A Silent Voice",
                        "A young man is ostracized by his classmates after bullying a deaf girl so severely that she eventually moves away. Years later, he sets out on a journey to seek forgiveness.",
                        List.of(Genre.ANIMATION, Genre.DRAMA, Genre.ROMANCE),
                        "M001",
                        2016,
                        "https://example.com/a_silent_voice.jpg",
                        130,
                        List.of("Naoko Yamada"),
                        List.of("Reiko Yoshida"),
                        List.of("Miyu Irino", "Saori Hayami"),
                        8.2
                ),
                new Movie(
                        "Interstellar",
                        "A team of researchers travels through a wormhole in the universe in search of new worlds to ensure the survival of humanity.",
                        List.of(Genre.SCIENCE_FICTION, Genre.ACTION, Genre.DRAMA),
                        "M002",
                        2014,
                        "https://example.com/interstellar.jpg",
                        169,
                        List.of("Christopher Nolan"),
                        List.of("Jonathan Nolan", "Christopher Nolan"),
                        List.of("Matthew McConaughey", "Anne Hathaway", "Jessica Chastain"),
                        8.6
                ),
                new Movie(
                        "Saw",
                        "Two strangers wake up in a room with no memory of how they got there, only to discover they are pawns in the deadly game of a notorious serial killer.",
                        List.of(Genre.HORROR, Genre.MYSTERY),
                        "M003",
                        2004,
                        "https://example.com/saw.jpg",
                        103,
                        List.of("James Wan"),
                        List.of("Leigh Whannell"),
                        List.of("Cary Elwes", "Leigh Whannell", "Danny Glover"),
                        7.6
                ),
                new Movie(
                        "The Curious Case of Benjamin Button",
                        "Tells the story of Benjamin Button, a man who starts aging backwards with bizarre consequences.",
                        List.of(Genre.DRAMA, Genre.FANTASY, Genre.ROMANCE),
                        "M004",
                        2008,
                        "https://example.com/benjamin_button.jpg",
                        166,
                        List.of("David Fincher"),
                        List.of("Eric Roth"),
                        List.of("Brad Pitt", "Cate Blanchett"),
                        7.8
                ),
                new Movie(
                        "Dallas Buyers Club",
                        "In 1985 Dallas, electrician and hustler Ron Woodroof works around the system to help AIDS patients get the medication they need.",
                        List.of(Genre.DRAMA, Genre.BIOGRAPHY),
                        "M005",
                        2013,
                        "https://example.com/dallas_buyers_club.jpg",
                        117,
                        List.of("Jean-Marc Vallée"),
                        List.of("Craig Borten", "Melisa Wallack"),
                        List.of("Matthew McConaughey", "Jared Leto", "Jennifer Garner"),
                        8.0
                ),
                new Movie(
                        "Inception",
                        "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a CEO.",
                        List.of(Genre.ACTION, Genre.SCIENCE_FICTION, Genre.THRILLER),
                        "M006",
                        2010,
                        "https://example.com/inception.jpg",
                        148,
                        List.of("Christopher Nolan"),
                        List.of("Christopher Nolan"),
                        List.of("Leonardo DiCaprio", "Joseph Gordon-Levitt", "Elliot Page"),
                        8.8
                )
        );
        homeController.refreshMovies();
        homeController.sortMovies(true);
    }

    @AfterEach
    public void tearDown() {
        homeController = null;
    }

    @Test
    public void sort_movies_ascending() {
        homeController.sortMovies(true);

        List<String> expectedOrder = List.of(
                "A Silent Voice",
                "Dallas Buyers Club",
                "Inception",
                "Interstellar",
                "Saw",
                "The Curious Case of Benjamin Button"
        );

        List<String> actualOrder = homeController.getShownMovies().stream()
                .map(Movie::getTitle)
                .toList();

        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    public void sort_movies_descending_fullList() {
        homeController.sortMovies(false);

        List<String> expectedOrder = List.of(
                "The Curious Case of Benjamin Button",
                "Saw",
                "Interstellar",
                "Inception",
                "Dallas Buyers Club",
                "A Silent Voice"
        );

        List<String> actualOrder = homeController.getShownMovies().stream()
                .map(Movie::getTitle)
                .toList();

        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    public void sort_movies_both_ways() {
        // ascending
        homeController.sortMovies(true);
        assertEquals("A Silent Voice", homeController.getShownMovies().get(0).getTitle());

        // descending
        homeController.sortMovies(false);
        assertEquals("The Curious Case of Benjamin Button", homeController.getShownMovies().get(0).getTitle());
    }

    @Test
    public void sort_on_empty_list() {
        // list stays empty when sorted empty
        homeController.allMovies = List.of();
        homeController.refreshMovies();

        homeController.sortMovies(true);

        assertTrue(homeController.getShownMovies().isEmpty());
    }

    // Tests for search text filter

    @Test
    public void filter_by_searchtext() {
        // WHEN
        homeController.filterMovies("Interstellar", null);
        List<Movie> actual = homeController.getShownMovies();

        // THEN
        List<Movie> expected = List.of(homeController.allMovies.get(1));
        assertEquals(1, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void filter_by_partial_searchtext() {
        // WHEN
        homeController.filterMovies("Interst", null);
        List<Movie> actual = homeController.getShownMovies();

        // THEN
        List<Movie> expected = List.of(homeController.allMovies.get(1));
        assertEquals(1, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void filter_by_searchtext_ignore_uppercase() {
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
    public void filter_by_genre() {
        // WHEN
        homeController.filterMovies("", Genre.DRAMA);
        List<Movie> actual = homeController.getShownMovies();

        // THEN
        // Filme mit DRAMA: A Silent Voice, Interstellar, Benjamin Button, Dallas Buyers Club
        List<Movie> expected = List.of(
                homeController.allMovies.get(0), // A Silent Voice
                homeController.allMovies.get(1), // Interstellar
                homeController.allMovies.get(3), // Benjamin Button
                homeController.allMovies.get(4)  // Dallas Buyers Club
        );
        assertEquals(4, actual.size());
        assertIterableEquals(expected, actual);
    }

    // Tests for combination of search text and genre

    @Test
    public void filter_by_searchtext_and_genre() {
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
        assertEquals(6, actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void filter_by_searchText_isNull() {
        // WHEN
        homeController.filterMovies(null, null);
        List<Movie> actual = homeController.getShownMovies();

        // THEN
        List<Movie> expected = homeController.allMovies;
        assertEquals(6, actual.size());
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

    @ParameterizedTest
    @CsvSource({
            "Interstellar, 1",
            "St, 6",                 // We find "st" in the title and description of every of the 6 movies, since the query is case insensitive
            "Nonexistent, 0",
            "'', 6"
    })
    public void filter_finds_correct_number_of_movies(String searchText, int expectedCount) {
        homeController.filterMovies(searchText, null);
        List<Movie> actualMovies = homeController.getShownMovies();
        assertEquals(expectedCount, actualMovies.size());
    }

    // Java Streams
    private List<Movie> getTestMovieList(int movieListIdx){
        if (movieListIdx == 0){
            return homeController.allMovies;
        }
        else if (movieListIdx == 1) {
            //silent voice
            return List.of(homeController.allMovies.get(0));
        }
        else if (movieListIdx == 2) {
            return new ArrayList<>();
        }
        return null;
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Matthew McConaughey, 0",
            "Saori Hayami, 1",
            "'', 2",
            "null, 3"
    }, nullValues={"null"})
    public void test_get_most_popular_actor(String actor, int listIdx) {
        String popularActor = homeController.getMostPopularActor(getTestMovieList(listIdx));
        assertEquals(actor, popularActor);
    }

    @ParameterizedTest
    @CsvSource({
            "35, 0",
            "14, 1",
            "0, 2",
            "-1, 3"
    })
    public void test_get_longest_movie_title(int count, int listIdx) {
        int longestTitleLength = homeController.getLongestMovieTitle(getTestMovieList(listIdx));
        assertEquals(count, longestTitleLength);
    }

    @ParameterizedTest
    @CsvSource({
            "2, Christopher Nolan, 0",
            "1, Naoko Yamada, 1",
            "0, Tom Holland, 2",
            "-1, Viktoria Angelmayer, 3"
    })
    public void test_count_movies_from(long expectedCount, String director, int listIdx) {
        long count = homeController.countMoviesFrom(getTestMovieList(listIdx), director);
        assertEquals(expectedCount, count);
    }

    @ParameterizedTest
    @CsvSource({
            "2, 2014, 2016, 0",
            "0, 2014, -2016, 0",
            "6, 0, 9000, 0",
            "1, 2014, 2016, 1",
            "0, 2014, 2016, 2",
            "0, 2014, 2016, 3"
    })
    public void test_get_movies_between_years_counts(int expectedCount, int startYear, int endYear, int listIdx) {
        List<Movie> list = getTestMovieList(listIdx);
        List<Movie> moviesBetween = homeController.getMoviesBetweenYears(list, startYear, endYear);
        try {
            assertEquals(expectedCount, moviesBetween.size());
        }
        catch (NullPointerException e) {
            if (list != null){
                fail("Returned list is null but should not be!");
            }
        }
   }

   @Test
    public void test_get_movies_between_years() {
        List<Movie> moviesBetween = homeController.getMoviesBetweenYears(homeController.allMovies, 2014, 2016);
        assertEquals(homeController.allMovies.get(0), moviesBetween.get(0));
        assertEquals(homeController.allMovies.get(1), moviesBetween.get(1));
    }
}