package models;
public class Room implements interfaces.Bookable {
    private int roomNumber;
    private int floor;
    private boolean isAvailable;
    private RoomType roomType;

    public Room(int roomNumber, int floor, RoomType roomType) throws Exception {
        if (roomNumber <= 0)
            throw new Exception("Error: Room number must be positive.");
        if (floor <= 0)
            throw new Exception("Error: Floor must be positive.");
        if (roomType == null)
            throw new Exception("Error: Room type cannot be null.");

        this.roomNumber = roomNumber;
        this.floor = floor;
        this.roomType = roomType;
        this.isAvailable = true;
    }

    public int getRoomNumber() { return roomNumber; }
    public int getFloor() { return floor; }
    public RoomType getRoomType() { return roomType; }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public void setRoomType(RoomType roomType) throws Exception {
        if (roomType == null)
            throw new Exception("Error: Room type cannot be null.");
        this.roomType = roomType;
    }

    public void setFloor(int floor) throws Exception {
        if (floor < 0)
            throw new Exception("Error: Floor cannot be negative.");
        else{
        this.floor = floor;
            }
    }

    @Override
    public String toString() {
        return "Room " + roomNumber + " (Floor " + floor + ")" +
                "\n  Type: " + roomType.getTypeName() +
                "\n  Price per Night: " + roomType.getBasePricePerNight() +
                "\n  Available: " + isAvailable;
    }


    @Override
    public boolean isAvailable() {
        return this.isAvailable; // Returns your boolean attribute
    }

    @Override
    public void book() {
        this.isAvailable = false;
    }

    @Override
    public void release() {
        this.isAvailable = true;
    }
}