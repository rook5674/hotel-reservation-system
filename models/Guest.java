package models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import enumerations.Gender;
import enumerations.PaymentMethod;
import enumerations.ReservationStatus;
import enumerations.UserType;

public class Guest extends User implements interfaces.Reservable {
    private double balance;
    private String address;
    private Gender gender;
    private RoomPreference roomPreferences;

    public Guest(String username, String password, LocalDate dob, String address, Gender gender, RoomType prefType, int prefFloor, boolean seaview, double price) throws Exception {
        super(username, password, dob);
        setRole(UserType.GUEST);

        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address cannot be empty.");
        }
        if (gender == null) {
            throw new IllegalArgumentException("Gender cannot be null.");
        }
        if (prefType == null) {
            throw new IllegalArgumentException("Preferred room type cannot be null.");
        }

        this.balance = 0.0;
        this.address = address;
        this.gender = gender;
        this.roomPreferences = new RoomPreference(prefType, prefFloor, seaview, price);
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative.");
        }
        this.balance = balance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address cannot be empty.");
        }
        this.address = address;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        if (gender == null) {
            throw new IllegalArgumentException("Gender cannot be null.");
        }
        this.gender = gender;
    }

    public RoomPreference getRoomPreferences() {
        return roomPreferences;
    }

    public void setRoomPreferences(RoomType prefType, int prefFloor, boolean seaview, double price) {
        if (prefType == null) {
            throw new IllegalArgumentException("Preferred room type cannot be null.");
        }
        this.roomPreferences = new RoomPreference(prefType, prefFloor, seaview, price);
    }

    @Override
    public boolean makeReservation(Room room, LocalDate checkIn, LocalDate checkOut) {
        if (room == null || !room.isAvailable()) {
            return false;
        }

        Reservation reservation = Database.createAndAddReservation(this, room, checkIn, checkOut);
        reservation.confirm();
        room.setAvailable(false);
        return true;
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
    public ArrayList<Reservation> viewReservations() {
        ArrayList<Reservation> myReservations = new ArrayList<>();
        for (Reservation res : Database.getAllReservations()) {
            if (res.getGuest().equals(this)) {
                myReservations.add(res);
            }
        }
        return myReservations;
    }

    public ArrayList<Room> viewAvailableRooms() {
        ArrayList<Room> availableRooms = new ArrayList<>();
        for (Room r : Database.getAllRooms()) {
            if (r.isAvailable()) {
                availableRooms.add(r);
            }
        }
        return availableRooms;
    }

    public boolean checkoutAndPay(Reservation reservation, PaymentMethod method) {
        if (method == null) {
            throw new IllegalArgumentException("Payment method cannot be null.");
        }

        ArrayList<PaymentMethod> methods = new ArrayList<>();
        methods.add(method);
        return checkoutAndPay(reservation, methods);
    }

    public boolean checkoutAndPay(Reservation reservation, List<PaymentMethod> methods) {
        if (reservation == null || methods == null || methods.isEmpty()) {
            return false;
        }

        if (!reservation.getGuest().equals(this)) {
            return false;
        }

        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            return false;
        }

        double cost = reservation.calculateTotalCost();

        if (balance < cost) {
            return false;
        }

        Invoice invoice = Database.createAndAddInvoice(reservation);
        invoice.pay(methods);
        balance -= cost;
        return true;
    }
}
