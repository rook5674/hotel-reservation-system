package models;
import enumerations.StaffRole;

public class Admin extends Staff implements interfaces.Manageable<Room> {

    public Admin(String name, String password, java.time.LocalDate dateOfBirth, int StartTime, int EndTime) throws Exception {

        super(name, password, dateOfBirth, StartTime, EndTime, StaffRole.Admin);
    }

    public Boolean updateRoomAvailability(int roomNumber, boolean isAvailable)  {
        Room room = Database.getRoomByNumber(roomNumber);
        if (room != null) {
            room.setAvailable(isAvailable);
            return true;
        } 
        return false;
    }

    public Boolean createRoomType(String typeName, double basePrice, int id, int maxOccupancy) throws Exception {
        Database.createAndAddRoomType(typeName, basePrice, id, maxOccupancy);
        return true;
    }
    
    public RoomType readRoomType(String typeName) { return Database.getRoomTypeByName(typeName); }

    public boolean updateRoomTypePrice(String typeName, double newPrice) throws Exception {
        RoomType rt = Database.getRoomTypeByName(typeName);
        if (rt != null) {
            rt.setBasePricePerNight(newPrice);
            return true;
        }
        return false;
    }

    public boolean deleteRoomType(String typeName) throws Exception {
        if (Database.removeRoomType(typeName)) return true;
        return false;

    }

    public Boolean createAmenity(int id, String name, enumerations.AmenityType type) throws Exception {
        Database.createAndAddAmenity(id, name, type);
        return true;

    }
    
    public Amenity readAmenity(String name) { return Database.getAmenityByName(name); }

    public boolean updateAmenityName(String oldName, String newName) throws Exception {
        Amenity amenity = Database.getAmenityByName(oldName);
        if (amenity != null) {
            amenity.setName(newName);
            return true;
        }
        return false;
    }

    public boolean deleteAmenity(String name) throws Exception {
        if (Database.removeAmenity(name)) return true;
        return false;
    }


    @Override
    public boolean create(Room item) throws Exception {
        Database.addRoom(item);
        return true;
    }

    @Override
    public Room read(int roomNumber) {
        return Database.getRoomByNumber(roomNumber);
    }

    @Override
    public boolean delete(int roomNumber) throws Exception {
        return Database.removeRoom(roomNumber);
    }

    public boolean resetUserPassword(String targetUsername, String newPassword) throws Exception {
        Guest guest = Database.getGuestByUsername(targetUsername);
        if (guest != null) {
            guest.forceSetPassword(newPassword);
            return true;
        }
        Staff staffUser = Database.getStaffByUsername(targetUsername);
        if (staffUser != null) {
            staffUser.forceSetPassword(newPassword);
            return true;
        }
        return false;
    }

    public boolean changeUserUsername(String oldUsername, String newUsername) throws Exception {
        if (Database.getGuestByUsername(newUsername) != null || Database.getStaffByUsername(newUsername) != null) {
            throw new Exception("Error: The username '" + newUsername + "' is already taken.");
        }
        Guest guest = Database.getGuestByUsername(oldUsername);
        if (guest != null) {
            guest.forceSetUserName(newUsername);
            return true;
        }
        Staff staffUser = Database.getStaffByUsername(oldUsername);
        if (staffUser != null) {
            staffUser.forceSetUserName(newUsername);
            return true;
        }
        throw new Exception("Error: Original username not found.");
    }

    public boolean findForgottenUsernameByDOB(java.time.LocalDate dateOfBirth) {
        for (Guest g : Database.getAllGuests()) {
            if (g.getDateOfBirth().equals(dateOfBirth)) return true;
        }
        for (Staff s : Database.getAllStaff()) {
            if (s.getDateOfBirth().equals(dateOfBirth)) return true;
        }
        return false;
    }

}