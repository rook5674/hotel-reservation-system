package interfaces;

public interface Budgetable {
    double getPrice();
    boolean isAffordable(double budget);
}
