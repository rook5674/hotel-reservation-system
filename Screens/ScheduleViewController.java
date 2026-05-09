package Screens;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Admin;
import models.Staff;

public class ScheduleViewController {
    @FXML private Label roleLabel;
    @FXML private Label scheduleLabel;

    @FXML
    private void initialize() {
        Staff staff = SessionContext.currentStaff;
        if (staff == null) {
            ScreenNavigator.goTo("Login.fxml");
            return;
        }

        roleLabel.setText("Role: " + staff.getStaffRole());
        scheduleLabel.setText("Working Hours: " + staff.getWorkingHours());
    }

    @FXML
    private void handleBack() {
        if (SessionContext.currentStaff instanceof Admin) {
            ScreenNavigator.goTo("AdminDashboard.fxml");
        } else {
            ScreenNavigator.goTo("ReceptionistDashboard.fxml");
        }
    }
}
