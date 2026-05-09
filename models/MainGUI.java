package models;

import Screens.ResponsiveScaler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainGUI extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        Database.initializeDummyData();

        Parent root = FXMLLoader.load(getClass().getResource("../Screens/RoleSelection.fxml"));
        Parent scaledRoot = ResponsiveScaler.wrap(root);

        Scene scene = new Scene(scaledRoot, 1000, 650);

        var css = getClass().getResource("../Screens/AppStyles.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        primaryStage.setTitle("CSE241 Hotel Reservation System");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(720);
        primaryStage.setMinHeight(480);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
