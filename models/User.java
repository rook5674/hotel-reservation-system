package models;
import enumerations.UserType;
import java.time.LocalDate;

abstract class User {
    private String userName;
    private String password;
    private LocalDate dateOfBirth;
    private UserType type;

    public User(String userName, String password, LocalDate dateOfBirth) {
        this.userName = userName;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
    }
    public User(){
    }
    public String getUserName() {
        return userName;
    }
    public boolean setUserName(String userName, String password) {
        if (this.password.equals(password)) {
            this.userName = userName;
            return true;
        }
        else
            return false;
    }
    public String getPassword() {
        return password;
    }
    public boolean setPassword(String password, String oldPassword) {
        if (this.password.equals(oldPassword)) {
            this.password = password;
            return true;
        }
        else
            return false;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public boolean setDateOfBirth(LocalDate dateOfBirth,String userName, String password) {
        if (this.password.equals(password) && this.userName.equals(userName)) {
            this.dateOfBirth = dateOfBirth;
            return true;
        }
        else
            return false;
    }
    public UserType getRole() {
        return type;
    }
    public void setRole(UserType type) {
        this.type = type;
    }
    public boolean login(String userName, String password) {
        if (this.userName.equals(userName) && this.password.equals(password)) {
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("Invalid username or password.");
            return false;
        }
    }


 
    
}