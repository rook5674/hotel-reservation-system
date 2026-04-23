package models;
import java.util.ArrayList;

import enumerations.AmenityType;
import enumerations.Gender;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.util.Scanner;

import java.time.LocalDate;


public class Database{
    private static ArrayList<Room> rooms = new ArrayList<Room>();
    private static ArrayList<Amenity> amenities = new ArrayList<Amenity>();
    private static ArrayList<Guest> guests = new ArrayList<Guest>();
    private static ArrayList<Staff> staff = new ArrayList<Staff>();
    private static ArrayList<RoomType> roomTypes = new ArrayList<RoomType>();
    private static ArrayList<Invoice> invoices = new ArrayList<Invoice>();
    private static ArrayList<Reservation> reservations = new ArrayList<Reservation>();
    private Database(){
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");

    }

    //adding objects





    public static void addRoom (Room room){
        rooms.add(room);
    }
    public static void addAmenity (Amenity amenity){
        amenities.add(amenity);
    }
    public static void addGuest (Guest guest){
        guests.add(guest);
    }

    public static void addStaff (Staff staffMember){
        staff.add(staffMember);
    }
    public static void addRoomType (RoomType roomType){
        roomTypes.add(roomType);
    }
    public static void addInvoice (Invoice invoice){
        invoices.add(invoice);
    }
    public static void addReservation (Reservation reservation){
        reservations.add(reservation);
    }


    //methods for removing objects from the database

    public static boolean removeRoom (int roomNumber){
        for (Room room : rooms){
            if (room.getRoomNumber() == roomNumber){
                rooms.remove(room);
                return true;
            }
        }
        return false;
    }
    public static boolean removeAmenity (String amenityName){
        for (Amenity amenity : amenities){
            if (amenity.getName().equals(amenityName)){
                amenities.remove(amenity);
                return true;
            }
        }
        return false;
    }
    public static boolean removeGuest (String guestName){
        for (Guest guest : guests){
            if (guest.getUserName().equals(guestName)){
                guests.remove(guest);
                return true;
            }
        }
        return false;
    }
    public static boolean removeStaff (String staffName){
        for (Staff staffMember : staff){
            if (staffMember.getUserName().equals(staffName)){
                staff.remove(staffMember);
                return true;
            }
        }
        return false;
    }
    public static boolean removeRoomType (String roomTypeName){
        for (RoomType roomType : roomTypes){
            if (roomType.getTypeName().equals(roomTypeName)){
                roomTypes.remove(roomType);
                return true;
            }
        }
        return false;
    }
    public static boolean removeInvoice (int invoiceId){
        for (Invoice invoice : invoices){
            if (invoice.getInvoiceId() == invoiceId){
                invoices.remove(invoice);
                return true;
            }
        }
        return false;
    }
    public static boolean removeReservation (int reservationId){
        for (Reservation reservation : reservations){
            if (reservation.getReservationId() == reservationId){
                reservations.remove(reservation);
                return true;
            }
        }
        return false;
    }

    



    public static Room getRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null; 
    }

    public static Guest getGuest(String username , String password) {
        for (Guest guest : guests) {
            if (guest.getUserName().equals(username) && guest.getPassword().equals(password)) {
                return guest;
                //return loadGuestFromTextFile(username, password);
            }
        }
        return null;
    }

    public static Staff getStaffByUsername(String username) {
        for (Staff staffMember : staff) {
            if (staffMember.getUserName().equals(username)) {
                return staffMember;
            }
        }
        return null;
    }

    public static RoomType getRoomTypeByName(String typeName) {
        for (RoomType roomType : roomTypes) {
            if (roomType.getTypeName().equals(typeName)) {
                return roomType;
            }
        }
        return null;
    }

    public static Amenity getAmenityByName(String amenityName) {
        for (Amenity amenity : amenities) {
            if (amenity.getName().equalsIgnoreCase(amenityName)) {
                return amenity;
            }
        }
        return null;
    }

    public static Reservation getReservationById(int reservationId) {
        for (Reservation res : reservations) {
            if (res.getReservationId() == reservationId) {
                return res;
            }
        }
        return null;
    }

    public static Invoice getInvoiceById(int invoiceId) {
        for (Invoice inv : invoices) {
            if (inv.getInvoiceId() == invoiceId) {
                return inv;
            }
        }
        return null;
    }


    // methods for creating objects and adding them to the database 



    public static Guest registerNewGuest(String username, String password, LocalDate dateOfBirth, double balance, String address, Gender gender, RoomType prefType, int prefFloor, boolean seaview , double price) {
        Guest newGuest = new Guest(username, password, dateOfBirth, address, gender, prefType, prefFloor, seaview, price);
        guests.add(newGuest);
        SaveGuestToTextFile(newGuest);
        return newGuest;
    }


    public static Admin registerNewAdmin(String username, String password, LocalDate dateOfBirth, Schedule workingHours, int StartTime, int EndTime) throws Exception {
        Admin newAdmin = new Admin(username, password, dateOfBirth, StartTime, EndTime);
        staff.add(newAdmin);
        return newAdmin;
    }


    public static Receptionist registerNewReceptionist(String username, String password, LocalDate dateOfBirth,  int StartTime, int EndTime) throws Exception  {
        // Notice we hardcode Role.RECEPTIONIST here
        Receptionist newReceptionist = new Receptionist(username, password, dateOfBirth, StartTime, EndTime);
        staff.add(newReceptionist);
        return newReceptionist;
    }


    public static Room createAndAddRoom(int roomNumber, int floor, RoomType type) throws Exception {
        Room newRoom = new Room(roomNumber, floor, type);
        rooms.add(newRoom);
        return newRoom;
    }

    public static RoomType createAndAddRoomType(String typeName , double basePrice, int id, int maxOccupancy ) throws Exception {
        RoomType newRoomType = new RoomType(id, typeName,   maxOccupancy, basePrice);
        roomTypes.add(newRoomType);
        return newRoomType;
    }

    public static Amenity createAndAddAmenity(int id,String name , AmenityType type) throws Exception {
        Amenity newAmenity = new Amenity(id, name, type);
        amenities.add(newAmenity);
        return newAmenity;
    }

    public static Reservation createAndAddReservation(Guest guest, Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        Reservation newReservation = new Reservation(guest, room, checkInDate, checkOutDate);
        reservations.add(newReservation);
        return newReservation;
    }

    public static Invoice createAndAddInvoice(Reservation reservation) {
        Invoice newInvoice = new Invoice(reservation);
        invoices.add(newInvoice);
        return newInvoice;
    }

    public static void SaveGuestToTextFile(Guest guest) {
    
    String seperator = "--------------------------------" ;
    
    
   if (guestExistsInTextFile(guest.getUserName(), guest.getPassword()))
        {
            System.out.println("Guest already exists in the text file. Skipping save.");
             // Skip saving if the guest already exists in the text file
}
    else    
   {
        try (PrintWriter writer = new PrintWriter (new FileWriter("hotel-reservation-system/IOfiles/guests.txt", true))) {
            writer.println(guest.getUserName());
            writer.println(guest.getPassword());
            writer.println(guest.getDateOfBirth().toString());
            writer.println(guest.getAddress(guest.getUserName(), guest.getPassword()));
            writer.println(guest.getGender(guest.getUserName(), guest.getPassword()).name());
            writer.println(guest.getRoomPreferences(guest.getUserName(), guest.getPassword()));
            writer.println(guest.getBalance(guest.getUserName(), guest.getPassword())); // Separator between guests
            writer.println( seperator );

        } catch (IOException e) {
            System.out.println("An error occurred while saving guest information: " + e.getMessage());
        }

        }


    }

    public static Gender getvalueOfGenderusingString(String name) {

    if (name == "MALE")
        return Gender.MALE;
    else if (name == "FEMALE")
    {
        return Gender.FEMALE;
    }
    else
        return null;    
}

    public static Guest loadGuestFromTextFile(String username, String password) {
       
        File file = new File("hotel-reservation-system/IOfiles/guests.txt");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String fileUsername = scanner.nextLine();
                String filePassword = scanner.nextLine();
                if (fileUsername.equals(username) && filePassword.equals(password)) {
                    LocalDate dateOfBirth = LocalDate.parse(scanner.nextLine());
                    String fileAddress = scanner.nextLine();
                    Gender fileGender = Gender.valueOf(scanner.nextLine());
                    RoomType fileRoomType = Database.getRoomTypeByName(scanner.nextLine());
                    int fileRoomID = Integer.parseInt(scanner.nextLine());
                    int fileMaxOccupancy = Integer.parseInt(scanner.nextLine());
                    double filePrice = Double.parseDouble(scanner.nextLine());
                    String fileAmenities = scanner.nextLine(); // This line is currently not used, but you can implement parsing if needed
                    int filePreferredFloornumber = Integer.parseInt(scanner.nextLine());
                    boolean fileSeaview = Boolean.parseBoolean(scanner.nextLine());
                    double fileBalance = Double.parseDouble(scanner.nextLine());
                    scanner.nextLine(); // Skip the separator line

                    Guest guest = new Guest(fileUsername, filePassword, dateOfBirth, fileAddress, Database.getvalueOfGenderusingString(fileGender.name()), fileRoomType, filePreferredFloornumber, fileSeaview, filePrice);
                    guest.setAddress(fileUsername, filePassword, fileAddress);
                    guest.setGender(fileUsername, filePassword, Database.getvalueOfGenderusingString(fileGender.name()));
                    guest.setRoomPreferences( fileRoomType, filePreferredFloornumber, fileSeaview, filePrice);
                    guest.setBalance(fileUsername, filePassword, fileBalance);

                    return guest;
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred while loading guest information: " + e.getMessage());
        }

        return null; // Return null if guest not found
    }

    public static boolean guestExistsInTextFile(String username, String password) {
        File file = new File("hotel-reservation-system/IOfiles/guests.txt");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String fileUsername = scanner.nextLine();
                String filePassword = scanner.nextLine();
                if (fileUsername.equals(username) && filePassword.equals(password)) {
                    return true; // Guest exists in the text file
                }
                // Skip the rest of the guest information
                for (int i = 0; i < 7; i++) {
                    if (scanner.hasNextLine()) {
                        scanner.nextLine();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while checking guest existence: " + e.getMessage());
        }
        return false; // Guest does not exist in the text file
    }






}