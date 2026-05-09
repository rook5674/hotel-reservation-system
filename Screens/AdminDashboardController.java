package Screens;

public class AdminDashboardController {
    @javafx.fxml.FXML private void goToSchedule() { ScreenNavigator.goTo("ScheduleView.fxml"); }
    @javafx.fxml.FXML private void goToRooms() { ScreenNavigator.goTo("RoomManagement.fxml"); }
    @javafx.fxml.FXML private void goToRoomTypes() { ScreenNavigator.goTo("RoomTypeManagement.fxml"); }
    @javafx.fxml.FXML private void goToAmenities() { ScreenNavigator.goTo("AmenityManagement.fxml"); }
    @javafx.fxml.FXML private void goToUsers() { ScreenNavigator.goTo("UserCredentialsManagement.fxml"); }
    @javafx.fxml.FXML private void goToOverview() { ScreenNavigator.goTo("AdminGlobalOverview.fxml"); }

    @javafx.fxml.FXML
    private void handleLogout() {
        SessionContext.logout();
        ScreenNavigator.goTo("RoleSelection.fxml");
    }
}
