package boxoffice.ui;

import boxoffice.Main;
import boxoffice.database.Staff;
import boxoffice.models.StaffRepository;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginPage extends VBox {
    private final TextField emailField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Button loginButton = new Button("LOGIN");
    private final Label statusLabel = new Label();
    private final Stage stage;
    private final Main main;

    public LoginPage(Stage stage, Main main) {
        this.stage = stage;
        this.main = main;
        setupUI();
        stage.getIcons().add(new Image("file:src/boxoffice/images/logo.png"));
    }

    private void setupUI() {
        setSpacing(20);
        setPadding(new Insets(-15, 40, 40, 40));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: linear-gradient(to bottom right, #2c3e50, #3498db);");
        setPrefSize(500, 550);


        ImageView logo = new ImageView(new Image("file:src/boxoffice/images/logo.png"));
        logo.setFitHeight(70);
        logo.setFitWidth(70);
        logo.setPreserveRatio(true);

        Text title = new Text("BOX OFFICE SYSTEM");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setFill(Color.WHITE);

        VBox formContainer = new VBox(15);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(25, 40, 35, 40));
        formContainer.setStyle("-fx-background-color: rgba(255,255,255,0.95); " +
                "-fx-background-radius: 8; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 2, 2);");
        formContainer.setMaxWidth(400);
        formContainer.setMinHeight(300);

        GridPane form = new GridPane();
        form.setAlignment(Pos.CENTER);
        form.setHgap(15);
        form.setVgap(20);
        form.setPadding(new Insets(10));

        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-min-width: 80;");
        emailField.setPromptText("your.email@example.com");
        emailField.setStyle("-fx-pref-width: 200px; -fx-padding: 8px; -fx-font-size: 14px;");
        form.add(emailLabel, 0, 0);
        form.add(emailField, 1, 0);

        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-min-width: 80;");
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle("-fx-pref-width: 200px; -fx-padding: 8px; -fx-font-size: 14px;");
        form.add(passwordLabel, 0, 1);
        form.add(passwordField, 1, 1);

        loginButton.setStyle("-fx-background-color: #2980b9; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 10px 30px; " +
                "-fx-background-radius: 5px;");
        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: #3498db; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px 30px; " +
                "-fx-background-radius: 5px;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-background-color: #2980b9; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10px 30px; " +
                "-fx-background-radius: 5px;"));
        loginButton.setOnAction(e -> handleLogin());

        statusLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 13px;");
        statusLabel.setMaxWidth(320);
        statusLabel.setAlignment(Pos.CENTER);
        statusLabel.setWrapText(true);

        Hyperlink forgotPassword = new Hyperlink("Forgot Password?");
        forgotPassword.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");
        forgotPassword.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Password Assistance");
            alert.setHeaderText("Contact the Manager");
            alert.setContentText("Please email: BoxOffice25@gmail.com\nfor password assistance.");
            alert.showAndWait();
        });

        formContainer.getChildren().addAll(
                form,
                loginButton,
                forgotPassword,
                statusLabel
        );

        getChildren().addAll(
                logo,
                title,
                formContainer
        );
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please enter both email and password");
            return;
        }

        try {
            StaffRepository staffRepo = new StaffRepository();
            Staff staff = staffRepo.getStaffByCredentials(email, password);

            if (staff != null) {
                main.getBoxOfficeManager().setCurrentStaff(staff);
                main.showHomePage();
            } else {
                showError("Invalid email or password");
                animateShake();
            }
        } catch (Exception e) {
            showError("Login failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        statusLabel.setText(message);
    }

    private void animateShake() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(translateXProperty(), 0)),
                new KeyFrame(Duration.millis(50), new KeyValue(translateXProperty(), -10)),
                new KeyFrame(Duration.millis(100), new KeyValue(translateXProperty(), 10)),
                new KeyFrame(Duration.millis(150), new KeyValue(translateXProperty(), -10)),
                new KeyFrame(Duration.millis(200), new KeyValue(translateXProperty(), 10)),
                new KeyFrame(Duration.millis(250), new KeyValue(translateXProperty(), -10)),
                new KeyFrame(Duration.millis(300), new KeyValue(translateXProperty(), 10)),
                new KeyFrame(Duration.millis(350), new KeyValue(translateXProperty(), 0))
        );
        timeline.play();
    }
}