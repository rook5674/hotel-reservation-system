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

    public void viewAllGuests() {
        System.out.println("--- All Registered Guests ---");
        for (Guest g : Database.getAllGuests()) {
            System.out.println(g.getUserName() + " | Balance: $" + g.getBalance(g.getUserName(), g.getPassword()));
        }
    }

    public void viewAllRooms() {
        System.out.println("--- All Rooms ---");
        for (Room r : Database.getAllRooms()) {
            System.out.println(r.toString());
        }
    }

    public void viewAllReservations() {
        System.out.println("--- All Reservations ---");
        for (Reservation res : Database.getAllReservations()) {
            System.out.println(res.toString());
        }
    }


}