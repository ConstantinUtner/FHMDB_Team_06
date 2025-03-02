package at.ac.fhcampuswien.fhmdb.models;

import java.util.List;

public class Movie implements Comparable<Movie> {
    private String title;
    private String description;
    private List<Genre> genres;

    public Movie(String title, String description, List<Genre> genres) {
        this.title = title;
        this.description = description;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    @Override
    public int compareTo(Movie o) {
        return this.getTitle().compareTo(o.getTitle());
    }

    public static List<Movie> initializeMovies(){
        List<Movie> movies;

        Movie aSilentVoice = new Movie("A Silent Voice", "A young man is ostracized by his classmates after bullying a deaf girl so severely that she eventually moves away. Years later, he sets out on a journey to seek forgiveness.", List.of(Genre.ANIMATION, Genre.DRAMA, Genre.ROMANCE));
        Movie interstellar = new Movie("Interstellar", "A team of researchers travels through a wormhole in the universe in search of new worlds to ensure the survival of humanity.", List.of(Genre.SCIENCE_FICTION, Genre.ACTION, Genre.DRAMA));
        Movie wallE = new Movie("WALL·E", "In the distant future, a small waste-collecting robot accidentally embarks on a journey into space—one that ultimately determines the fate of humanity.", List.of(Genre.ANIMATION, Genre.ROMANCE, Genre.FAMILY));
        Movie planetEarth = new Movie("Planet Earth", "Emmy-winning, 11 episodes, five years in production—the most expensive nature documentary series ever commissioned by the BBC and the first to be filmed in high definition.", List.of(Genre.DOCUMENTARY, Genre.FAMILY));
        Movie hamilton = new Movie("Hamilton", "A filmed version of the Broadway musical Hamilton by Lin-Manuel Miranda, telling the story of American founding father Alexander Hamilton—born and raised as an orphan in the Caribbean.", List.of(Genre.MUSICAL, Genre.HISTORY));
        Movie saw = new Movie ("Saw", "Two strangers wake up in a room with no memory of how they got there, only to discover they are pawns in the deadly game of a notorious serial killer.", List.of(Genre.HORROR,Genre.MYSTERY));
        Movie manitousShoe = new Movie("Manitou's Shoe", "The story of two best friends, Apache chief Abahachi and cowboy Ranger, set in the Wild West.", List.of(Genre.COMEDY));
        Movie twilight = new Movie("Twilight", "When Bella Swan moves to a small town in the Pacific Northwest, she falls in love with Edward Cullen, a mysterious classmate who turns out to be a 108-year-old vampire.", List.of(Genre.DRAMA, Genre.FANTASY));

        movies = List.of(aSilentVoice, interstellar, wallE, planetEarth, hamilton, saw, manitousShoe, twilight);
        return movies;
    }
}
