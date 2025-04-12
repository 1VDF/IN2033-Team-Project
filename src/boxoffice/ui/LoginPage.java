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

/**
 * The LoginPage class represents the login interface for staff members.
 * It provides a way for staff members to authenticate by entering their email and password.
 */
public class LoginPage extends VBox {
    private final StaffRepository staffRepository;
    private final TextField emailField;
    private final PasswordField passwordField;

    /**
     * Initialises the LoginPage UI and sets up the required components for staff login.
     */
    public LoginPage() {
        this.staffRepository = new StaffRepository();
        this.emailField = new TextField();
        this.passwordField = new PasswordField();
        initialiseUI();
    }

    /**
     * Initialises the user interface for the LoginPage.
     * This includes setting up labels, fields for email and password, and the login button.
     */
    private void initialiseUI() {
        // Setup the logo image
        Image logoImage = new Image("boxoffice/data/lancaster_logo.png");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(190);
        logoView.setFitWidth(190);
        logoView.setPreserveRatio(true);
        logoView.setLayoutX(-10);
        logoView.setLayoutY(-100);

        // Setup the title label
        Label titleLabel = new Label("Staff Login");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        titleLabel.setStyle("-fx-text-fill: #ffffff;");
        titleLabel.setLayoutX(520);
        titleLabel.setLayoutY(200);

        // Setup the email label and text field
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

        // Setup the password label and password field
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

        // Setup the login button and action
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20; -fx-background-color: #2ecc40; " +
                "-fx-text-fill: white; -fx-background-radius: 5;");
        loginButton.setLayoutX(560);
        loginButton.setLayoutY(430);
        loginButton.setOnAction(e -> handleLogin());

        // Setup the login pane
        Pane loginPane = new Pane(logoView, titleLabel, emailLabel, emailField, passwordLabel, passwordField, loginButton);

        // Add all components to the LoginPage
        this.getChildren().addAll(loginPane);
        this.setSpacing(15);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #f4f4f4;");
    }

    /**
     * Handles the login action when the login button is pressed.
     * It checks if the entered email and password match the stored credentials.
     * If successful, the staff member is logged in and redirected to the home page.
     */
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        // Validate the email and password inputs
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Input Error", "Please enter both email and password.");
            return;
        }

        try {
            // Authenticate the staff member using the entered email and password
            Staff staff = staffRepository.authenticate(email, password);
            if (staff != null) {
                if (password.equals(staff.getPassword())) {
                    // Set the logged-in staff session and navigate to the home page
                    Session.getInstance().setCurrentStaff(staff);
                    HomePage homePage = new HomePage();
                    homePage.setStyle("-fx-background-color: linear-gradient(to bottom, #122023 0%, #122023 20%, #468585 100%);");
                    this.getScene().setRoot(homePage);

                    // Clear the fields after successful login
                    emailField.clear();
                    passwordField.clear();
                } else {
                    showAlert("Login Failed", "Invalid password.");
                }
            } else {
                showAlert("Login Failed", "No account found with that email");
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Could not connect to the database: " + e.getMessage());
        }
    }

    /**
     * Displays an alert with the specified title and message.
     *
     * @param title the title of the alert
     * @param message the message content of the alert
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
