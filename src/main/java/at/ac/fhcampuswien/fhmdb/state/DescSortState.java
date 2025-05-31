package at.ac.fhcampuswien.fhmdb.state;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.Collections;
import java.util.List;

public class DescSortState implements SortState{
    @Override
    public void sort(List<Movie> movies) {
        Collections.sort(movies, Collections.reverseOrder());
    }
}
