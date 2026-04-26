package models;
import java.util.ArrayList;

import enumerations.AmenityType;
import enumerations.Gender;

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

    // search methods for getting objects from names or ids



    public static Room getRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null; 
    }

    public static Guest getGuestByUsername(String username) {
        for (Guest guest : guests) {
            if (guest.getUserName().equals(username)) {
                return guest;
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
            if (roomType.getTypeName().equalsIgnoreCase(typeName)) {
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



    public static Guest registerNewGuest(String username, String password, LocalDate dateOfBirth, double balance, String address, Gender gender, RoomType prefType, int prefFloor, boolean seaview , double price) throws Exception {
        if (getGuestByUsername(username) != null || getStaffByUsername(username) != null) {
            throw new Exception("Error: Username '" + username + "' is already taken.");
        }
        Guest newGuest = new Guest(username, password, dateOfBirth, address, gender, prefType, prefFloor, seaview , price);
        guests.add(newGuest);
        return newGuest;
    }


    public static Admin registerNewAdmin(String username, String password, LocalDate dateOfBirth, int StartTime, int EndTime) throws Exception {
        if (getGuestByUsername(username) != null || getStaffByUsername(username) != null) {
            throw new Exception("Error: Username '" + username + "' is already taken.");
        }
        Admin newAdmin = new Admin(username, password, dateOfBirth, StartTime, EndTime);
        staff.add(newAdmin);
        return newAdmin;
    }


    public static Receptionist registerNewReceptionist(String username, String password, LocalDate dateOfBirth,  int StartTime, int EndTime) throws Exception  {
        if (getGuestByUsername(username) != null || getStaffByUsername(username) != null) {
            throw new Exception("Error: Username '" + username + "' is already taken.");
        }
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


    public static ArrayList<Guest> getAllGuests() { return guests; }
    public static ArrayList<Room> getAllRooms() { return rooms; }
    public static ArrayList<Reservation> getAllReservations() { return reservations; }
    public static ArrayList<RoomType> getAllRoomTypes() { return roomTypes; }
    public static ArrayList<Amenity> getAllAmenities() { return amenities; }
    public static java.util.ArrayList<Staff> getAllStaff() { return staff; }



    public static void initializeDummyData() {
        try {
            System.out.println("Initializing Database with extensive dummy data...");

            Amenity wifi = createAndAddAmenity(1, "High-Speed WiFi", enumerations.AmenityType.WIFI);
            Amenity tv = createAndAddAmenity(2, "4K Smart TV", enumerations.AmenityType.TV);
            Amenity ac = createAndAddAmenity(3, "Central A/C", enumerations.AmenityType.AIR_CONDITIONING);
            Amenity minibar = createAndAddAmenity(4, "Fully Stocked Minibar", enumerations.AmenityType.MINI_BAR);
            Amenity balcony = createAndAddAmenity(5, "Ocean View Balcony", enumerations.AmenityType.BALCONY);
            Amenity breakfast = createAndAddAmenity(6, "Continental Breakfast", enumerations.AmenityType.BREAKFAST_INCLUDED);
            Amenity hairDryer = createAndAddAmenity(7, "Hair Dryer", enumerations.AmenityType.HAIR_DRYER);

            RoomType single = createAndAddRoomType("Standard Single", 100.0, 101, 1);
            single.addAmenity(wifi); single.addAmenity(ac); single.addAmenity(tv);

            RoomType doubleRoom = createAndAddRoomType("Deluxe Double", 150.0, 102, 2);
            doubleRoom.addAmenity(wifi); doubleRoom.addAmenity(ac); doubleRoom.addAmenity(tv); doubleRoom.addAmenity(hairDryer);

            RoomType suite = createAndAddRoomType("Executive Suite", 350.0, 103, 4);
            suite.addAmenity(wifi); suite.addAmenity(ac); suite.addAmenity(tv); suite.addAmenity(minibar); suite.addAmenity(breakfast);

            RoomType penthouse = createAndAddRoomType("Presidential Penthouse", 850.0, 104, 6);
            penthouse.addAmenity(wifi); penthouse.addAmenity(ac); penthouse.addAmenity(tv); penthouse.addAmenity(minibar); 
            penthouse.addAmenity(breakfast); penthouse.addAmenity(balcony); penthouse.addAmenity(hairDryer);



            createAndAddRoom(101, 1, single);
            createAndAddRoom(102, 1, single);
            createAndAddRoom(103, 1, doubleRoom);

            createAndAddRoom(201, 2, doubleRoom);
            createAndAddRoom(202, 2, doubleRoom);
            createAndAddRoom(203, 2, suite);

            createAndAddRoom(301, 3, suite);
            createAndAddRoom(302, 3, suite);

            createAndAddRoom(401, 4, penthouse);
            createAndAddRoom(402, 4, penthouse);


            registerNewAdmin("admin_boss", "admin123", LocalDate.of(1980, 5, 20), 800, 1600); // 8 AM - 4 PM
            registerNewReceptionist("rec_morning", "rec123", LocalDate.of(1995, 8, 15), 800, 1600); // 8 AM - 4 PM
            registerNewReceptionist("rec_night", "rec123", LocalDate.of(1992, 11, 5), 1600, 2359); // 4 PM - Midnight


            Guest guest1 = registerNewGuest("ahmed_99", "password123", LocalDate.of(1999, 1, 15), 500.0, 
                "New Cairo, Egypt", enumerations.Gender.MALE, suite, 3, true, 400.0);
            
            Guest guest2 = registerNewGuest("sarah_smith", "password123", LocalDate.of(1985, 6, 30), 1200.0, 
                "London, UK", enumerations.Gender.FEMALE, penthouse, 4, true, 900.0);
            
            Guest guest3 = registerNewGuest("mohamed_ali", "password123", LocalDate.of(2001, 12, 10), 0.0, 
                "Alexandria, Egypt", enumerations.Gender.MALE, single, 1, false, 150.0);



            Room bookedSuite = getRoomByNumber(301);
            Reservation res1 = createAndAddReservation(guest1, bookedSuite, LocalDate.now(), LocalDate.now().plusDays(3));
            res1.confirm();      // Admin/System confirms it
            bookedSuite.setAvailable(false); // Room becomes physically occupied


            Room bookedPenthouse = getRoomByNumber(401);
            Reservation res2 = createAndAddReservation(guest2, bookedPenthouse, LocalDate.now().plusDays(1), LocalDate.now().plusDays(6));
            res2.confirm();
            bookedPenthouse.setAvailable(false);


            Invoice invoice1 = createAndAddInvoice(res1);

            java.util.List<enumerations.PaymentMethod> mixedPayments = new java.util.ArrayList<>();
            mixedPayments.add(enumerations.PaymentMethod.ONLINE);
            mixedPayments.add(enumerations.PaymentMethod.CASH);
            invoice1.pay(mixedPayments); 

            
            System.out.println(" Dummy data successfully loaded! (10 Rooms, 3 Guests, 3 Staff, 2 Reservations)");

        } catch (Exception e) {
            System.err.println(" Error loading dummy data: " + e.getMessage());
            e.printStackTrace();
        }
    }







}