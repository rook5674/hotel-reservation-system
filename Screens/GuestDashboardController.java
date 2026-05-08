package Screens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Guest;
import models.Reservation;
import enumerations.ReservationStatus;
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


    public void setGuestData(Guest guest, String password) {
        this.currentGuest = guest;
        
        welcomeLabel.setText("Welcome back, " + guest.getUserName() + "!");
        balanceLabel.setText("Current Balance: $" + guest.getBalance(guest.getUserName(), password));
        
        profileLabel.setText("Address: " + guest.getAddress(guest.getUserName(), password) + 
         "\nGender: " + guest.getGender(guest.getUserName(), password));

        loadReservationsTable();
    }

    private void loadReservationsTable() {

        colCheckIn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        colCheckOut.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        

        ObservableList<Reservation> obsReservations = FXCollections.observableArrayList(currentGuest.viewReservations());
        
        reservationsTable.setItems(obsReservations);
    }

    @FXML
    public void handleLogout() {

    }

    @FXML
    public void handleBookNewRoom() {
        
    }
}