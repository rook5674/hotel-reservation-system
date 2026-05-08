public class GuestRegistrationController {
    public boolean registerGuest(String name, String email, String password) {
        // TODO: Add registration logic (e.g., save to database, validate input)
        if (name == null || name.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }
        // Simulate registration success
        return true;
    }
}
