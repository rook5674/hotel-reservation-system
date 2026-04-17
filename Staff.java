
public class Staff extends User {

    Schedule workinghours ; 


    private final staff_role role;
    public enum staff_role {
    Receptionist,
    Admin
    }

    public Staff(String username, String password, java.time.LocalDate dateOfBirth, int StartTime, int EndTime, staff_role role) throws Exception {
        super(username, password, dateOfBirth);
        super.setRole(UserType.Staff);
        this.workinghours = new Schedule(StartTime, EndTime);
        this.role = role;
        addStaff(this);

    }

    public Schedule getWorkingHours() {
        return workinghours;
    }

    public void setWorkingHours(Schedule workinghours) {
        this.workinghours = workinghours;
    }

    public staff_role getstaff_role() {
        return role;
    }
}