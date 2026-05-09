package models;
public  class RoomPreference{

    private RoomType preferredRoomType;
    private int preferredFloornumber;
    private boolean seaview;
    private double price;


    public RoomPreference(RoomType preferredRoomType, int preferredFloornumber, boolean seaview , double price) {
        this.preferredRoomType = preferredRoomType;
        this.preferredFloornumber = preferredFloornumber;
        this.seaview = seaview ;
        this.price = price;
    }


    @Override
    public String toString() {
        String typeName = (preferredRoomType != null) ? preferredRoomType.getTypeName() : "No Preference";
        return "Room Preferences:" +
                "\n - Preferred Type: " + typeName +
                "\n - Preferred Floor: " + preferredFloornumber +
                "\n - Sea View: " + (seaview ? "Yes" : "No") ;
    }




}