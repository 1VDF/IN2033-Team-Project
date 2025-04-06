package boxoffice.ui;

import boxoffice.database.Staff;
import boxoffice.models.StaffRepository;
import boxoffice.models.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.sql.SQLException;

public class LoginPage extends VBox {
    private final StaffRepository staffRepository;
    private final TextField emailField;
    private final PasswordField passwordField;

    public LoginPage() {
        this.staffRepository = new StaffRepository();
        this.emailField = new TextField();
        this.passwordField = new PasswordField();
        initializeUI();
    }

    private void initializeUI() {

        Image logoImage = new Image("boxoffice/data/lancaster_logo.png");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(190);
        logoView.setFitWidth(190);
        logoView.setPreserveRatio(true);
        logoView.setLayoutX(-10);
        logoView.setLayoutY(-100);

        Label titleLabel = new Label("Staff Login");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        titleLabel.setStyle("-fx-text-fill: #ffffff;");
        titleLabel.setLayoutX(520);
        titleLabel.setLayoutY(200);

        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #ffffff;");
        emailLabel.setLayoutX(450);
        emailLabel.setLayoutY(250);

        emailField.setPromptText("Enter your email");
        emailField.setPrefWidth(300);
        emailField.setStyle("-fx-font-size: 14px; -fx-padding: 8; -fx-border-radius: 5; " +
                "-fx-background-radius: 5; -fx-background-color: #ffffff;");
        emailField.setLayoutX(450);
        emailField.setLayoutY(280);

        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #ffffff;");
        passwordLabel.setLayoutX(450);
        passwordLabel.setLayoutY(340);

        passwordField.setPromptText("Enter your password");
        passwordField.setPrefWidth(300);
        passwordField.setStyle("-fx-font-size: 14px; -fx-padding: 8; -fx-border-radius: 5; " +
                "-fx-background-radius: 5; -fx-background-color: #ffffff;");
        passwordField.setLayoutX(450);
        passwordField.setLayoutY(370);

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20; -fx-background-color: #2ecc40; " +
                "-fx-text-fill: white; -fx-background-radius: 5;");
        loginButton.setLayoutX(560);
        loginButton.setLayoutY(430);
        loginButton.setOnAction(e -> handleLogin());

        Pane loginPane = new Pane(logoView,titleLabel,emailLabel,emailField,passwordLabel,passwordField,loginButton);

        // Layout
        this.getChildren().addAll(
                loginPane
        );
        this.setSpacing(15);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #f4f4f4;");
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Input Error", "Please enter both email and password.");
            return;
        }

        try {
            Staff staff = staffRepository.authenticate(email, password);
            if (staff != null) {
                // Store staff in session
                Session.getInstance().setCurrentStaff(staff);

                // Navigate to PerformancePage
                HomePage homePage = new HomePage();
                homePage.setStyle("-fx-background-color: linear-gradient(to bottom, #122023 0%, #122023 20%, #468585 100%);");
                this.getScene().setRoot(homePage);

                // Clear fields
                emailField.clear();
                passwordField.clear();
            } else {
                showAlert("Login Failed", "Invalid email or password.");
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Could not connect to the database: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
