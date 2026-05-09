package Screens;

import enumerations.AmenityType;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.*;

import java.util.ArrayList;

public class RoomBrowsingController {
    @FXML private ComboBox<RoomType> roomTypeComboBox;
    @FXML private ComboBox<AmenityType> amenityComboBox;
    @FXML private TextField maxPriceField;
    @FXML private CheckBox availableOnlyCheckBox;
    @FXML private CheckBox affordableOnlyCheckBox;

    @FXML private TableView<Room> roomsTable;
    @FXML private TableColumn<Room, Integer> roomNumberCol;
    @FXML private TableColumn<Room, Integer> floorCol;
    @FXML private TableColumn<Room, String> typeCol;
    @FXML private TableColumn<Room, Double> priceCol;
    @FXML private TableColumn<Room, String> amenitiesCol;
    @FXML private TableColumn<Room, Boolean> availableCol;

    @FXML private Label statusLabel;

    private Thread roomRefreshThread;
    private volatile boolean keepRefreshingRooms;

    @FXML
    private void initialize() {
        if (SessionContext.currentGuest == null) {
            ScreenNavigator.goTo("RoleSelection.fxml");
            return;
        }

        roomTypeComboBox.setItems(FXCollections.observableArrayList(Database.getAllRoomTypes()));
        amenityComboBox.setItems(FXCollections.observableArrayList(AmenityType.values()));
        availableOnlyCheckBox.setSelected(true);

        roomNumberCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getRoomNumber()).asObject());
        floorCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getFloor()).asObject());
        typeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRoomType().getTypeName()));
        priceCol.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getRoomType().getBasePricePerNight()).asObject());
        amenitiesCol.setCellValueFactory(c -> new SimpleStringProperty(formatAmenities(c.getValue().getRoomType())));
        availableCol.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isAvailable()));

        refreshRooms();
        startRoomAvailabilityThread();
    }

    private void startRoomAvailabilityThread() {
        keepRefreshingRooms = true;

        Runnable refreshTask = new Runnable() {
            @Override
            public void run() {
                int refreshCounter = 0;

                while (keepRefreshingRooms && !Thread.currentThread().isInterrupted()) {
                    try {
                        ArrayList<Room> snapshot;

                        synchronized (Database.class) {
                            snapshot = new ArrayList<>(Database.getAllRooms());
                        }

                        refreshCounter++;
                        final int currentRefreshNumber = refreshCounter;
                        final String workerThreadName = Thread.currentThread().getName();

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                displayRoomsFromSnapshot(
                                        snapshot,
                                        "Auto-refresh #" + currentRefreshNumber + " by " + workerThreadName
                                );
                            }
                        });

                        Thread.sleep(3000);

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (Exception e) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                statusLabel.setText("Auto-refresh failed: " + e.getMessage());
                            }
                        });
                    }
                }
            }
        };

        roomRefreshThread = new Thread(refreshTask);
        roomRefreshThread.setName("room-availability-thread");
        roomRefreshThread.setDaemon(true);
        roomRefreshThread.start();
    }

    private void stopRoomAvailabilityThread() {
        keepRefreshingRooms = false;

        if (roomRefreshThread != null && roomRefreshThread.isAlive()) {
            roomRefreshThread.interrupt();
        }
    }

    @FXML
    private void refreshRooms() {
        ArrayList<Room> snapshot;

        synchronized (Database.class) {
            snapshot = new ArrayList<>(Database.getAllRooms());
        }

        displayRoomsFromSnapshot(snapshot, "Manual refresh complete.");
    }

    private void displayRoomsFromSnapshot(ArrayList<Room> snapshot, String messagePrefix) {
        RoomType selectedType = roomTypeComboBox.getValue();
        AmenityType selectedAmenity = amenityComboBox.getValue();

        Double maxPrice = null;
        if (maxPriceField.getText() != null && !maxPriceField.getText().trim().isEmpty()) {
            try {
                maxPrice = UiUtil.parseDouble(maxPriceField.getText(), "Max price");
            } catch (Exception e) {
                statusLabel.setText(e.getMessage());
                return;
            }
        }

        double guestBalance = SessionContext.currentGuest.getBalance();

        ArrayList<Room> filtered = new ArrayList<>();

        for (Room room : snapshot) {
            if (availableOnlyCheckBox.isSelected() && !room.isAvailable()) continue;
            if (selectedType != null && room.getRoomType() != selectedType) continue;
            if (selectedAmenity != null && !hasAmenity(room, selectedAmenity)) continue;
            if (maxPrice != null && room.getRoomType().getBasePricePerNight() > maxPrice) continue;
            if (affordableOnlyCheckBox.isSelected() && !room.getRoomType().isAffordable(guestBalance)) continue;

            filtered.add(room);
        }

        roomsTable.setItems(FXCollections.observableArrayList(filtered));
        statusLabel.setText(messagePrefix + " " + filtered.size() + " room(s) found.");
    }

    @FXML
    private void handleBookSelected() {
        Room selected = roomsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Please select a room first.");
            return;
        }

        if (!selected.isAvailable()) {
            statusLabel.setText("Selected room is not available.");
            return;
        }

        stopRoomAvailabilityThread();
        SessionContext.selectedRoom = selected;
        ScreenNavigator.goTo("RoomBooking.fxml");
    }

    @FXML
    private void handleClearFilters() {
        roomTypeComboBox.setValue(null);
        amenityComboBox.setValue(null);
        maxPriceField.clear();
        availableOnlyCheckBox.setSelected(true);
        affordableOnlyCheckBox.setSelected(false);
        refreshRooms();
    }

    @FXML
    private void handleBack() {
        stopRoomAvailabilityThread();
        ScreenNavigator.goTo("GuestDashboard.fxml");
    }

    private boolean hasAmenity(Room room, AmenityType amenityType) {
        for (Amenity amenity : room.getRoomType().getAmenities()) {
            if (amenity.getType() == amenityType) return true;
        }
        return false;
    }

    private String formatAmenities(RoomType roomType) {
        StringBuilder sb = new StringBuilder();
        for (Amenity amenity : roomType.getAmenities()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(amenity.getName());
        }
        return sb.length() == 0 ? "None" : sb.toString();
    }
}
