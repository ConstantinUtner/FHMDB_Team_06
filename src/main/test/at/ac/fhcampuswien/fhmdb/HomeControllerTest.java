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

    @Test
    void filterMovies_byGenre() {
        // WHEN
        List<Movie> actual = homeController.filter("", Genre.DRAMA);

        // THEN
        List<Movie> expected = List.of(homeController.allMovies.get(0), homeController.allMovies.get(1));
        assertEquals(2, actual.size());
        assertIterableEquals(expected, actual);
    }
}