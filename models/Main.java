package models;

import enumerations.AmenityType;
import enumerations.PaymentMethod;
import exceptions.InvalidPaymentException;
import exceptions.RoomNotAvailableException;

import java.time.LocalDate;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("MILESTONE 1 SMOKE TEST STARTED");
        Database.initializeDummyData();

        try {
            Admin admin = (Admin) Database.getStaffByUsername("admin");
            Receptionist receptionist = (Receptionist) Database.getStaffByUsername("receptionist1");
            Guest guest = Database.getGuestByUsername("ahmed");

            if (admin == null || receptionist == null || guest == null) {
                throw new IllegalStateException("Required dummy users were not created.");
            }

            System.out.println("Admin Login test: " + admin.login("admin", "admin123"));
            System.out.println("Receptionist Shift: " + receptionist.getWorkingHours());
            System.out.println("Guest Preferences: " + guest.getRoomPreferences());

            admin.createAmenity(99, "Pool Access", AmenityType.WIFI);
            admin.createRoomType("Ultra Villa", 1500.0, 999, 10);
            RoomType villaType = admin.readRoomType("Ultra Villa");
            Room newRoom = new Room(999, 9, villaType);
            admin.create(newRoom);
            admin.updateAmenityName("Pool Access", "Private Pool Access");
            admin.updateRoomTypePrice("Ultra Villa", 2000.0);
            admin.updateRoomAvailability(999, false);

            if (admin.read(999).isAvailable()) {
                throw new IllegalStateException("Admin updateRoomAvailability failed.");
            }

            admin.delete(999);
            admin.deleteRoomType("Ultra Villa");
            admin.deleteAmenity("Private Pool Access");
            admin.resetUserPassword("ahmed", "newpassword123");

            Guest payingGuest = Database.getGuestByUsername("seif");
            Room targetRoom = Database.getRoomByNumber(102);
            double beforeBalance = payingGuest.getBalance();
            boolean resSuccess = payingGuest.makeReservation(targetRoom, LocalDate.now().plusDays(2), LocalDate.now().plusDays(4));

            if (!resSuccess) {
                throw new IllegalStateException("Guest reservation failed.");
            }

            ArrayList<Reservation> reservations = payingGuest.viewReservations();
            Reservation activeRes = reservations.get(reservations.size() - 1);
            boolean paymentSuccess = payingGuest.checkoutAndPay(activeRes, PaymentMethod.CREDIT_CARD);

            if (!paymentSuccess) {
                throw new IllegalStateException("Guest checkout payment failed.");
            }

            if (payingGuest.getBalance() >= beforeBalance) {
                throw new IllegalStateException("Guest balance was not deducted.");
            }

            if (!targetRoom.isAvailable()) {
                throw new IllegalStateException("Room was not released after checkout.");
            }

            try {
                Invoice paidInvoice = Database.getAllInvoices().get(Database.getAllInvoices().size() - 1);
                paidInvoice.pay(PaymentMethod.CASH);
                throw new IllegalStateException("Double payment should have failed.");
            } catch (InvalidPaymentException expected) {
                System.out.println("Expected double-payment error caught: " + expected.getMessage());
            }

            try {
                Room occupiedRoom = Database.getRoomByNumber(401);
                new Reservation(guest, occupiedRoom, LocalDate.now(), LocalDate.now().plusDays(2));
                throw new IllegalStateException("Double booking should have failed.");
            } catch (RoomNotAvailableException expected) {
                System.out.println("Expected double-booking error caught: " + expected.getMessage());
            }

            System.out.println("MILESTONE 1 SMOKE TEST PASSED");

        } catch (Exception e) {
            System.err.println("SMOKE TEST FAILED");
            e.printStackTrace();
        }
    }
}
