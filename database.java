import java.util.ArrayList;

public class Database{
    private static ArrayList<Room> rooms;
    private static ArrayList<Amenity> amenities;
    private static ArrayList<Guest> guests;
    private static ArrayList<Staff> staff;
    private static ArrayList<RoomType> roomTypes;
    private static ArrayList<invoice> invoices;
    private static ArrayList<Reservation> reservations;
    public Database(){
        rooms = new ArrayList<Room>();
        amenities = new ArrayList<Amenity>();
        guests = new ArrayList<Guest>();
        staff = new ArrayList<Staff>();
        roomTypes = new ArrayList<RoomType>();
        invoices = new ArrayList<invoice>();
        reservations = new ArrayList<Reservation>();
    }
    public void addRoom (Room room){
        rooms.add(room);
    }
    public void addAmenity (Amenity amenity){
        amenities.add(amenity);
    }
    addGuest (Guest guest){
        guests.add(guest);
    }
    public void addStaff (Staff staffMember){
        staff.add(staffMember);
    }
    public void addRoomType (RoomType roomType){
        roomTypes.add(roomType);
    }
    public void addInvoice (invoice invoice){
        invoices.add(invoice);
    }
    public void addReservation (Reservation reservation){
        reservations.add(reservation);
    }
    public bolean removeRoom (int roomNumber){
        for (Room room : rooms){
            if (room.getRoomNumber() == roomNumber){
                rooms.remove(room);
                return true;
            }
        }
        return false;
    }
    public boolean removeAmenity (String amenityName){
        for (Amenity amenity : amenities){
            if (amenity.getName().equals(amenityName)){
                amenities.remove(amenity);
                return true;
            }
        }
        return false;
    }
    public boolean removeGuest (String guestName){
        for (Guest guest : guests){
            if (guest.getName().equals(guestName)){
                guests.remove(guest);
                return true;
            }
        }
        return false;
    }
    public boolean removeStaff (String staffName){
        for (Staff staffMember : staff){
            if (staffMember.getName().equals(staffName)){
                staff.remove(staffMember);
                return true;
            }
        }
        return false;
    }
    public boolean removeRoomType (String roomTypeName){
        for (RoomType roomType : roomTypes){
            if (roomType.getName().equals(roomTypeName)){
                roomTypes.remove(roomType);
                return true;
            }
        }
        return false;
    }
    public boolean removeInvoice (int invoiceId){
        for (invoice invoice : invoices){
            if (invoice.getId() == invoiceId){
                invoices.remove(invoice);
                return true;
            }
        }
        return false;
    }
    public boolean removeReservation (int reservationId){
        for (Reservation reservation : reservations){
            if (reservation.getId() == reservationId){
                reservations.remove(reservation);
                return true;
            }
        }
        return false;
    }






}