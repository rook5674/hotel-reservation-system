package Screens;

import enumerations.StaffRole;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Database;
import models.Guest;
import models.Staff;

public class LoginController {
    @FXML private Label accessModeLabel;
    @FXML private Label loginTitleLabel;
    @FXML private Label loginSubtitleLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;
    @FXML private Button registerButton;

    @FXML
    private void initialize() {
        if (SessionContext.selectedLoginMode == null) {
            ScreenNavigator.goTo("RoleSelection.fxml");
            return;
        }

        if (SessionContext.isGuestLoginMode()) {
            accessModeLabel.setText("GUEST ACCESS / LOGIN");
            loginTitleLabel.setText("GUEST LOGIN");
            loginSubtitleLabel.setText("Book rooms, manage your wallet, view reservations, and checkout.");
            registerButton.setVisible(true);
            registerButton.setManaged(true);
        } else {
            accessModeLabel.setText("STAFF ACCESS / LOGIN");
            loginTitleLabel.setText("STAFF LOGIN");
            loginSubtitleLabel.setText("Admin and receptionist access only. Guest accounts cannot log in here.");
            registerButton.setVisible(false);
            registerButton.setManaged(false);
        }
    }

    @FXML
    private void handleLogin() {
        statusLabel.setText("");
        statusLabel.setStyle("");

        String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            showLoginError("Please enter username and password.");
            return;
        }

        if (SessionContext.isGuestLoginMode()) {
            loginAsGuest(username, password);
            return;
        }

        if (SessionContext.isStaffLoginMode()) {
            loginAsStaff(username, password);
            return;
        }

        showLoginError("Please choose Guest or Staff access first.");
    }

    private void loginAsGuest(String username, String password) {
        Guest guest = Database.getGuestByUsername(username);

        if (guest == null) {
            showLoginError("No guest account found for username: " + username);
            return;
        }

        if (!guest.login(username, password)) {
            showLoginError("Incorrect password for username: " + username);
            return;
        }

        SessionContext.loginGuest(guest, password);
        ScreenNavigator.goTo("GuestDashboard.fxml");
    }

    private void loginAsStaff(String username, String password) {
        Staff staff = Database.getStaffByUsername(username);

        if (staff == null) {
            showLoginError("No staff account found for username: " + username);
            return;
        }

        if (!staff.login(username, password)) {
            showLoginError("Incorrect password for username: " + username);
            return;
        }

        SessionContext.loginStaff(staff, password);

        if (staff.getStaffRole() == StaffRole.Admin) {
            ScreenNavigator.goTo("AdminDashboard.fxml");
        } else {
            ScreenNavigator.goTo("ReceptionistDashboard.fxml");
        }
    }

    private void showLoginError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: red;");
    }

    @FXML
    private void handleRegister() {
        if (!SessionContext.isGuestLoginMode()) {
            showLoginError("Guest registration is only available from Guest Access.");
            return;
        }
        ScreenNavigator.goTo("GuestRegistration.fxml");
    }

    @FXML
    private void handleForgotUsername() {
        ScreenNavigator.goTo("AccountRecovery.fxml");
    }

    @FXML
    private void handleBackToRoleSelection() {
        SessionContext.logout();
        ScreenNavigator.goTo("RoleSelection.fxml");
    }
}
