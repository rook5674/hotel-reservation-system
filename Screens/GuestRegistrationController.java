package Screens;

import enumerations.Gender;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Database;
import models.RoomType;

import java.time.LocalDate;

public class GuestRegistrationController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private DatePicker dobPicker;
    @FXML private TextField addressField;
    @FXML private ComboBox<Gender> genderComboBox;
    @FXML private ComboBox<RoomType> roomTypeComboBox;
    @FXML private TextField preferredFloorField;
    @FXML private CheckBox seaViewCheckBox;
    @FXML private TextField maxPriceField;
    @FXML private TextField balanceField;
    @FXML private Label statusLabel;

    @FXML
    private void initialize() {
        genderComboBox.setItems(FXCollections.observableArrayList(Gender.values()));
        roomTypeComboBox.setItems(FXCollections.observableArrayList(Database.getAllRoomTypes()));
    }

    @FXML
    private void handleRegister() {
        try {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            LocalDate dob = dobPicker.getValue();
            String address = addressField.getText().trim();
            Gender gender = genderComboBox.getValue();
            RoomType roomType = roomTypeComboBox.getValue();
            int preferredFloor = UiUtil.parseInt(preferredFloorField.getText(), "Preferred floor");
            double maxPrice = UiUtil.parseDouble(maxPriceField.getText(), "Max price");
            double balance = UiUtil.parseDouble(balanceField.getText(), "Initial balance");

            if (username.isBlank() || password.isBlank() || dob == null || address.isBlank() || gender == null || roomType == null) {
                statusLabel.setText("Please fill in all required fields.");
                return;
            }

            Database.registerNewGuest(
                    username,
                    password,
                    dob,
                    balance,
                    address,
                    gender,
                    roomType,
                    preferredFloor,
                    seaViewCheckBox.isSelected(),
                    maxPrice
            );

            UiUtil.showInfo("Registration Successful", "Guest account created successfully.");
            ScreenNavigator.goTo("Login.fxml");

        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        ScreenNavigator.goTo("Login.fxml");
    }
}
