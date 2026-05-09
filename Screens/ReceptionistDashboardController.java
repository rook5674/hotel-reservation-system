package Screens;

public class ReceptionistDashboardController {
    @javafx.fxml.FXML private void goToSchedule() { ScreenNavigator.goTo("ScheduleView.fxml"); }
    @javafx.fxml.FXML private void goToCheckInOut() { ScreenNavigator.goTo("CheckInOut.fxml"); }
    @javafx.fxml.FXML private void goToOverview() { ScreenNavigator.goTo("ReceptionistGlobalOverview.fxml"); }

    @javafx.fxml.FXML
    private void handleLogout() {
        SessionContext.logout();
        ScreenNavigator.goTo("RoleSelection.fxml");
    }
}
