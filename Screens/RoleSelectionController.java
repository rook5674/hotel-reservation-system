package Screens;

import javafx.fxml.FXML;

public class RoleSelectionController {

    @FXML
    public void handleGuest() {
        SessionContext.setLoginMode(SessionContext.LoginMode.GUEST);
        ScreenNavigator.goTo("Login.fxml");
    }

    @FXML
    public void handleStaff() {
        SessionContext.setLoginMode(SessionContext.LoginMode.STAFF);
        ScreenNavigator.goTo("Login.fxml");
    }
}
