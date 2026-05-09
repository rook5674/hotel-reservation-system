package Screens;

import enumerations.ReservationStatus;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Guest;
import models.Reservation;

import java.time.LocalDate;

public class GuestDashboardController {
    @FXML private Label welcomeLabel;
    @FXML private Label balanceLabel;
    @FXML private Label profileLabel;

    @FXML private TableView<Reservation> reservationsTable;
    @FXML private TableColumn<Reservation, Integer> colRoomNumber;
    @FXML private TableColumn<Reservation, LocalDate> colCheckIn;
    @FXML private TableColumn<Reservation, LocalDate> colCheckOut;
    @FXML private TableColumn<Reservation, ReservationStatus> colStatus;

    private Guest currentGuest;

    @FXML
    public void initialize() {
        currentGuest = SessionContext.currentGuest;
        if (currentGuest == null) {
            ScreenNavigator.goTo("RoleSelection.fxml");
            return;
        }

        String username = currentGuest.getUserName();
        String password = SessionContext.currentPassword;

        welcomeLabel.setText("Welcome back, " + username + "!");
        balanceLabel.setText("Current Balance: $" + String.format("%.2f", currentGuest.getBalance(username, password)));
        profileLabel.setText(
                "Address: " + currentGuest.getAddress(username, password) +
                "\nGender: " + currentGuest.getGender(username, password) +
                "\n" + currentGuest.getRoomPreferences(username, password)
        );

        loadReservationsTable();
    }

    private void loadReservationsTable() {
        colRoomNumber.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getRoom().getRoomNumber()).asObject()
        );
        colCheckIn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        colCheckOut.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        reservationsTable.setItems(FXCollections.observableArrayList(currentGuest.viewReservations()));
    }

    @FXML public void handleProfile() { ScreenNavigator.goTo("ProfileSettings.fxml"); }
    @FXML public void handleWallet() { ScreenNavigator.goTo("Wallet.fxml"); }
    @FXML public void handleBrowseRooms() { ScreenNavigator.goTo("RoomBrowsing.fxml"); }
    @FXML public void handleBookNewRoom() { ScreenNavigator.goTo("RoomBrowsing.fxml"); }
    @FXML public void handleMyReservations() { ScreenNavigator.goTo("MyReservations.fxml"); }
    @FXML public void handleCheckout() { ScreenNavigator.goTo("Checkout.fxml"); }

    @FXML
    public void handleLogout() {
        SessionContext.logout();
        ScreenNavigator.goTo("RoleSelection.fxml");
    }
}
