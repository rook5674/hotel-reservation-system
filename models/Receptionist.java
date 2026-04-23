package models;
import enumerations.StaffRole;

public class Receptionist extends Staff {
    public Receptionist(String username, String password, java.time.LocalDate dateOfBirth, int StartTime, int EndTime ) throws Exception {   
        super(username, password, dateOfBirth, StartTime, EndTime, StaffRole.Receptionist);

    }

    public boolean checkInGuest(Guest guest, Room room) {
        if (room.isAvailable()) {
            room.setAvailable(false); 
            return true;
        }
        return false;
    }

    public boolean checkOutGuest(Guest guest, Room room) {
        if (!room.isAvailable()) {
            room.setAvailable(true); 
            return true;
        }
        return false;
    }
}