package at.ac.fhcampuswien.fhmdb.state;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.Collections;
import java.util.List;

public class AscSortState implements SortState {
    @Override
    public void sort(List<Movie> movies) {
        Collections.sort(movies); // Movie implementiert Comparable → alphabetisch nach Titel
    }
}
