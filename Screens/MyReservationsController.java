package Screens;

import enumerations.ReservationStatus;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Database;
import models.Guest;
import models.Reservation;

import java.util.ArrayList;

public class MyReservationsController {
    @FXML private TableView<Reservation> reservationTable;
    @FXML private TableColumn<Reservation, Integer> idCol;
    @FXML private TableColumn<Reservation, String> roomCol;
    @FXML private TableColumn<Reservation, String> checkInCol;
    @FXML private TableColumn<Reservation, String> checkOutCol;
    @FXML private TableColumn<Reservation, Long> nightsCol;
    @FXML private TableColumn<Reservation, Double> costCol;
    @FXML private TableColumn<Reservation, ReservationStatus> statusCol;
    @FXML private ComboBox<String> filterComboBox;
    @FXML private Label statusLabel;

    private Guest guest;
    private Thread reservationLoaderThread;

    @FXML
    private void initialize() {
        guest = SessionContext.currentGuest;
        if (guest == null) {
            ScreenNavigator.goTo("RoleSelection.fxml");
            return;
        }

        idCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getReservationId()).asObject());
        roomCol.setCellValueFactory(c -> new SimpleStringProperty("Room " + c.getValue().getRoom().getRoomNumber()));
        checkInCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCheckInDate().toString()));
        checkOutCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCheckOutDate().toString()));
        nightsCol.setCellValueFactory(c -> new SimpleLongProperty(c.getValue().getNumberOfNights()).asObject());
        costCol.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().calculateTotalCost()).asObject());
        statusCol.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getStatus()));

        filterComboBox.getItems().addAll("All", "PENDING", "CONFIRMED", "CANCELLED", "COMPLETED");
        filterComboBox.setValue("All");

        filterComboBox.setOnAction(e -> loadReservationsWithThread());

        loadReservationsWithThread();
    }


    private void loadReservationsWithThread() {
        statusLabel.setText("Loading reservations in background...");

        final String selectedFilter = filterComboBox.getValue() == null ? "All" : filterComboBox.getValue();
        final String currentGuestUsername = guest.getUserName();

        stopReservationLoaderThread();

        Runnable loadTask = new Runnable() {
            @Override
            public void run() {
                try {
                    String workerThreadName = Thread.currentThread().getName();


                    Thread.sleep(650);

                    ArrayList<Reservation> snapshot;


                    synchronized (Database.class) {
                        snapshot = new ArrayList<>(Database.getAllReservations());
                    }

                    ArrayList<Reservation> filtered = new ArrayList<>();

                    for (Reservation reservation : snapshot) {
                        boolean matchesGuest = reservation.getGuest().getUserName().equals(currentGuestUsername);

                        boolean matchesFilter =selectedFilter.equals("All") ||reservation.getStatus().toString().equals(selectedFilter);

                        if (matchesGuest && matchesFilter) {
                            filtered.add(reservation);
                        }
                    }


                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            reservationTable.setItems(FXCollections.observableArrayList(filtered));
                            statusLabel.setText(filtered.size()+ " reservation(s) shown. Loaded by background thread: "+ workerThreadName);
                        }
                    });

                } catch (InterruptedException e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            statusLabel.setText("Previous reservation loading task was stopped.");
                        }
                    });
                } catch (Exception e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            statusLabel.setText("Could not load reservations: " + e.getMessage());
                        }
                    });
                }
            }
        };

        reservationLoaderThread = new Thread(loadTask);
        reservationLoaderThread.setName("reservation-loader-thread");
        reservationLoaderThread.setDaemon(true);
        reservationLoaderThread.start();
    }

    private void stopReservationLoaderThread() {
        if (reservationLoaderThread != null && reservationLoaderThread.isAlive()) {
            reservationLoaderThread.interrupt();
        }
    }

    @FXML
    private void handleCancelSelected() {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            statusLabel.setText("Please select a reservation to cancel.");
            return;
        }

        try {
            boolean cancelled;
            synchronized (Database.class) {
                cancelled = guest.cancelReservation(selected);
            }

            if (cancelled) {
                loadReservationsWithThread();
                UiUtil.showInfo("Cancelled", "Reservation #" + selected.getReservationId() + " was cancelled.");
            } else {
                statusLabel.setText("You can only cancel your own reservation.");
            }
        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleCheckoutSelected() {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            statusLabel.setText("Please select a confirmed reservation to checkout.");
            return;
        }

        if (selected.getStatus() != ReservationStatus.CONFIRMED) {
            statusLabel.setText("Only CONFIRMED reservations can be paid.");
            return;
        }

        stopReservationLoaderThread();
        SessionContext.selectedReservation = selected;
        ScreenNavigator.goTo("Checkout.fxml");
    }

    @FXML
    private void handleRefresh() {
        loadReservationsWithThread();
    }

    @FXML
    private void handleBack() {
        stopReservationLoaderThread();
        ScreenNavigator.goTo("GuestDashboard.fxml");
    }
}
