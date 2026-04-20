public class Admin extends Staff {

    public Admin(String name, String password, java.time.LocalDate dateOfBirth, int StartTime, int EndTime) throws Exception {

        super(name, password, dateOfBirth, StartTime, EndTime, StaffRole.Admin);
    }
    
    public void createRoom(int roomNumber, RoomType roomType, int floor) throws Exception {
        Room newRoom = new Room(roomNumber, floor, roomType);
    
        Database.addRoom(newRoom); 
    }
    
    public Room readRoom(int roomNumber) {
        return Database.getRoomByNumber(roomNumber);
        
    } 

        
    public void deleteRoom(int roomNumber) throws Exception {
        Database.removeRoom(roomNumber); //database for room with roomNumber and remove it

    }
}