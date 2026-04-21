package models;
import enumerations.StaffRole;

public class Receptionist extends Staff {
    public Receptionist(String username, String password, java.time.LocalDate dateOfBirth, int StartTime, int EndTime ) throws Exception {   
        super(username, password, dateOfBirth, StartTime, EndTime, StaffRole.Receptionist);

    }

    public void checkInGuest(Guest guest , Room room) {
        System.out.println("Checking in guest: " + guest.getUserName());
        if (room.isAvailable()) {
            System.out.println("Room assigned: " + room.getRoomNumber() + " to guest: " + guest.getUserName());
            room.setAvailable(false); // Mark the room as occupied
        } 
        else {
            System.out.println("Room " + room.getRoomNumber() + " is not available.");
            // Exit the method if the room is not available
        }
        
    }

    public void checkOutGuest(Guest guest, Room room) {
        
        System.out.println("Checking out guest: " + guest.getUserName());
        if (room.isAvailable()) {
            System.out.println("Room released: " + room.getRoomNumber() + " from guest: " + guest.getUserName());
            room.setAvailable(true); // Mark the room as available
        } else {
            System.out.println("Room " + room.getRoomNumber() + " is not currently occupied.");
        }
    }
}