import java.util.ArrayList;
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



    public static Guest registerNewGuest(String username, String password, LocalDate dateOfBirth, double balance, String address, Gender gender, RoomType prefType, int prefFloor, boolean seaview , double price) {
        Guest newGuest = new Guest(username, password, dateOfBirth, address, gender, prefType, prefFloor, seaview , price);
        guests.add(newGuest);
        return newGuest;
    }


    public static Admin registerNewAdmin(String username, String password, LocalDate dateOfBirth, Schedule workingHours, int StartTime, int EndTime) {
        Admin newAdmin = new Admin(username, password, dateOfBirth, StartTime, EndTime);
        staff.add(newAdmin);
        return newAdmin;
    }


    public static Receptionist registerNewReceptionist(String username, String password, LocalDate dateOfBirth,  int StartTime, int EndTime)  {
        // Notice we hardcode Role.RECEPTIONIST here
        Receptionist newReceptionist = new Receptionist(username, password, dateOfBirth, StartTime, EndTime);
        staff.add(newReceptionist);
        return newReceptionist;
    }


    public static Room createAndAddRoom(int roomNumber, int floor, RoomType type) {
        Room newRoom = new Room(roomNumber, floor, type);
        rooms.add(newRoom);
        return newRoom;
    }

    public static RoomType createAndAddRoomType(String typeName , double basePrice, int id, int maxOccupancy ) throws Exception {
        RoomType newRoomType = new RoomType(id, typeName,   maxOccupancy, basePrice);
        roomTypes.add(newRoomType);
        return newRoomType;
    }

    public static Amenity createAndAddAmenity(int id,String name , AmenityType type) {
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







}