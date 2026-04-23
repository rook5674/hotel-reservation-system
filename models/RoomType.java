package models;
import interfaces.Budgetable;

public class RoomType implements Budgetable {
    @Override
    public double getPrice() {
        return basePricePerNight;
    }

    @Override
    public boolean isAffordable(double budget) {
        return basePricePerNight <= budget;
    }
    private final int id;
    private String typeName;
    private int maxOccupancy;
    private double basePricePerNight;
    private Amenity[] amenities;
    private int amenityCount;

    public RoomType(int id, String typeName, int maxOccupancy, double basePricePerNight) throws Exception {
        if (typeName == null)
            throw new Exception("Error: Room type name cannot be null.");
        if (maxOccupancy <= 0)
            throw new Exception("Error: Max occupancy must be positive.");
        if (basePricePerNight < 0)
            throw new Exception("Error: Base price cannot be negative.");

        this.id = id;
        this.typeName = typeName;
        this.maxOccupancy = maxOccupancy;
        this.basePricePerNight = basePricePerNight;
        this.amenities = new Amenity[10];
        this.amenityCount = 0;
    }

    public void addAmenity(Amenity amenity) throws Exception {
        if (amenity == null)
            throw new Exception("Error: Cannot add a null amenity.");
        if (amenityCount == amenities.length)
            throw new Exception("Error: Amenity list is full.");
        for (int i = 0; i < amenityCount; i++) {
            if (amenities[i].getId() == amenity.getId())
                throw new Exception("Error: Amenity already exists in this room type.");
        }
        amenities[amenityCount++] = amenity;
    }

    public void removeAmenity(int amenityId) throws Exception {
        for (int i = 0; i < amenityCount; i++) {
            if (amenities[i].getId() == amenityId) {
                amenities[i] = amenities[--amenityCount];
                amenities[amenityCount] = null;
                return;
            }
        }
        throw new Exception("Error: Amenity with id " + amenityId + " not found.");
    }

    public Amenity[] getAmenities() {
        Amenity[] result = new Amenity[amenityCount];
        for (int i = 0; i < amenityCount; i++)
            result[i] = amenities[i];
        return result;
    }

    public int getId() { return id; }
    public String getTypeName() { return typeName; }
    public int getMaxOccupancy() { return maxOccupancy; }
    public double getBasePricePerNight() { return basePricePerNight; }

    public void setTypeName(String typeName) throws Exception {
        if (typeName == null)
            throw new Exception("Error: Room type name cannot be null.");
        this.typeName = typeName;
    }

    public void setMaxOccupancy(int maxOccupancy) throws Exception {
        if (maxOccupancy <= 0)
            throw new Exception("Error: Max occupancy must be positive.");
        this.maxOccupancy = maxOccupancy;
    }

    public void setBasePricePerNight(double basePricePerNight) throws Exception {
        if (basePricePerNight < 0)
            throw new Exception("Error: Base price cannot be negative.");
        this.basePricePerNight = basePricePerNight;
    }

    @Override
    public String toString() {
        String result = typeName + "\n" + id  + 
                "\n" + maxOccupancy +
                "\n" + basePricePerNight +
                "\n";
        if (amenityCount == 0) {
            result += "None";
        } else {
            for (int i = 0; i < amenityCount; i++) {
                result += "\n    - " + amenities[i].getName();
            }
        }
        return result;
    }


}