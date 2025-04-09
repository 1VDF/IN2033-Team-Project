
package boxoffice.ui;

import boxoffice.database.Performance;
import boxoffice.database.Staff;
import boxoffice.models.PerformanceRepository;
import boxoffice.models.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
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
    private final PerformanceRepository performanceRepository;

    public HomePage() {
        this.performanceRepository = new PerformanceRepository();

        Image logoImage = new Image("boxoffice/data/lancaster_logo.png");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(150);
        logoView.setFitWidth(150);
        logoView.setPreserveRatio(true);

        HBox logoContainer = new HBox(logoView);
        logoContainer.setAlignment(Pos.TOP_LEFT);
        logoContainer.setPadding(new Insets(20, 0, 0, 20));

        Text title = new Text("Box Office System");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        title.setFill(Color.WHITE);
        DropShadow shadow = new DropShadow(10, Color.GRAY);
        title.setEffect(shadow);

        Region topButtonSpacer = new Region();
        VBox.setVgrow(topButtonSpacer, Priority.ALWAYS);

        HBox buttonLayout = new HBox(30);
        buttonLayout.setAlignment(Pos.CENTER);

        Button customerButton = createStyledButton("Manage Customers", () -> {
            try {
                showManageCustomersPage();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        Button guestCheckinButton = createStyledButton("Guest Check-in", () -> showGuestCheckinPage());
        Button refundsButton = createStyledButton("Refunds", () -> showRefundsPage());

        if (Session.getInstance().getCurrentStaff().getRole() == Staff.Role.Staff) {
            refundsButton.setTooltip(new Tooltip("Only Managers can access refunds"));
            refundsButton.setDisable(true);
        }else{
            refundsButton.setDisable(false);
        }

        Button reportsButton = createStyledButton("Reports", () -> showReportsPage());

        buttonLayout.getChildren().addAll(customerButton,guestCheckinButton, refundsButton, reportsButton);

        Region gapBetweenButtonsAndTable = new Region();
        VBox.setVgrow(gapBetweenButtonsAndTable, Priority.ALWAYS);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        HBox bottomButtons = new HBox(400);
        bottomButtons.setAlignment(Pos.TOP_CENTER);

        Button manageUsersButton = new Button("Manage Users");
        manageUsersButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20; -fx-background-color: #2ecc40; " +
                "-fx-text-fill: white; -fx-background-radius: 5;");
        manageUsersButton.setOnAction(e -> showManageUsersPage());

        if (Session.getInstance().getCurrentStaff().getRole() == Staff.Role.Staff || Session.getInstance().getCurrentStaff().getRole() == Staff.Role.DeputyManager) {
            manageUsersButton.setTooltip(new Tooltip("Only Managers can access user management"));
            manageUsersButton.setDisable(true);
        }else{
            manageUsersButton.setDisable(false);
        }

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20; -fx-background-color: #2ecc40; " +
                "-fx-text-fill: white; -fx-background-radius: 5;");
        logoutButton.setOnAction(e -> {
            LoginPage loginPage = new LoginPage();
            loginPage.setStyle("-fx-background-color: linear-gradient(to bottom, #122023 0%, #122023 20%, #468585 100%);");
            this.getScene().setRoot(loginPage);
        });

        Button bookTicketButton = new Button("Manage Hall");
        bookTicketButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20; -fx-background-color: #2ecc40; " +
                "-fx-text-fill: white; -fx-background-radius: 5;");

        bottomButtons.getChildren().addAll(manageUsersButton, bookTicketButton, logoutButton);

        this.getChildren().addAll(
                logoContainer,
                title,
                topButtonSpacer,
                buttonLayout,
                gapBetweenButtonsAndTable,
                getPerformanceTable(bookTicketButton),
                spacer,
                bottomButtons
        );

        // Set VBox properties
        this.setAlignment(Pos.TOP_CENTER);
        this.setSpacing(0);
    }

    private VBox getPerformanceTable(Button bookTicketButton) {
        VBox performanceList = new VBox(10);
        performanceList.setAlignment(Pos.CENTER);

        Text performanceTitle = new Text("Upcoming Performances");
        performanceTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        performanceTitle.setFill(Color.WHITE);

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
        venueColumn.setCellValueFactory(cellData -> cellData.getValue().venueNameProperty());
        venueColumn.setMinWidth(110);
        venueColumn.setPrefWidth(110);


        TableColumn<Performance, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        descriptionColumn.setMinWidth(150);
        descriptionColumn.setPrefWidth(332);


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
        bookTicketButton.setOnAction(event -> {
            Performance selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if(selected.getVenueID() == 1) {
                    TicketSalesPage ticketSalesPage = null; // Make sure selected is passed
                    try {
                        ticketSalesPage = new TicketSalesPage(selected);
                        ticketSalesPage.setStyle("-fx-background-color: linear-gradient(to bottom, #122023 0%, #122023 20%, #468585 100%);");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    this.getScene().setRoot(ticketSalesPage);
                }
                else{
                    TicketSalesPageSmall ticketSalesPageSmall = null;
                    try {
                        ticketSalesPageSmall = new TicketSalesPageSmall(selected);
                        ticketSalesPageSmall.setStyle("-fx-background-color: linear-gradient(to bottom, #122023 0%, #122023 20%, #468585 100%);");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    this.getScene().setRoot(ticketSalesPageSmall);
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select a performance first").showAndWait();
            }
        });

        performanceList.getChildren().addAll(performanceTitle, tableView);
        return performanceList;
    }


    private Button createStyledButton(String buttonText, Runnable action) {
        Button button = new Button(buttonText);
        button.setStyle("-fx-font-size: 12px; -fx-padding: 30px; -fx-background-color: linear-gradient(to right, #4CAF50, #81C784); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 50%;");
        button.setMinWidth(180);
        button.setMinHeight(180);
        button.setMaxWidth(180);
        button.setMaxHeight(180);

        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 12px; -fx-padding: 30px; -fx-background-color: linear-gradient(to right, #388E3C, #66BB6A); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 50%;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 12px; -fx-padding: 30px; -fx-background-color: linear-gradient(to right, #4CAF50, #81C784); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 50%;"));

        button.setOnAction(e -> action.run());
        return button;
    }

    private void showTicketSalesPage() {
    }

    private void showGuestCheckinPage() {
        GuestCheckInPage guestCheckInPage = new GuestCheckInPage();
        guestCheckInPage.setStyle("-fx-background-color: linear-gradient(to bottom, #122023 0%, #122023 20%, #468585 100%);");
        this.getScene().setRoot(guestCheckInPage);
    }

    private void showGroupBookingsPage() {
        GroupBookingsPage groupBookingsPage = new GroupBookingsPage();
        groupBookingsPage.setStyle("-fx-background-color: linear-gradient(to bottom, #122023 0%, #122023 20%, #468585 100%);");
        this.getScene().setRoot(groupBookingsPage);
    }

    private void showRefundsPage() {
        RefundsPage refundsPage = new RefundsPage();
        refundsPage.setStyle("-fx-background-color: linear-gradient(to bottom, #122023 0%, #122023 20%, #468585 100%);");
        this.getScene().setRoot(refundsPage);
    }

    private void showReportsPage() {
        ReportsPage reportsPage = new ReportsPage();
        reportsPage.setStyle("-fx-background-color: linear-gradient(to bottom, #122023 0%, #122023 20%, #468585 100%);");
        this.getScene().setRoot(reportsPage);
    }

    private void showManageUsersPage() {
        UserManagementPage userManagementPage = new UserManagementPage();
        userManagementPage.setStyle("-fx-background-color: linear-gradient(to bottom, #122023 0%, #122023 20%, #468585 100%);");
        this.getScene().setRoot(userManagementPage);
    }

    private void showManageCustomersPage() throws SQLException {
        CustomerPage customerPage = new CustomerPage();
        customerPage.setStyle("-fx-background-color: linear-gradient(to bottom, #122023 0%, #122023 20%, #468585 100%);");
        this.getScene().setRoot(customerPage);
    }
}
