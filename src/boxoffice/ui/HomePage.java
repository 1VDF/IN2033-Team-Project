
package boxoffice.ui;

import boxoffice.BoxOfficeManager;
import boxoffice.database.Performance;
import boxoffice.models.PerformanceRepository;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;

import java.sql.SQLException;
import java.util.List;

public class HomePage extends VBox {

    private final BoxOfficeManager boxOfficeManager;
    private final PerformanceRepository performanceRepository;

    public HomePage(BoxOfficeManager boxOfficeManager) {
        this.boxOfficeManager = boxOfficeManager;
        this.performanceRepository = new PerformanceRepository();

        // Page Title
        Text title = new Text("Box Office System");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        title.setFill(Color.web("#2C3E50"));

        Region topButtonSpacer = new Region();
        VBox.setVgrow(topButtonSpacer, Priority.ALWAYS);

        HBox buttonLayout = new HBox(30);
        buttonLayout.setAlignment(Pos.CENTER);

        Button ticketSalesButton = createStyledButton("Ticket Sales", () -> showTicketSalesPage());
        Button guestCheckinButton = createStyledButton("Guest Check-in", () -> showGuestCheckinPage());
        Button groupBookingButton = createStyledButton("Group Booking", () -> showGroupBookingsPage());
        Button refundsButton = createStyledButton("Refunds", () -> showRefundsPage());
        Button reportsButton = createStyledButton("Reports", () -> showReportsPage());

        buttonLayout.getChildren().addAll(ticketSalesButton, guestCheckinButton, groupBookingButton, refundsButton, reportsButton);

        VBox layout = new VBox(30);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setStyle("-fx-background-color: #ECF0F1; -fx-padding: 20px;");

        layout.setPrefHeight(800);
        layout.getChildren().addAll(title, topButtonSpacer, buttonLayout);

        Region gapBetweenButtonsAndTable = new Region();
        VBox.setVgrow(gapBetweenButtonsAndTable, Priority.ALWAYS);

        layout.getChildren().add(gapBetweenButtonsAndTable);
        layout.getChildren().add(getPerformanceTable());

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Bottom Buttons: Manager User and Logout
        HBox bottomButtons = new HBox(20);
        bottomButtons.setStyle("-fx-padding: 20px 50px 20px 50px;");
        bottomButtons.setAlignment(Pos.CENTER);

        Button manageUsersButton = createStyledBottomButton("Manage Users", "file:src/boxoffice/images/manage-users.png");
        Button logoutButton = createStyledBottomButton("Logout", "file:src/boxoffice/images/logout.png");

        Region bottomSpacer = new Region();
        HBox.setHgrow(bottomSpacer, Priority.ALWAYS);

        bottomButtons.getChildren().addAll(manageUsersButton, bottomSpacer, logoutButton);

        layout.getChildren().addAll(spacer, bottomButtons);

        this.getChildren().add(layout);
    }

    private VBox getPerformanceTable() {
        VBox performanceList = new VBox(10);
        performanceList.setAlignment(Pos.CENTER);

        Text performanceTitle = new Text("Upcoming Performances");
        performanceTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        performanceTitle.setFill(Color.web("#2C3E50"));

        TableView<Performance> tableView = new TableView<>();
        tableView.setPrefWidth(750);
        tableView.setMaxWidth(1000);
        tableView.setPrefHeight(250);

        TableColumn<Performance, String> nameColumn = new TableColumn<>("Performance Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        nameColumn.setMinWidth(150);
        nameColumn.setPrefWidth(225);

        TableColumn<Performance, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        dateColumn.setMinWidth(110);
        dateColumn.setPrefWidth(110);

        TableColumn<Performance, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());
        timeColumn.setMinWidth(110);
        timeColumn.setPrefWidth(110);

        TableColumn<Performance, String> durationColumn = new TableColumn<>("Duration (min)");
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().durationMinutesProperty().asString());
        durationColumn.setMinWidth(110);
        durationColumn.setPrefWidth(110);

        TableColumn<Performance, String> venueColumn = new TableColumn<>("Venue");
        venueColumn.setCellValueFactory(cellData -> cellData.getValue().venueNameProperty()); // Ensure this exists in `Performance.java`
        venueColumn.setMinWidth(110);
        venueColumn.setPrefWidth(110);

        // New column for Description
        TableColumn<Performance, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty()); // Assuming you have descriptionProperty in Performance class
        descriptionColumn.setMinWidth(150);
        descriptionColumn.setPrefWidth(332);

        // Add columns to the table
        tableView.getColumns().addAll(nameColumn, dateColumn, timeColumn, durationColumn, venueColumn, descriptionColumn);

        try {
            List<Performance> performances = performanceRepository.getAllPerformances();
            tableView.getItems().addAll(performances);
        } catch (SQLException e) {
            e.printStackTrace();
            Label errorLabel = new Label("Error loading performances. Please try again.");
            errorLabel.setTextFill(Color.RED);
            performanceList.getChildren().add(errorLabel);
        }

        performanceList.getChildren().addAll(performanceTitle, tableView);
        return performanceList;
    }


    private Button createStyledButton(String buttonText, Runnable action) {
        Button button = new Button(buttonText);
        button.setStyle("-fx-font-size: 18px; -fx-padding: 30px; -fx-background-color: linear-gradient(to right, #4CAF50, #81C784); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 50%;");
        button.setMinWidth(180);
        button.setMinHeight(180);
        button.setMaxWidth(180);
        button.setMaxHeight(180);

        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 18px; -fx-padding: 30px; -fx-background-color: linear-gradient(to right, #388E3C, #66BB6A); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 50%;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 18px; -fx-padding: 30px; -fx-background-color: linear-gradient(to right, #4CAF50, #81C784); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 50%;"));

        button.setOnAction(e -> action.run());
        return button;
    }

    private Button createStyledBottomButton(String buttonText, String imagePath) {
        Button button = new Button(buttonText);

        // Create an ImageView for the icon
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(40);  // Increase icon size
        imageView.setFitWidth(40);

        button.setGraphic(imageView);

        button.setStyle("-fx-font-size: 16px; -fx-padding: 15px 40px; -fx-background-color: linear-gradient(to right, #3498DB, #5DADE2); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px;");

        button.setMinWidth(180);
        button.setMinHeight(70);
        button.setMaxWidth(180);
        button.setMaxHeight(70);

        button.setAlignment(Pos.CENTER);
        button.setContentDisplay(ContentDisplay.CENTER);

        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 16px; -fx-padding: 15px 40px; -fx-background-color: linear-gradient(to right, #2980B9, #5499C7); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 16px; -fx-padding: 15px 40px; -fx-background-color: linear-gradient(to right, #3498DB, #5DADE2); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px;"));

        return button;
    }


    private void showTicketSalesPage() {
        this.getScene().setRoot(new PerformancePage());
    }

    private void showGuestCheckinPage() {
        this.getScene().setRoot(new GuestCheckInPage(boxOfficeManager));
    }

    private void showGroupBookingsPage() {
        this.getScene().setRoot(new GroupBookingsPage(boxOfficeManager));
    }

    private void showRefundsPage() {
        this.getScene().setRoot(new RefundsPage(boxOfficeManager));
    }

    private void showReportsPage() {
        this.getScene().setRoot(new ReportsPage(boxOfficeManager));
    }
}
