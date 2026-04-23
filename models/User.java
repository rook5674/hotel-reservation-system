package models;
import java.time.LocalDate;

import enumerations.UserType;

abstract class User implements interfaces.Authenticatable{
    private String userName;
    private String password;
    private LocalDate dateOfBirth;
    private UserType type;

    public User(String userName, String password, LocalDate dateOfBirth) throws Exception {
        if (password == null || password.length() < 6) {
            throw new Exception("Password must be at least 6 characters long.");
        } else if (userName == null ) {
            throw new Exception("Username cannot be empty.");
        } else if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now())) {
            throw new Exception("Date of birth must be a valid past date.");
        }
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
    public boolean setDateOfBirth(LocalDate dateOfBirth, String password) {
        if (this.password.equals(password)) {
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

    @Override
    public void register() {
        // Since your Database factory methods handle the actual saving, 
        // this can just be a confirmation behavior.
        System.out.println("User " + this.userName + " is successfully registered.");
    }

    
}