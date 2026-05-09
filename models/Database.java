package models;

import java.util.ArrayList;
import java.time.LocalDate;

import enumerations.AmenityType;
import enumerations.Gender;

public class Database {
    private static ArrayList<Room> rooms = new ArrayList<>();
    private static ArrayList<Amenity> amenities = new ArrayList<>();
    private static ArrayList<Guest> guests = new ArrayList<>();
    private static ArrayList<Staff> staff = new ArrayList<>();
    private static ArrayList<RoomType> roomTypes = new ArrayList<>();
    private static ArrayList<Invoice> invoices = new ArrayList<>();
    private static ArrayList<Reservation> reservations = new ArrayList<>();

    private Database() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static synchronized void clearAll() {
        rooms.clear();
        amenities.clear();
        guests.clear();
        staff.clear();
        roomTypes.clear();
        invoices.clear();
        reservations.clear();
    }

    public static synchronized void addRoom(Room room) {
        if (room == null) throw new IllegalArgumentException("Room cannot be null.");
        if (getRoomByNumber(room.getRoomNumber()) != null) {
            throw new IllegalArgumentException("Room number already exists.");
        }
        rooms.add(room);
    }

    public static synchronized void addAmenity(Amenity amenity) {
        if (amenity == null) throw new IllegalArgumentException("Amenity cannot be null.");
        amenities.add(amenity);
    }

    public static synchronized void addGuest(Guest guest) {
        if (guest == null) throw new IllegalArgumentException("Guest cannot be null.");
        guests.add(guest);
    }

    public static synchronized void addStaff(Staff staffMember) {
        if (staffMember == null) throw new IllegalArgumentException("Staff member cannot be null.");
        staff.add(staffMember);
    }

    public static synchronized void addRoomType(RoomType roomType) {
        if (roomType == null) throw new IllegalArgumentException("Room type cannot be null.");
        roomTypes.add(roomType);
    }

    public static synchronized void addInvoice(Invoice invoice) {
        if (invoice == null) throw new IllegalArgumentException("Invoice cannot be null.");
        invoices.add(invoice);
    }

    public static synchronized void addReservation(Reservation reservation) {
        if (reservation == null) throw new IllegalArgumentException("Reservation cannot be null.");
        reservations.add(reservation);
    }

    public static synchronized boolean removeRoom(int roomNumber) {
        return rooms.removeIf(room -> room.getRoomNumber() == roomNumber);
    }

    public static synchronized boolean removeAmenity(String amenityName) {
        if (amenityName == null) return false;
        return amenities.removeIf(amenity -> amenity.getName().equalsIgnoreCase(amenityName));
    }

    public static synchronized boolean removeGuest(String guestName) {
        if (guestName == null) return false;
        return guests.removeIf(guest -> guest.getUserName().equals(guestName));
    }

    public static synchronized boolean removeStaff(String staffName) {
        if (staffName == null) return false;
        return staff.removeIf(staffMember -> staffMember.getUserName().equals(staffName));
    }

    public static synchronized boolean removeRoomType(String roomTypeName) {
        if (roomTypeName == null) return false;
        return roomTypes.removeIf(roomType -> roomType.getTypeName().equalsIgnoreCase(roomTypeName));
    }

    public static synchronized boolean removeInvoice(int invoiceId) {
        return invoices.removeIf(invoice -> invoice.getInvoiceId() == invoiceId);
    }

    public static synchronized boolean removeReservation(int reservationId) {
        return reservations.removeIf(reservation -> reservation.getReservationId() == reservationId);
    }

    public static synchronized Room getRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) return room;
        }
        return null;
    }

    public static synchronized Guest getGuestByUsername(String username) {
        if (username == null) return null;
        for (Guest guest : guests) {
            if (guest.getUserName().equals(username)) return guest;
        }
        return null;
    }

    public static synchronized Staff getStaffByUsername(String username) {
        if (username == null) return null;
        for (Staff staffMember : staff) {
            if (staffMember.getUserName().equals(username)) return staffMember;
        }
        return null;
    }

    public static synchronized RoomType getRoomTypeByName(String typeName) {
        if (typeName == null) return null;
        for (RoomType roomType : roomTypes) {
            if (roomType.getTypeName().equalsIgnoreCase(typeName)) return roomType;
        }
        return null;
    }

    public static synchronized Amenity getAmenityByName(String amenityName) {
        if (amenityName == null) return null;
        for (Amenity amenity : amenities) {
            if (amenity.getName().equalsIgnoreCase(amenityName)) return amenity;
        }
        return null;
    }

    public static synchronized Reservation getReservationById(int reservationId) {
        for (Reservation res : reservations) {
            if (res.getReservationId() == reservationId) return res;
        }
        return null;
    }

    public static synchronized Invoice getInvoiceById(int invoiceId) {
        for (Invoice inv : invoices) {
            if (inv.getInvoiceId() == invoiceId) return inv;
        }
        return null;
    }

    public static synchronized Guest registerNewGuest(
            String username,
            String password,
            LocalDate dateOfBirth,
            double balance,
            String address,
            Gender gender,
            RoomType prefType,
            int prefFloor,
            boolean seaview,
            double price
    ) throws Exception {
        if (getGuestByUsername(username) != null || getStaffByUsername(username) != null) {
            throw new Exception("Error: Username '" + username + "' is already taken.");
        }

        Guest newGuest = new Guest(username, password, dateOfBirth, address, gender, prefType, prefFloor, seaview, price);
        newGuest.setBalance(balance);
        guests.add(newGuest);
        return newGuest;
    }

    public static synchronized Admin registerNewAdmin(String username, String password, LocalDate dateOfBirth, int startTime, int endTime) throws Exception {
        if (getGuestByUsername(username) != null || getStaffByUsername(username) != null) {
            throw new Exception("Error: Username '" + username + "' is already taken.");
        }
        Admin newAdmin = new Admin(username, password, dateOfBirth, startTime, endTime);
        staff.add(newAdmin);
        return newAdmin;
    }

    public static synchronized Receptionist registerNewReceptionist(String username, String password, LocalDate dateOfBirth, int startTime, int endTime) throws Exception {
        if (getGuestByUsername(username) != null || getStaffByUsername(username) != null) {
            throw new Exception("Error: Username '" + username + "' is already taken.");
        }
        Receptionist newReceptionist = new Receptionist(username, password, dateOfBirth, startTime, endTime);
        staff.add(newReceptionist);
        return newReceptionist;
    }

    public static synchronized Room createAndAddRoom(int roomNumber, int floor, RoomType type) throws Exception {
        if (getRoomByNumber(roomNumber) != null) {
            throw new Exception("Error: Room number '" + roomNumber + "' already exists.");
        }
        Room newRoom = new Room(roomNumber, floor, type);
        rooms.add(newRoom);
        return newRoom;
    }

    public static synchronized RoomType createAndAddRoomType(String typeName, double basePrice, int id, int maxOccupancy) throws Exception {
        if (getRoomTypeByName(typeName) != null) {
            throw new Exception("Error: Room type '" + typeName + "' already exists.");
        }
        for (RoomType roomType : roomTypes) {
            if (roomType.getId() == id) {
                throw new Exception("Error: Room type ID '" + id + "' already exists.");
            }
        }
        RoomType newRoomType = new RoomType(id, typeName, maxOccupancy, basePrice);
        roomTypes.add(newRoomType);
        return newRoomType;
    }

    public static synchronized Amenity createAndAddAmenity(int id, String name, AmenityType type) throws Exception {
        if (getAmenityByName(name) != null) {
            throw new Exception("Error: Amenity '" + name + "' already exists.");
        }
        for (Amenity amenity : amenities) {
            if (amenity.getId() == id) {
                throw new Exception("Error: Amenity ID '" + id + "' already exists.");
            }
        }
        Amenity newAmenity = new Amenity(id, name, type);
        amenities.add(newAmenity);
        return newAmenity;
    }

    public static synchronized Reservation createAndAddReservation(Guest guest, Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        Reservation newReservation = new Reservation(guest, room, checkInDate, checkOutDate);
        reservations.add(newReservation);
        return newReservation;
    }

    public static synchronized Invoice createAndAddInvoice(Reservation reservation) {
        Invoice newInvoice = new Invoice(reservation);
        invoices.add(newInvoice);
        return newInvoice;
    }

    public static ArrayList<Guest> getAllGuests() { return guests; }
    public static ArrayList<Room> getAllRooms() { return rooms; }
    public static ArrayList<Reservation> getAllReservations() { return reservations; }
    public static ArrayList<RoomType> getAllRoomTypes() { return roomTypes; }
    public static ArrayList<Amenity> getAllAmenities() { return amenities; }
    public static ArrayList<Staff> getAllStaff() { return staff; }

    public static synchronized void initializeDummyData() {
        clearAll();

        try {
            System.out.println("Initializing Database with extensive dummy data...");

            Amenity wifi = createAndAddAmenity(1, "High-Speed WiFi", AmenityType.WIFI);
            Amenity tv = createAndAddAmenity(2, "4K Smart TV", AmenityType.TV);
            Amenity ac = createAndAddAmenity(3, "Central A/C", AmenityType.AIR_CONDITIONING);
            Amenity minibar = createAndAddAmenity(4, "Fully Stocked Minibar", AmenityType.MINI_BAR);
            Amenity balcony = createAndAddAmenity(5, "Ocean View Balcony", AmenityType.BALCONY);
            Amenity breakfast = createAndAddAmenity(6, "Continental Breakfast", AmenityType.BREAKFAST_INCLUDED);
            Amenity hairDryer = createAndAddAmenity(7, "Hair Dryer", AmenityType.HAIR_DRYER);

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

            registerNewAdmin("admin", "admin123", LocalDate.of(1980, 5, 20), 800, 1600);
            registerNewReceptionist("receptionist1", "rec123", LocalDate.of(1995, 8, 15), 800, 1600);
            registerNewReceptionist("receptionist2", "rec123", LocalDate.of(1992, 11, 5), 1600, 2359);

            Guest guest1 = registerNewGuest("ahmed", "password123", LocalDate.of(1999, 1, 15), 500.0,
                    "New Cairo, Egypt", Gender.MALE, suite, 3, true, 400.0);

            Guest guest2 = registerNewGuest("seif", "password123", LocalDate.of(1985, 6, 30), 1200.0,
                    "London, UK", Gender.FEMALE, penthouse, 4, true, 900.0);

            registerNewGuest("omar", "password123", LocalDate.of(2001, 12, 10), 0.0,
                    "Alexandria, Egypt", Gender.MALE, single, 1, false, 150.0);

            Room bookedSuite = getRoomByNumber(301);
            Reservation res1 = createAndAddReservation(guest1, bookedSuite, LocalDate.now(), LocalDate.now().plusDays(3));
            res1.confirm();
            bookedSuite.setAvailable(false);

            Room bookedPenthouse = getRoomByNumber(401);
            Reservation res2 = createAndAddReservation(guest2, bookedPenthouse, LocalDate.now().plusDays(1), LocalDate.now().plusDays(6));
            res2.confirm();
            bookedPenthouse.setAvailable(false);

            Invoice invoice1 = createAndAddInvoice(res1);
            java.util.List<enumerations.PaymentMethod> mixedPayments = new java.util.ArrayList<>();
            mixedPayments.add(enumerations.PaymentMethod.ONLINE);
            mixedPayments.add(enumerations.PaymentMethod.CASH);
            invoice1.pay(mixedPayments);

            System.out.println("Dummy data successfully loaded! (10 Rooms, 3 Guests, 3 Staff, 2 Reservations)");

        } catch (Exception e) {
            System.err.println("Error loading dummy data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
