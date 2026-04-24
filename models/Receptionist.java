package models;
import enumerations.StaffRole;

public class Receptionist extends Staff {
    public Receptionist(String username, String password, java.time.LocalDate dateOfBirth, int StartTime, int EndTime ) throws Exception {   
        super(username, password, dateOfBirth, StartTime, EndTime, StaffRole.Receptionist);

    }

    public boolean checkInGuest(Reservation reservation) {
        if (reservation.getStatus() == enumerations.ReservationStatus.CONFIRMED ) {
            reservation.getRoom().setAvailable(false);
            return true;
        }
        return false;
    }

    public boolean checkOutGuest(Reservation reservation) {
        if (!reservation.getRoom().isAvailable()) {
            reservation.getRoom().setAvailable(true);
            reservation.complete(); 
            return true;
        }
        return false;
    }
}