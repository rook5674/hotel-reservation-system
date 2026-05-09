package Screens;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import models.Database;
import models.Guest;
import models.Staff;

import java.time.LocalDate;
import java.util.ArrayList;

public class AccountRecoveryController {
    @FXML private DatePicker dobPicker;
    @FXML private Label resultLabel;

    @FXML
    public void handleRecover() {
        LocalDate dob = dobPicker.getValue();
        if (dob == null) {
            resultLabel.setText("Please select your date of birth.");
            return;
        }

        ArrayList<String> usernames = findUsernamesByDob(dob);

        if (usernames.isEmpty()) {
            resultLabel.setText("No username found for this date of birth.");
        } else {
            resultLabel.setText("Matching username(s): " + String.join(", ", usernames));
        }
    }

    private ArrayList<String> findUsernamesByDob(LocalDate dob) {
        ArrayList<String> usernames = new ArrayList<>();

        for (Guest guest : Database.getAllGuests()) {
            if (guest.getDateOfBirth().equals(dob)) {
                usernames.add(guest.getUserName());
            }
        }

        for (Staff staff : Database.getAllStaff()) {
            if (staff.getDateOfBirth().equals(dob)) {
                usernames.add(staff.getUserName());
            }
        }

        return usernames;
    }

    @FXML
    public void handleBack() {
        ScreenNavigator.goTo("Login.fxml");
    }
}
