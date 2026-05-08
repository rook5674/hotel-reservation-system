import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GuestRegistrationScreen extends Application {
    private TextField nameField;
    private TextField emailField;
    private PasswordField passwordField;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Guest Registration");

        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f0f0f0;");

        Label titleLabel = new Label("Guest Registration");
        titleLabel.setStyle("-fx-font-size: 22; -fx-font-weight: bold;");

        Label nameLabel = new Label("Name:");
        nameField = new TextField();
        nameField.setPromptText("Enter your name");
        nameField.setPrefWidth(300);

        Label emailLabel = new Label("Email:");
        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setPrefWidth(300);

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Create a password");
        passwordField.setPrefWidth(300);

        Button registerButton = new Button("Register");
        registerButton.setPrefWidth(300);
        registerButton.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        registerButton.setOnAction(e -> handleRegister());

        statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: green;");

        root.getChildren().addAll(
                titleLabel,
                new Separator(),
                nameLabel,
                nameField,
                emailLabel,
                emailField,
                passwordLabel,
                passwordField,
                registerButton,
                statusLabel
        );

        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleRegister() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Error: Please fill in all fields");
            statusLabel.setStyle("-fx-text-fill: red;");
        } else {
            // TODO: Implement registration logic
            statusLabel.setText("Registration successful for: " + name);
            statusLabel.setStyle("-fx-text-fill: green;");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}