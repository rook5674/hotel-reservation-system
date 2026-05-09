package Screens;

import enumerations.Gender;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Database;
import models.Guest;
import models.RoomType;

public class ProfileSettingsController {
    @FXML private TextField addressField;
    @FXML private ComboBox<Gender> genderComboBox;
    @FXML private ComboBox<RoomType> roomTypeComboBox;
    @FXML private TextField preferredFloorField;
    @FXML private CheckBox seaViewCheckBox;
    @FXML private TextField maxPriceField;
    @FXML private Label statusLabel;

    private Guest guest;

    @FXML
    private void initialize() {
        guest = SessionContext.currentGuest;
        if (guest == null) {
            ScreenNavigator.goTo("RoleSelection.fxml");
            return;
        }

        genderComboBox.setItems(FXCollections.observableArrayList(Gender.values()));
        roomTypeComboBox.setItems(FXCollections.observableArrayList(Database.getAllRoomTypes()));

        addressField.setText(guest.getAddress());
        genderComboBox.setValue(guest.getGender());

        if (!Database.getAllRoomTypes().isEmpty()) {
            roomTypeComboBox.setValue(Database.getAllRoomTypes().get(0));
        }
        preferredFloorField.setText("1");
        maxPriceField.setText("300");
    }

    @FXML
    private void handleSave() {
        try {
            String address = addressField.getText().trim();
            Gender gender = genderComboBox.getValue();
            RoomType roomType = roomTypeComboBox.getValue();
            int floor = UiUtil.parseInt(preferredFloorField.getText(), "Preferred floor");
            double maxPrice = UiUtil.parseDouble(maxPriceField.getText(), "Max price");

            if (address.isBlank() || gender == null || roomType == null) {
                statusLabel.setText("Please fill in all required fields.");
                return;
            }

            guest.setAddress(address);
            guest.setGender(gender);
            guest.setRoomPreferences(roomType, floor, seaViewCheckBox.isSelected(), maxPrice);

            statusLabel.setText("Profile updated successfully.");
        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        ScreenNavigator.goTo("GuestDashboard.fxml");
    }
}
