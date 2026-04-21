package interfaces;
import models.Room;
import models.Reservation;

public interface Reservable {
void makeReservation(Room room); 
void cancelReservation(Reservation reservation); 
void viewReservations();
}
