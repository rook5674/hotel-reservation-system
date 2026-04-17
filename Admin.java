public class Admin extends Staff {

    public Admin(String name, String password, java.time.LocalDate dateOfBirth, int StartTime, int EndTime) throws Exception {

        super(name, password, dateOfBirth, StartTime, EndTime, staff_role.Admin);
    }
    
    public void createRoom(int roomNumber, RoomType roomType, int floor) throws Exception {
        Room newRoom = new Room(roomNumber, floor, roomType);
    
        hotelname.addRoom(newRoom);
         //hotelname will be made in main)
    }
    
    /*public Room readRoom(int roomNumber) {
        
        //search database for room with roomNumber and return it
        return room; // Placeholder return statement
    } */

    /*public void updateRoom(int roomNumber, String newRoomType, int newFloor) throws Exception {
            //search database for room with roomNumber
            //if found, update its type and floor
            Room roomToUpdate = readRoom(roomNumber);
            Room roomToUpdate = null; // Placeholder for the actual room retrieval logic
            if (roomToUpdate != null) {
                roomToUpdate.setRoomType(newRoomType);
                roomToUpdate.setFloor(newFloor);
            } else {
                throw new Exception("Error: Room not found.");
            }
        }
        */  
    /*public void deleteRoom(int roomNumber) {
        //search database for room with roomNumber and delete it
    } */
}