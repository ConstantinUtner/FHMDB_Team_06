package at.ac.fhcampuswien.fhmdb.ui;

@FunctionalInterface
public interface ClickEventHandler<T> {
    boolean onClick(T t);
}