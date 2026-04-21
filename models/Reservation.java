package models;
import exceptions.RoomNotAvailableException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
// --- Reservation.java ---

import enumerations.ReservationStatus;

public class Reservation {

    private static int idCounter = 1;
    private RoomType roomType;
    private int reservationId;
    private Guest guest;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private ReservationStatus status;

    public Reservation(Guest guest, Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        validateGuest(guest);
        validateRoom(room);
        validateDates(checkInDate, checkOutDate);

        this.reservationId = idCounter++;
        this.guest = guest;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = ReservationStatus.PENDING;
        
    }

    // --- Validation ---

    private void validateGuest(Guest guest) {
        if (guest == null) {
            throw new IllegalArgumentException("Guest cannot be null.");
        }
    }

    private void validateRoom(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null.");
        }
        if (!room.isAvailable()) {
            throw new RoomNotAvailableException("Room " + room.getRoomNumber() + " is not available.");
        }
    }

    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Check-in and check-out dates cannot be null.");
        }
        if (!checkIn.isBefore(checkOut)) {
            throw new IllegalArgumentException("Check-in date must be before check-out date.");
        }
        if (checkIn.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past.");
        }
    }

    // --- Business Logic ---

    public long getNumberOfNights() {
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    public double calculateTotalCost() {
        return getNumberOfNights() *  roomType.getBasePricePerNight();
    }

    public void confirm() {
        if (status != ReservationStatus.PENDING) {
            throw new IllegalStateException("Only PENDING reservations can be confirmed. Current status: " + status);
        }
        this.status = ReservationStatus.CONFIRMED;
    }

    public void cancel() {
        if (status == ReservationStatus.COMPLETED || status == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel a reservation with status: " + status);
        }
        this.status = ReservationStatus.CANCELLED;
        this.room.setAvailable(true);
    }

    public void complete() {
        if (status != ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("Only CONFIRMED reservations can be completed. Current status: " + status);
        }
        this.status = ReservationStatus.COMPLETED;
    }

    // --- Getters & Setters ---

    public int getReservationId() { return reservationId; }

    public Guest getGuest() { return guest; }

    public Room getRoom() { return room; }

    public LocalDate getCheckInDate() { return checkInDate; }

    public void setCheckInDate(LocalDate checkInDate) {
        validateDates(checkInDate, this.checkOutDate);
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() { return checkOutDate; }

    public void setCheckOutDate(LocalDate checkOutDate) {
        validateDates(this.checkInDate, checkOutDate);
        this.checkOutDate = checkOutDate;
    }

    public ReservationStatus getStatus() { return status; }

    public void setStatus(ReservationStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + reservationId +
                ", guest=" + guest.getUserName() +
                ", room=" + room.getRoomNumber() +
                ", checkIn=" + checkInDate +
                ", checkOut=" + checkOutDate +
                ", nights=" + getNumberOfNights() +
                ", totalCost=" + calculateTotalCost() +
                ", status=" + status +
                '}';
    }

}
