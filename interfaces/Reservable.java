package interfaces;

import java.time.LocalDate;
import java.util.ArrayList;

import models.Reservation;
import models.Room;

public interface Reservable {
    boolean makeReservation(Room room, LocalDate checkIn, LocalDate checkOut);
    boolean cancelReservation(Reservation reservation);
    ArrayList<Reservation> viewReservations();
}
