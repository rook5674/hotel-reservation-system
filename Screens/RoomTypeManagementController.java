package Screens;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.*;

public class RoomTypeManagementController {
    @FXML private TextField idField;
    @FXML private TextField typeNameField;
    @FXML private TextField maxOccupancyField;
    @FXML private TextField basePriceField;
    @FXML private Label statusLabel;

    @FXML private TableView<RoomType> roomTypesTable;
    @FXML private TableColumn<RoomType, Integer> idCol;
    @FXML private TableColumn<RoomType, String> nameCol;
    @FXML private TableColumn<RoomType, Integer> occupancyCol;
    @FXML private TableColumn<RoomType, Double> priceCol;

    @FXML
    private void initialize() {
        if (!(SessionContext.currentStaff instanceof Admin)) {
            ScreenNavigator.goTo("Login.fxml");
            return;
        }

        idCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()).asObject());
        nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTypeName()));
        occupancyCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getMaxOccupancy()).asObject());
        priceCol.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getBasePricePerNight()).asObject());

        roomTypesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, rt) -> {
            if (rt != null) {
                idField.setText(String.valueOf(rt.getId()));
                typeNameField.setText(rt.getTypeName());
                maxOccupancyField.setText(String.valueOf(rt.getMaxOccupancy()));
                basePriceField.setText(String.valueOf(rt.getBasePricePerNight()));
            }
        });

        refreshRoomTypes();
    }

    @FXML
    private void handleCreate() {
        try {
            int id = UiUtil.parseInt(idField.getText(), "ID");
            String typeName = typeNameField.getText().trim();
            int maxOccupancy = UiUtil.parseInt(maxOccupancyField.getText(), "Max occupancy");
            double basePrice = UiUtil.parseDouble(basePriceField.getText(), "Base price");

            Admin admin = (Admin) SessionContext.currentStaff;
            admin.createRoomType(typeName, basePrice, id, maxOccupancy);
            refreshRoomTypes();
            statusLabel.setText("Room type created.");
        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleUpdatePrice() {
        try {
            String typeName = typeNameField.getText().trim();
            double basePrice = UiUtil.parseDouble(basePriceField.getText(), "Base price");

            Admin admin = (Admin) SessionContext.currentStaff;
            if (admin.updateRoomTypePrice(typeName, basePrice)) {
                refreshRoomTypes();
                statusLabel.setText("Room type price updated.");
            } else {
                statusLabel.setText("Room type not found.");
            }
        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        try {
            RoomType selected = roomTypesTable.getSelectionModel().getSelectedItem();
            String typeName = selected != null ? selected.getTypeName() : typeNameField.getText().trim();

            Admin admin = (Admin) SessionContext.currentStaff;
            if (admin.deleteRoomType(typeName)) {
                refreshRoomTypes();
                statusLabel.setText("Room type deleted.");
            } else {
                statusLabel.setText("Room type not found.");
            }
        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void refreshRoomTypes() {
        roomTypesTable.setItems(FXCollections.observableArrayList(Database.getAllRoomTypes()));
    }

    @FXML
    private void handleBack() {
        ScreenNavigator.goTo("AdminDashboard.fxml");
    }
}
