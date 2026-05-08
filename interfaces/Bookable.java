package interfaces;

public interface Bookable {
    boolean isAvailable();
    boolean book();
    boolean release();
}
