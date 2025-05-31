package at.ac.fhcampuswien.fhmdb.state;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.List;

public class SortContext {
    private SortState currentState;

    public SortContext() {
        currentState = new UnsortedState(); // Startzustand
    }

    public void setState(SortState state) {
        this.currentState = state;
    }

    public void applySort(List<Movie> movies) {
        if (currentState != null) {
            currentState.sort(movies);
        }
    }
}
