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


    public String getPreferredRoomType() {
        return preferredRoomType.toString();
    }
    public boolean isSeaview() {
        return seaview; 
    }
    public int getPreferredFloornumber() {
        return preferredFloornumber;
    }


    public double getPrice() {
        return price;
    }
    



}