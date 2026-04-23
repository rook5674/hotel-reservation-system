package interfaces;

public interface Bookable {
    boolean isAvailable();
    void book();
    void release();
}
