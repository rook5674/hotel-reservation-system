public class main {
    public static void main(String[] args) {
        try {
            Amenity wifi = new Amenity(1, "WiFi", AmenityType.WIFI);
            Amenity tv = new Amenity(2, "TV", AmenityType.TV);
            Amenity miniBar = new Amenity(3, "Mini Bar", AmenityType.MINI_BAR);

            RoomType suite = new RoomType(1, "Suite", 3, 250.0);
            RoomType single = new RoomType(2, "Single", 1, 100.0);
            RoomType double_ = new RoomType(3, "Double", 2, 150.0);

            suite.addAmenity(wifi);
            suite.addAmenity(tv);
            suite.addAmenity(miniBar);
            single.addAmenity(wifi);
            double_.addAmenity(wifi);
            double_.addAmenity(tv);

            System.out.println(suite);
            System.out.println(single);
            System.out.println(double_);

            suite.removeAmenity(3);
            System.out.println("After removing Mini Bar: " + suite);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}