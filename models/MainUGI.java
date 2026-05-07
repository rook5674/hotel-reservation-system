package models;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainUGI extends Application {
    
    public static Stage primaryStage;

@Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        Parent root = FXMLLoader.load(getClass().getResource("../Screens/GuestDashboard.fxml"));
        Scene scene = new Scene(root, 900, 600); 
        
        primaryStage.setTitle("CSE241 Hotel Reservation System");
        primaryStage.setScene(scene);
        
        primaryStage.setResizable(true); 
        primaryStage.setMinWidth(800);   
        primaryStage.setMinHeight(500);  
        
        primaryStage.show();
    }

    public static void main(String[] args) {
        models.Database.initializeDummyData(); 
        
        launch(args); 
    }
}