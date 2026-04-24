package models;
import enumerations.StaffRole;
import enumerations.UserType;

public class Staff extends User {

    Schedule workinghours ; 
    protected final StaffRole role;

    
    public Staff(String username, String password, java.time.LocalDate dateOfBirth, int StartTime, int EndTime, StaffRole role) throws Exception {
        super(username, password, dateOfBirth);
        super.setRole(UserType.Staff);
        this.workinghours = new Schedule(StartTime, EndTime);
        this.role = role;

    }

    public Schedule getWorkingHours() {
        return workinghours;
    }

    public void setWorkingHours(Schedule workinghours) {
        this.workinghours = workinghours;
    }

    public StaffRole getStaffRole() {
        return role;
    }

    public java.util.ArrayList<Guest> viewAllGuests() {
        return Database.getAllGuests();
    }

    public java.util.ArrayList<Room> viewAllRooms() {
        return Database.getAllRooms();
    }

    public java.util.ArrayList<Reservation> viewAllReservations() {
        return Database.getAllReservations();
    }


}