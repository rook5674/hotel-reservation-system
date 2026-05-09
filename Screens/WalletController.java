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
            ScreenNavigator.goTo("RoleSelection.fxml");
            return;
        }
        refreshBalance();
    }

    private void refreshBalance() {
        balanceLabel.setText("Current Balance: $" + String.format("%.2f", guest.getBalance()));
    }

    @FXML
    private void handleUpdateBalance() {
        try {
            double newBalance = UiUtil.parseDouble(newBalanceField.getText(), "Balance");
            guest.setBalance(newBalance);
            refreshBalance();
            statusLabel.setText("Balance updated successfully.");
        } catch (Exception e) {
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        ScreenNavigator.goTo("GuestDashboard.fxml");
    }
}
