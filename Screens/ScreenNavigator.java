package Screens;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import models.MainGUI;

import java.net.URL;

public final class ScreenNavigator {
    private static final double DEFAULT_WIDTH = 1000;
    private static final double DEFAULT_HEIGHT = 650;

    private ScreenNavigator() {}

    public static void goTo(String fxmlFile) {
        try {
            URL fxmlUrl = ScreenNavigator.class.getResource(fxmlFile);
            if (fxmlUrl == null) {
                throw new IllegalArgumentException("FXML file not found in Screens package: " + fxmlFile);
            }

            Parent root = FXMLLoader.load(fxmlUrl);
            Parent scaledRoot = ResponsiveScaler.wrap(root);

            double width = DEFAULT_WIDTH;
            double height = DEFAULT_HEIGHT;

            if (MainGUI.primaryStage != null && MainGUI.primaryStage.getScene() != null) {
                width = Math.max(MainGUI.primaryStage.getScene().getWidth(), DEFAULT_WIDTH);
                height = Math.max(MainGUI.primaryStage.getScene().getHeight(), DEFAULT_HEIGHT);
            }

            Scene scene = new Scene(scaledRoot, width, height);

            URL cssUrl = ScreenNavigator.class.getResource("AppStyles.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            MainGUI.primaryStage.setScene(scene);
            MainGUI.primaryStage.setMinWidth(720);
            MainGUI.primaryStage.setMinHeight(480);
            MainGUI.primaryStage.setResizable(true);
            MainGUI.primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            UiUtil.showError(
                    "Navigation Error",
                    "Could not open screen: " + fxmlFile + "\n\nReason: " + e.getMessage()
            );
        }
    }
}
