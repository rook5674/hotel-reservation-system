package models;
import enumerations.AmenityType;

public class Amenity {
    public void display() {
        System.out.println("Amenity: " + name + " (id=" + id + ")" +
                "\n  Type: " + type);
    }
    private int id;
    private String name;
    private AmenityType type;
    public Amenity(int id, String name, AmenityType type) throws Exception {
        if (name == null || name.isBlank())
            throw new Exception("Error: Amenity name cannot be empty.");
        if (type == null)
            throw new Exception("Error: Amenity type cannot be null.");

        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public AmenityType getType() { return type; }

    public void setName(String name) throws Exception {
        if (name == null || name.isBlank())
            throw new Exception("Error: Amenity name cannot be empty.");
        this.name = name;
    }

    public void setType(AmenityType type) throws Exception {
        if (type == null)
            throw new Exception("Error: Amenity type cannot be null.");
        this.type = type;
    }

    @Override
    public String toString() {
        return "Amenity{id=" + id + ", name='" + name + "', type=" + type + "}";
    }
}