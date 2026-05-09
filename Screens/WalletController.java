package Screens;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.Guest;

public class WalletController {
    @FXML private Label balanceLabel;
    @FXML private TextField newBalanceField;
    @FXML private Label statusLabel;

    private Guest guest;

    @FXML
    private void initialize() {
        guest = SessionContext.currentGuest;
        if (guest == null) {
            ScreenNavigator.goTo("Login.fxml");
            return;
        }
        refreshBalance();
    }

    private void refreshBalance() {
        double balance = guest.getBalance(guest.getUserName(), SessionContext.currentPassword);
        balanceLabel.setText("Current Balance: $" + String.format("%.2f", balance));
    }

    @FXML
    private void handleUpdateBalance() {
        try {
            double newBalance = UiUtil.parseDouble(newBalanceField.getText(), "Balance");
            if (newBalance < 0) {
                statusLabel.setText("Balance cannot be negative.");
                return;
            }

            boolean updated = guest.setBalance(guest.getUserName(), SessionContext.currentPassword, newBalance);
            if (updated) {
                refreshBalance();
                statusLabel.setText("Balance updated successfully.");
            } else {
                statusLabel.setText("Could not update balance. Please log in again.");
            }
        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        ScreenNavigator.goTo("GuestDashboard.fxml");
    }
}
