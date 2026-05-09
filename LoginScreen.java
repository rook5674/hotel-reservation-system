import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen extends Application {
    private TextField usernameField;
    private PasswordField passwordField;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hotel Reservation System - Login");

        // Create main layout
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f0f0f0;");

        // Title
        Label titleLabel = new Label("Hotel Reservation System");
        titleLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        // Username field
        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setPrefWidth(300);

        // Password field
        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefWidth(300);

        // Login button
        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(300);
        loginButton.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        loginButton.setOnAction(e -> handleLogin());

        // Status label
        statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: green;");

        // Add components to root
        root.getChildren().addAll(
                titleLabel,
                new Separator(),
                usernameLabel,
                usernameField,
                passwordLabel,
                passwordField,
                loginButton,
                statusLabel
        );

        // Create scene
        Scene scene = new Scene(root, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Error: Please enter both username and password");
            statusLabel.setStyle("-fx-text-fill: red;");
        } else {
            // TODO: Implement authentication logic
            statusLabel.setText("Login successful for: " + username);
            statusLabel.setStyle("-fx-text-fill: green;");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
