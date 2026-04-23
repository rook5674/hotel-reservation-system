package models;

import enumerations.*;
import exceptions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("MILESTONE 1 TESTING STARTED");

        // ---------------------------------------------------------
        // TEST 1: Database & Dummy Data Initialization
        // ---------------------------------------------------------
        System.out.println("--- [TEST 1] INITIALIZING DATABASE ---");
        Database.initializeDummyData(); // Tests Database.java and all createAndAdd methods

        try {
            // Fetch users from database to use in tests
            Admin admin = (Admin) Database.getStaffByUsername("admin_boss");
            Receptionist receptionist = (Receptionist) Database.getStaffByUsername("rec_morning");
            Guest guest = Database.getGuestByUsername("mohamed_ali");
            
            System.out.println("\n--- [TEST 2] AUTHENTICATION & USER CLASSES ---");
            // Tests User.java, Authenticatable interface, and Schedule.java
            System.out.println("Admin Login test (Correct): " + admin.login("admin_boss", "admin123"));
            System.out.println("Admin Login test (Wrong): " + admin.login("admin_boss", "wrongpass"));
            System.out.println("Receptionist Shift: " + receptionist.getWorkingHours().toString());
            System.out.println("Guest Preferences: " + guest.getRoomPreferences("mohamed_ali", "password123"));

            // testing Admin Functionality (Manageable Interface, CRUD)
            System.out.println("\n--- [TEST 3] ADMIN CRUD OPERATIONS ---");
            // Tests Admin.java, Manageable<Room>, Amenity, RoomType
            
            // Create
            admin.createAmenity(99, "Pool Access", AmenityType.WIFI); 
            admin.createRoomType("Ultra Villa", 1500.0, 999, 10);
            RoomType villaType = admin.readRoomType("Ultra Villa");
            Room newRoom = new Room(999, 9, villaType);
            admin.create(newRoom);
            System.out.println("Admin created new room: " + admin.read(999).getRoomNumber());

            // Update
            admin.updateAmenityName("Pool Access", "Private Pool Access");
            admin.updateRoomTypePrice("Ultra Villa", 2000.0);
            System.out.println("Admin updated Villa price to: $" + admin.readRoomType("Ultra Villa").getBasePricePerNight());

            // Delete
            admin.delete(999);
            admin.deleteRoomType("Ultra Villa");
            admin.deleteAmenity("Private Pool Access");
            System.out.println("Admin successfully deleted temporary test data.");

            // Account Recovery Tests
            System.out.println("Forgotten Username Lookup (DOB 1999-01-15): " + admin.findForgottenUsernameByDOB(LocalDate.of(1999, 1, 15)));
            admin.resetUserPassword("mohamed_ali", "newpassword123");
            System.out.println("Admin forced password reset for mohamed_ali.");

            // tetsing Guest Operations & Budgetable Interface
    
            System.out.println("\n--- [TEST 4] GUEST RESERVATIONS ---");
            // Tests Guest.java, Reservable interface, Budgetable interface
            
            ArrayList<Room> availableRooms = guest.viewAvailableRooms();
            Room targetRoom = availableRooms.get(0); // Grab the first available room
            
            // Test Budgetable interface on RoomType
            double guestBudget = 200.0;
            System.out.println("Is " + targetRoom.getRoomType().getTypeName() + " affordable for $" + guestBudget + "? " 
                + targetRoom.getRoomType().isAffordable(guestBudget));

            // Make Reservation
            boolean resSuccess = guest.makeReservation(targetRoom);
            System.out.println("Guest booking room " + targetRoom.getRoomNumber() + ": " + (resSuccess ? "SUCCESS" : "FAILED"));
            
            // Fetch the newly created reservation
            ArrayList<Reservation> guestReservations = guest.viewReservations();
            Reservation activeRes = guestReservations.get(0);
            System.out.println("Reservation Status: " + activeRes.getStatus());



            // testing Receptionist Operations
            System.out.println("testing RECEPTIONIST CHECK-IN / CHECK-OUT ");
            // Tests Receptionist.java, Reservation state changes
            
            // System/Admin must confirm before check-in
            activeRes.confirm(); 
            System.out.println("Admin confirmed reservation. Status: " + activeRes.getStatus());

            boolean checkIn = receptionist.checkInGuest(activeRes);
            System.out.println("Receptionist checked guest in: " + (checkIn ? "SUCCESS" : "FAILED"));
            System.out.println("Room " + targetRoom.getRoomNumber() + " is available? " + targetRoom.isAvailable());



            // testing Invoicing, Payments & Exceptions

            System.out.println(" testing INVOICING & EXCEPTIONS ");
            // Tests Invoice.java, Payable interface, Exception handling
            
            receptionist.checkOutGuest(activeRes);
            System.out.println("Receptionist checked guest out. Reservation Status: " + activeRes.getStatus());

            // Guest pays the invoice
            boolean paymentSuccess = guest.checkoutAndPay(activeRes, PaymentMethod.CREDIT_CARD);
            System.out.println("Guest checkout and payment: " + (paymentSuccess ? "SUCCESS" : "FAILED"));

            // Force an Exception: Try to pay the invoice again!
            System.out.println("testing EXCEPTION HANDLING SIMULATION ");
            try {
                System.out.println("Attempting to double-charge the guest...");
                Invoice paidInvoice = Database.getInvoiceById(2); // ID 2 because dummy data made ID 1
                if(paidInvoice != null) {
                    paidInvoice.pay(PaymentMethod.CASH); 
                }
            } catch (InvalidPaymentException e) {
                System.out.println("EXPECTED ERROR CAUGHT: " + e.getMessage());
            }

            // Force Exception: Try to double book a room
            try {
                System.out.println("Attempting to double-book an occupied room...");
                Room occupiedRoom = Database.getRoomByNumber(401); // Sarah's penthouse from dummy data
                Reservation badRes = new Reservation(guest, occupiedRoom, LocalDate.now(), LocalDate.now().plusDays(2));
            } catch (RoomNotAvailableException e) {
                System.out.println("EXPECTED ERROR CAUGHT: " + e.getMessage());
            }

            System.out.println("Milestone 1 testing done");

        } catch (Exception e) {
            System.err.println("  ERROR ");
            e.printStackTrace();
        }
    }
}