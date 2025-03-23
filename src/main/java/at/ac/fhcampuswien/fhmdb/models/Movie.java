package at.ac.fhcampuswien.fhmdb.models;

import java.util.List;

public class Movie implements Comparable<Movie> {
    private String id;
    private String title;
    private List<Genre> genres;
    private int releaseYear;
    private String description;
    private String imgUrl;
    private int lengthInMinutes;
    private List<String> directors;
    private List<String> writers;
    private List<String> mainCast;
    private Number rating;

    public Movie(String title, String description, List<Genre> genres,
                 String id, int releaseYear, String imgUrl, int lengthInMinutes,
                 List<String> directors, List<String> writers,
                 List<String> mainCast, Number rating) {
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.id = id;
        this.releaseYear = releaseYear;
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.directors = directors;
        this.writers = writers;
        this.mainCast = mainCast;
        this.rating = rating;
    }

    @Override
    public int compareTo(Movie other) {
        return title.compareToIgnoreCase(other.title);
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

    public int getReleaseYear() {
        return releaseYear;
    }

    //    public static List<Movie> initializeMovies(){
//        List<Movie> movies;
//
//        Movie aSilentVoice = new Movie(
//                "A Silent Voice",
//                "A young man is ostracized by his classmates after bullying a deaf girl so severely that she eventually moves away. Years later, he sets out on a journey to seek forgiveness.",
//                List.of(Genre.ANIMATION, Genre.DRAMA, Genre.ROMANCE),
//                "M001",
//                2016,
//                "https://example.com/a_silent_voice.jpg",
//                130,
//                List.of("Naoko Yamada"),
//                List.of("Reiko Yoshida"),
//                List.of("Miyu Irino", "Saori Hayami"),
//                8.2
//        );
//
//        Movie interstellar = new Movie(
//                "Interstellar",
//                "A team of researchers travels through a wormhole in the universe in search of new worlds to ensure the survival of humanity.",
//                List.of(Genre.SCIENCE_FICTION, Genre.ACTION, Genre.DRAMA),
//                "M002",
//                2014,
//                "https://example.com/interstellar.jpg",
//                169,
//                List.of("Christopher Nolan"),
//                List.of("Jonathan Nolan", "Christopher Nolan"),
//                List.of("Matthew McConaughey", "Anne Hathaway", "Jessica Chastain"),
//                8.6
//        );
//
//        Movie wallE = new Movie(
//                "WALL·E",
//                "In the distant future, a small waste-collecting robot accidentally embarks on a journey into space—one that ultimately determines the fate of humanity.",
//                List.of(Genre.ANIMATION, Genre.ROMANCE, Genre.FAMILY),
//                "M003",
//                2008,
//                "https://example.com/walle.jpg",
//                98,
//                List.of("Andrew Stanton"),
//                List.of("Andrew Stanton", "Pete Docter"),
//                List.of("Ben Burtt", "Elissa Knight"),
//                8.4
//        );
//
//        Movie planetEarth = new Movie(
//                "Planet Earth",
//                "Emmy-winning, 11 episodes, five years in production—the most expensive nature documentary series ever commissioned by the BBC and the first to be filmed in high definition.",
//                List.of(Genre.DOCUMENTARY, Genre.FAMILY),
//                "M004",
//                2006,
//                "https://example.com/planet_earth.jpg",
//                550,
//                List.of("Alastair Fothergill"),
//                List.of("David Attenborough"),
//                List.of("David Attenborough"),
//                9.4
//        );
//
//        Movie hamilton = new Movie(
//                "Hamilton",
//                "A filmed version of the Broadway musical Hamilton by Lin-Manuel Miranda, telling the story of American founding father Alexander Hamilton—born and raised as an orphan in the Caribbean.",
//                List.of(Genre.MUSICAL, Genre.HISTORY),
//                "M005",
//                2020,
//                "https://example.com/hamilton.jpg",
//                160,
//                List.of("Thomas Kail"),
//                List.of("Lin-Manuel Miranda"),
//                List.of("Lin-Manuel Miranda", "Leslie Odom Jr."),
//                8.3
//        );
//
//        Movie saw = new Movie(
//                "Saw",
//                "Two strangers wake up in a room with no memory of how they got there, only to discover they are pawns in the deadly game of a notorious serial killer.",
//                List.of(Genre.HORROR, Genre.MYSTERY),
//                "M006",
//                2004,
//                "https://example.com/saw.jpg",
//                103,
//                List.of("James Wan"),
//                List.of("Leigh Whannell"),
//                List.of("Cary Elwes", "Leigh Whannell", "Danny Glover"),
//                7.6
//        );
//
//        Movie manitousShoe = new Movie(
//                "Manitou's Shoe",
//                "The story of two best friends, Apache chief Abahachi and cowboy Ranger, set in the Wild West.",
//                List.of(Genre.COMEDY),
//                "M007",
//                2001,
//                "https://example.com/manitous_shoe.jpg",
//                87,
//                List.of("Michael Herbig"),
//                List.of("Michael Herbig"),
//                List.of("Michael Herbig", "Christian Tramitz"),
//                6.9
//        );
//
//        Movie twilight = new Movie(
//                "Twilight",
//                "When Bella Swan moves to a small town in the Pacific Northwest, she falls in love with Edward Cullen, a mysterious classmate who turns out to be a 108-year-old vampire.",
//                List.of(Genre.DRAMA, Genre.FANTASY),
//                "M008",
//                2008,
//                "https://example.com/twilight.jpg",
//                122,
//                List.of("Catherine Hardwicke"),
//                List.of("Melissa Rosenberg"),
//                List.of("Kristen Stewart", "Robert Pattinson"),
//                5.3
//        );
//
//        movies = List.of(aSilentVoice, interstellar, wallE, planetEarth, hamilton, saw, manitousShoe, twilight);
//        return movies;
//    }
}
