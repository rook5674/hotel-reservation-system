package Screens;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.*;
import models.*;
import enumerations.ReservationStatus;

public class ReservationHistoryController {

    @FXML private TableView<Reservation>                      reservationTable;
    @FXML private TableColumn<Reservation, Integer>           idCol;
    @FXML private TableColumn<Reservation, String>            roomCol;
    @FXML private TableColumn<Reservation, String>            checkInCol;
    @FXML private TableColumn<Reservation, String>            checkOutCol;
    @FXML private TableColumn<Reservation, Long>              nightsCol;
    @FXML private TableColumn<Reservation, Double>            costCol;
    @FXML private TableColumn<Reservation, ReservationStatus> statusCol;
    @FXML private ComboBox<String>                            filterComboBox;

    private Guest loggedInGuest;

    public void setGuest(Guest guest) {
        this.loggedInGuest = guest;
    }

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("reservationId"));

        roomCol.setCellValueFactory(c ->
                new SimpleStringProperty("Room " + c.getValue().getRoom().getRoomNumber()));

        checkInCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getCheckInDate().toString()));

        checkOutCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getCheckOutDate().toString()));

        nightsCol.setCellValueFactory(c ->
                new SimpleLongProperty(c.getValue().getNumberOfNights()).asObject());

        costCol.setCellValueFactory(c ->
                new SimpleDoubleProperty(c.getValue().calculateTotalCost()).asObject());

        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Color code status
        statusCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(ReservationStatus status, boolean empty) {
                super.updateItem(status, empty);
                getStyleClass().removeAll(
                        "status-pending","status-confirmed",
                        "status-cancelled","status-completed");
                if (empty || status == null) {
                    setText(null);
                } else {
                    setText(status.toString());
                    switch (status) {
                        case PENDING   -> getStyleClass().add("status-pending");
                        case CONFIRMED -> getStyleClass().add("status-confirmed");
                        case CANCELLED -> getStyleClass().add("status-cancelled");
                        case COMPLETED -> getStyleClass().add("status-completed");
                    }
                }
            }
        });

        filterComboBox.getItems().addAll(
                "All", "PENDING", "CONFIRMED", "CANCELLED", "COMPLETED");
        filterComboBox.setValue("All");
        filterComboBox.setOnAction(e -> loadReservations());

        loadReservations();
    }

    @FXML
    private void handleRefresh() {
        loadReservations();
    }

    private void loadReservations() {
        String filter = filterComboBox.getValue();
        ObservableList<Reservation> list = FXCollections.observableArrayList();

        for (Reservation r : Database.getAllReservations()) {
            // If a guest is logged in, show only their reservations
            boolean matchesGuest = loggedInGuest == null ||
                    r.getGuest().getUserName().equals(loggedInGuest.getUserName());
            boolean matchesFilter = filter.equals("All") ||
                    r.getStatus().toString().equals(filter);

            if (matchesGuest && matchesFilter) {
                list.add(r);
            }
        }
        reservationTable.setItems(list);
    }
}