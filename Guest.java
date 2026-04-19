
import java.time.LocalDate;


public class Guest extends User{
    private double balance;
    private String address;
    private Gender gender;
    private RoomPreference roomPreferences;
    


    public Guest(String username, String password, LocalDate dob, String address, Gender gender, RoomType prefType, int prefFloor, boolean seaview , double price) {
        super(username, password, dob);
        setRole(UserType.GUEST);     // Set the user type to GUEST
        this.balance = 0.0; // Initialize balance to 0.0 for new guests
        this.address = address; 
        this.gender = gender; 
        this.roomPreferences = new RoomPreference(prefType, prefFloor, seaview, price);
        // we cant add to the database in the constructor
        }

    public double getBalance(String username, String password) {
        
        if (this.login(username, password)) {
            return balance;
            
        } 
        else {
            System.out.println("Access denied. Invalid username or password.");
            return -1; // Return -1 to indicate access denied
        }
    }
    public void setBalance(String username, String password, double balance) {
        if (this.login(username, password)) {
            this.balance = balance;
        } else {
            System.out.println("Access denied. Invalid username or password.");
        }

    }
    public String getAddress(String username, String password) {
        if (this.login(username, password)) {
            return address;
        } else {
            System.out.println("Access denied. Invalid username or password.");
            return null; // Return null to indicate access denied
        }
    }

    public void setAddress(String username, String password, String address) {
        if (this.login(username, password)) {
            this.address = address;
        } else {
            System.out.println("Access denied. Invalid username or password.");
        }
    }

    public Gender getGender(String username, String password) {
        if (this.login(username, password)) {
            return gender;
        } else {
            System.out.println("Access denied. Invalid username or password.");     

            return null; // Return null to indicate access denied
        }

    }

    public void setGender(String username, String password, Gender gender) {
        if (this.login(username, password)) {
            this.gender = gender;
        } else {
            System.out.println("Access denied. Invalid username or password.");
        }
    }

    public RoomPreference getRoomPreferences(String username, String password) {
        if (this.login(username, password)) {
            return roomPreferences;
        } else {
            System.out.println("Access denied. Invalid username or password.");
            return null; // Return null to indicate access denied
        }
    }

    public void setRoomPreferences(String username, String password, RoomType prefType, int prefFloor, boolean seaview , double price) {
        if (this.login(username, password)) {
            this.roomPreferences = new RoomPreference(prefType, prefFloor, seaview, price);
        } else {
            System.out.println("Access denied. Invalid username or password.");
        }
    }

    public void makeReservation(Room room, LocalDate checkInDate, LocalDate checkOutDate, String username, String password) {
        if (this.login(username, password)) {
            System.out.println("Making reservation for " + username + " at " + room.getRoomNumber() + " from " + checkInDate + " to " + checkOutDate);
            if (room.isAvailable()) {
                    room.setAvailable(false); // Mark the room as unavailable
                    System.out.println("Reservation successful for room " + room.getRoomNumber());
               
            } else {
                System.out.println("Sorry, room " + room.getRoomNumber() + " is not available for the selected dates.");
            }
        } else {
            System.out.println("Access denied. Invalid username or password.");
        }
    }
}

    
