package interfaces;
import models.Room;

import java.util.ArrayList;

import models.Reservation;

public interface Reservable {
    boolean makeReservation(Room room); 
    boolean cancelReservation(Reservation reservation); 
    ArrayList<Reservation> viewReservations();
}
