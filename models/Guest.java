package models;

import java.time.LocalDate;

import enumerations.Gender;
import enumerations.UserType;


public class Guest extends User implements interfaces.Reservable {
    private double balance;
    private String address;
    private Gender gender;
    private RoomPreference roomPreferences;
    


    public Guest(String username, String password, LocalDate dob, String address, Gender gender, RoomType prefType, int prefFloor, boolean seaview , double price) throws Exception {
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
    public boolean setBalance(String username, String password, double balance) {
        if (this.login(username, password)) {
            this.balance = balance;
            return true;
        } else {
            return false;
        }

    }
    public String getAddress(String username, String password) {
        if (this.login(username, password)) {
            return address;
        } else {
            return null; 
        }
    }

    public boolean setAddress(String username, String password, String address) {
        if (this.login(username, password)) {
            this.address = address;
            return true;
        } else {
            return false;
        }
    }
    

    public Gender getGender(String username, String password) {
        if (this.login(username, password)) {
            return gender;
        } else {

            return null; // Return null to indicate access denied
        }

    }

    public boolean setGender(String username, String password, Gender gender) {
        if (this.login(username, password)) {
            this.gender = gender;
            return true;
        } else {
            return false;
        }
    }

    public RoomPreference getRoomPreferences(String username, String password) {
        if (this.login(username, password)) {
            return roomPreferences;
        } else {
            return null; // Return null to indicate access denied
        }
    }

    public boolean setRoomPreferences(String username, String password, RoomType prefType, int prefFloor, boolean seaview , double price) {
        if (this.login(username, password)) {
            this.roomPreferences = new RoomPreference(prefType, prefFloor, seaview, price);
            return true;
        } else {

            return false;
        }
    }



    @Override
    public boolean makeReservation(Room room) {
        if (room.isAvailable()) {
            Database.createAndAddReservation(this, room, LocalDate.now(), LocalDate.now().plusDays(1));
            room.setAvailable(false);
            return true;
        } 
        return false;
    }

    @Override
    public boolean cancelReservation(Reservation reservation) {
        if (reservation != null && reservation.getGuest().equals(this)) {
            reservation.cancel(); 
            return true;
        }
        return false;
    }

    @Override
    public java.util.ArrayList<Reservation> viewReservations() {
        java.util.ArrayList<Reservation> myReservations = new java.util.ArrayList<>();
        for (Reservation res : Database.getAllReservations()) {
            if (res.getGuest().equals(this)) myReservations.add(res);
        }
        return myReservations;
    }

    public java.util.ArrayList<Room> viewAvailableRooms() {
        java.util.ArrayList<Room> availableRooms = new java.util.ArrayList<>();
        for (Room r : Database.getAllRooms()) {
            if (r.isAvailable()) availableRooms.add(r);
        }
        return availableRooms;
    }

    public boolean checkoutAndPay(Reservation reservation, enumerations.PaymentMethod method) {
        if (reservation.getGuest().equals(this)) {
            Invoice invoice = Database.createAndAddInvoice(reservation);
            invoice.pay(method);
            return true;
        }
        return false;
    }
}



    
