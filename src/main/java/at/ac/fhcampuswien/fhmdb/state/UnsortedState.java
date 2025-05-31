package at.ac.fhcampuswien.fhmdb.state;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.List;

public class UnsortedState implements SortState {
    @Override
    public void sort(List<Movie> movies) {
        // Keine Sortierung → Listenreihenfolge bleibt wie sie ist
        // Absichtlich leer gelassen
    }
}
