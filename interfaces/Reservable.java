package interfaces;

public interface Reservable {
void makeReservation(Room room); 
void cancelReservation(Reservation reservation); 
void viewReservations();
}
