package Screens;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Admin;

public class UserCredentialsManagementController {
    @FXML private TextField oldUsernameField;
    @FXML private TextField newUsernameField;
    @FXML private TextField targetUsernameField;
    @FXML private PasswordField newPasswordField;
    @FXML private Label statusLabel;

    @FXML
    private void initialize() {
        if (!(SessionContext.currentStaff instanceof Admin)) {
            ScreenNavigator.goTo("Login.fxml");
        }
    }

    @FXML
    private void handleChangeUsername() {
        try {
            String oldUsername = oldUsernameField.getText().trim();
            String newUsername = newUsernameField.getText().trim();

            Admin admin = (Admin) SessionContext.currentStaff;
            if (admin.changeUserUsername(oldUsername, newUsername)) {
                statusLabel.setText("Username changed successfully.");
            }
        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleResetPassword() {
        try {
            String targetUsername = targetUsernameField.getText().trim();
            String newPassword = newPasswordField.getText();

            Admin admin = (Admin) SessionContext.currentStaff;
            if (admin.resetUserPassword(targetUsername, newPassword)) {
                statusLabel.setText("Password reset successfully.");
            } else {
                statusLabel.setText("User not found.");
            }
        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        ScreenNavigator.goTo("AdminDashboard.fxml");
    }
}
