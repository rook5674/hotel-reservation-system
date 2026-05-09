package Screens;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.Database;
import models.Guest;
import models.Reservation;
import models.Room;

public class ReceptionistGlobalOverviewController {
    @FXML private TableView<Guest> guestsTable;
    @FXML private TableColumn<Guest, String> guestUsernameCol;
    @FXML private TableColumn<Guest, String> guestDobCol;

    @FXML private TableView<Room> roomsTable;
    @FXML private TableColumn<Room, Integer> roomNumberCol;
    @FXML private TableColumn<Room, String> roomTypeCol;
    @FXML private TableColumn<Room, Boolean> roomAvailableCol;

    @FXML private TableView<Reservation> reservationsTable;
    @FXML private TableColumn<Reservation, Integer> reservationIdCol;
    @FXML private TableColumn<Reservation, String> reservationGuestCol;
    @FXML private TableColumn<Reservation, String> reservationRoomCol;
    @FXML private TableColumn<Reservation, String> reservationStatusCol;

    @FXML
    private void initialize() {
        if (SessionContext.currentStaff == null) {
            ScreenNavigator.goTo("Login.fxml");
            return;
        }

        guestUsernameCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getUserName()));
        guestDobCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDateOfBirth().toString()));

        roomNumberCol.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getRoomNumber()).asObject());
        roomTypeCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getRoomType().getTypeName()));
        roomAvailableCol.setCellValueFactory(c ->
                new SimpleBooleanProperty(c.getValue().isAvailable()).asObject());

        reservationIdCol.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getReservationId()).asObject());
        reservationGuestCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getGuest().getUserName()));
        reservationRoomCol.setCellValueFactory(c ->
                new SimpleStringProperty("Room " + c.getValue().getRoom().getRoomNumber()));
        reservationStatusCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStatus().toString()));

        refresh();
    }

    @FXML
    private void refresh() {
        guestsTable.setItems(FXCollections.observableArrayList(Database.getAllGuests()));
        roomsTable.setItems(FXCollections.observableArrayList(Database.getAllRooms()));
        reservationsTable.setItems(FXCollections.observableArrayList(Database.getAllReservations()));
    }

    @FXML
    private void handleBack() {
        ScreenNavigator.goTo("ReceptionistDashboard.fxml");
    }
}
