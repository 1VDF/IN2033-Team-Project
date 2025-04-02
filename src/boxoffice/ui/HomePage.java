package boxoffice.ui;

import boxoffice.BoxOfficeManager;
import boxoffice.models.Performance;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends VBox {

    private final BoxOfficeManager boxOfficeManager;

    public HomePage(BoxOfficeManager boxOfficeManager) {
        this.boxOfficeManager = boxOfficeManager;

        // Title for the HomePage
        Text title = new Text("Box Office System");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        title.setFill(Color.web("#2C3E50"));

        HBox buttonLayout = new HBox(30);
        buttonLayout.setAlignment(Pos.CENTER);

        // Button for Ticket Sales
        Button ticketSalesButton = createStyledButton("Ticket Sales");
        ticketSalesButton.setOnAction(e -> showTicketSalesPage());

        // Button for Guest Check-in
        Button guestCheckinButton = createStyledButton("Guest Check-in");
        guestCheckinButton.setOnAction(e -> showGuestCheckinPage());

        // Button for Group Booking
        Button groupBookingButton = createStyledButton("Group Booking");
        groupBookingButton.setOnAction(e -> showGroupBookingsPage());

        // Button for Refunds
        Button refundsButton = createStyledButton("Refunds");
        refundsButton.setOnAction(e -> showRefundsPage());

        // Button for Reports
        Button reportsButton = createStyledButton("Reports");
        reportsButton.setOnAction(e -> showReportsPage());

        buttonLayout.getChildren().addAll(
                ticketSalesButton,
                guestCheckinButton,
                groupBookingButton,
                refundsButton,
                reportsButton
        );

        VBox layout = new VBox(40);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #ECF0F1; -fx-padding: 40px;");


        layout.getChildren().addAll(
                title,
                buttonLayout
        );

        // Loading upcoming performances from CSV file and displaying them
        List<Performance> performances = loadPerformancesFromFile();
        if (performances != null && !performances.isEmpty()) {
            VBox performanceList = new VBox(10);
            Text performanceTitle = new Text("Upcoming Performances");
            performanceTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            performanceTitle.setFill(Color.web("#2C3E50"));
            performanceList.getChildren().add(performanceTitle);

            TableView<Performance> tableView = new TableView<>();
            tableView.setPrefWidth(600);

            // Columns for the TableView
            TableColumn<Performance, String> nameColumn = new TableColumn<>("Performance Name");
            nameColumn.setCellValueFactory(cellData -> cellData.getValue().performanceNameProperty());

            TableColumn<Performance, String> dateColumn = new TableColumn<>("Date");
            dateColumn.setCellValueFactory(cellData -> cellData.getValue().performanceDateProperty());

            TableColumn<Performance, String> timeColumn = new TableColumn<>("Time");
            timeColumn.setCellValueFactory(cellData -> cellData.getValue().performanceTimeProperty());

            TableColumn<Performance, String> locationColumn = new TableColumn<>("Venue");
            locationColumn.setCellValueFactory(cellData -> cellData.getValue().performanceLocationProperty());

            tableView.getColumns().add(nameColumn);
            tableView.getColumns().add(dateColumn);
            tableView.getColumns().add(timeColumn);
            tableView.getColumns().add(locationColumn);

            tableView.getItems().addAll(performances);

            // scrolling capability
            ScrollPane scrollPane = new ScrollPane(tableView);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefHeight(200);

            performanceList.getChildren().add(scrollPane);
            layout.getChildren().add(performanceList);
        }

        HBox bottomButtons = new HBox(20);
        bottomButtons.setStyle("-fx-padding: 20px 0px 0px 0px;");

// Manage Users Button with Icon
        Button manageUsersButton = createStyledBottomButton("Manage Users", "file:src/boxoffice/images/manage-users.png");
        manageUsersButton.setMaxWidth(150);
        manageUsersButton.setMinWidth(150);
        manageUsersButton.setMaxHeight(50);
        manageUsersButton.setContentDisplay(ContentDisplay.CENTER);

// Logout Button with Icon
        Button logoutButton = createStyledBottomButton("Logout", "file:src/boxoffice/images/logout.png");
        logoutButton.setMaxWidth(150);
        logoutButton.setMinWidth(150);
        logoutButton.setMaxHeight(50);
        logoutButton.setContentDisplay(ContentDisplay.CENTER);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        bottomButtons.getChildren().addAll(
                manageUsersButton,
                spacer,
                logoutButton
        );

// Adding bottomButtons to the layout
        layout.getChildren().add(bottomButtons);


        this.getChildren().add(layout);
    }

    // Helper method to create a stylish, round button with gradient and hover effect
    private Button createStyledButton(String buttonText) {
        Button button = new Button(buttonText);

        // Set button style: gradient background, round corners, and other styling
        button.setStyle("-fx-font-size: 16px; -fx-padding: 20px; -fx-background-color: linear-gradient(to right, #4CAF50, #81C784); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 50%;");

        button.setMinWidth(150);
        button.setMinHeight(150);
        button.setMaxWidth(150);
        button.setMaxHeight(150);
        button.setShape(new Circle(75));

        // Hover effect to change button style on mouse enter
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 16px; -fx-padding: 20px; -fx-background-color: linear-gradient(to right, #388E3C, #66BB6A); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 50%;"));

        // Reset to original style on mouse exit
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 16px; -fx-padding: 20px; -fx-background-color: linear-gradient(to right, #4CAF50, #81C784); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 50%;"));

        return button;
    }


    // Helper method to create bottom buttons (Manage Users and Logout) with icons
    private Button createStyledBottomButton(String buttonText, String imagePath) {
        Button button = new Button(buttonText);
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
        button.setGraphic(imageView);

        button.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-color: linear-gradient(to right, #3498DB, #5DADE2); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px; -fx-alignment: center;");
        // Hover effect to change button style on mouse enter
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-color: linear-gradient(to right, #2980B9, #5499C7); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px; -fx-alignment: center;"));
        // Reset to original style on mouse exit
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-color: linear-gradient(to right, #3498DB, #5DADE2); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5px; -fx-alignment: center;"));

        return button;
    }



    private void showTicketSalesPage() {
        PerformancePage performancePage = new PerformancePage();
        this.getScene().setRoot(performancePage);
    }

    private void showGuestCheckinPage() {
        GuestCheckInPage guestCheckinPage = new GuestCheckInPage(boxOfficeManager);
        this.getScene().setRoot(guestCheckinPage);
    }

    private void showGroupBookingsPage() {
        GroupBookingsPage groupBookingsPage = new GroupBookingsPage(boxOfficeManager);
        this.getScene().setRoot(groupBookingsPage);
    }

    private void showRefundsPage() {
        RefundsPage refundsPage = new RefundsPage(boxOfficeManager);
        this.getScene().setRoot(refundsPage);
    }

    private void showReportsPage() {
        ReportsPage reportsPage = new ReportsPage(boxOfficeManager);
        this.getScene().setRoot(reportsPage);
    }

    private void showManageUsersPage() {
        // Navigate to the Manage Users page
    }
    private void logout() {
        // Handle logout action
    }

    // Method to load performances from the CSV file
    private List<Performance> loadPerformancesFromFile() {
        List<Performance> performances = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/boxoffice/data/performances.csv"))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 4) {
                    String name = details[0].replace("\"", "");
                    String date = details[1].replace("\"", "");
                    String time = details[2].replace("\"", "");
                    String location = details[3].replace("\"", "");
                    performances.add(new Performance(name, date, time, location));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return performances;
    }
}







