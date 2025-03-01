package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HomeControllerTest {
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        homeController = new HomeController();
        homeController.allMovies = List.of(
                new Movie("A Silent Voice", "A young man is ostracized by his classmates after bullying a deaf girl so severely that she eventually moves away. Years later, he sets out on a journey to seek forgiveness.", List.of(Genre.ANIMATION, Genre.DRAMA, Genre.ROMANCE)),
                new Movie("Interstellar", "A team of researchers travels through a wormhole in the universe in search of new worlds to ensure the survival of humanity.", List.of(Genre.SCIENCE_FICTION, Genre.ACTION, Genre.DRAMA)),
                new Movie("WALL·E", "In the distant future, a small waste-collecting robot accidentally embarks on a journey into space—one that ultimately determines the fate of humanity.", List.of(Genre.ANIMATION, Genre.ROMANCE, Genre.FAMILY)),
                new Movie("Planet Earth", "Emmy-winning, 11 episodes, five years in production—the most expensive nature documentary series ever commissioned by the BBC and the first to be filmed in high definition.", List.of(Genre.DOCUMENTARY, Genre.FAMILY)),
                new Movie("Hamilton", "A filmed version of the Broadway musical Hamilton by Lin-Manuel Miranda, telling the story of American founding father Alexander Hamilton—born and raised as an orphan in the Caribbean.", List.of(Genre.MUSICAL, Genre.HISTORY)),
                new Movie ("Saw", "Two strangers wake up in a room with no memory of how they got there, only to discover they are pawns in the deadly game of a notorious serial killer.", List.of(Genre.HORROR,Genre.MYSTERY)),
                new Movie("Manitou's Shoe", "The story of two best friends, Apache chief Abahachi and cowboy Ranger, set in the Wild West.", List.of(Genre.COMEDY)),
                new Movie("Twilight", "When Bella Swan moves to a small town in the Pacific Northwest, she falls in love with Edward Cullen, a mysterious classmate who turns out to be a 108-year-old vampire.", List.of(Genre.DRAMA, Genre.FANTASY))
        );
        homeController.observableMovies.addAll(homeController.allMovies);
    }

    @Test
    void sortMoviesAscending() {
        homeController.ascending = true;
        homeController.sortMovies();

        assertEquals("A Silent Voice", homeController.observableMovies.get(0).getTitle());
        assertEquals("Hamilton", homeController.observableMovies.get(1).getTitle());
        assertEquals("Interstellar", homeController.observableMovies.get(2).getTitle());
    }

    @Test
    void sortMoviesDescending() {
        homeController.ascending = false;
        homeController.sortMovies();

        assertEquals("WALL·E", homeController.observableMovies.get(0).getTitle());
        assertEquals("Twilight", homeController.observableMovies.get(1).getTitle());
        assertEquals("Saw", homeController.observableMovies.get(2).getTitle());
    }

    @Test
    void sortingMovies() {
        // ascending
        homeController.ascending = true;
        homeController.sortMovies();
        assertEquals("A Silent Voice", homeController.observableMovies.get(0).getTitle());

        // descending
        homeController.sortMovies();
        assertEquals("WALL·E", homeController.observableMovies.get(0).getTitle());
    }

    @Test
    void sortMoviesOnEmptyList() {
        // list stays empty when sorted empty
        homeController.observableMovies.clear();
        homeController.sortMovies();

        assertTrue(homeController.observableMovies.isEmpty());
    }

    @AfterEach
    void tearDown() {
        homeController = null;
    }
}