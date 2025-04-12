package boxoffice.ui;

import boxoffice.database.TicketSale;
import boxoffice.models.TicketSaleRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;

/**
 * The GuestCheckInPage class represents the user interface for guest check-in within the box office system.
 * It allows staff members to search for customer tickets by name and check in selected tickets.
 */
public class GuestCheckInPage extends VBox {

    private final TextField searchField;
    private final Button searchButton;
    private final TableView<TicketSale> ticketsTable;
    private final Button checkInButton;
    private final Label statusLabel;

    /**
     * Constructs a new GuestCheckInPage and initialises its user interface components.
     * This includes the logo, search box, tickets table, status label, and check-in button.
     */
    public GuestCheckInPage() {
        Image logoImage = new Image("boxoffice/data/lancaster_logo.png");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(190);
        logoView.setFitWidth(190);
        logoView.setPreserveRatio(true);

        HBox logoContainer = new HBox(logoView);
        logoContainer.setAlignment(Pos.TOP_LEFT);
        logoContainer.setPadding(new Insets(20, 0, 0, 20));

        searchField = new TextField();
        searchField.setPromptText("Enter customer name...");

        searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchCustomerTickets());

        HBox searchBox = new HBox(10, searchField, searchButton);
        searchBox.setPadding(new Insets(10));
        HBox.setHgrow(searchField, Priority.ALWAYS);

        ticketsTable = new TableView<>();
        setupTableColumns();

        checkInButton = new Button("Check In Selected Ticket");
        checkInButton.setOnAction(e -> checkInTicket());
        checkInButton.setDisable(true);
        checkInButton.setLayoutX(-50);
        checkInButton.setLayoutY(680);
        checkInButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20; -fx-background-color: #2ecc40; " +
                "-fx-text-fill: white; -fx-background-radius: 5;");

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20; -fx-background-color: #2ecc40; " +
                "-fx-text-fill: white; -fx-background-radius: 5;");
        backButton.setLayoutX(650);
        backButton.setLayoutY(680);

        backButton.setOnAction(e -> {
            HomePage homePage = new HomePage();
            homePage.setStyle("-fx-background-color: linear-gradient(to bottom, #122023 0%, #122023 20%, #468585 100%);");
            this.getScene().setRoot(homePage);
        });

        statusLabel = new Label();
        statusLabel.setPadding(new Insets(5, 10, 10, 10));
        statusLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;-fx-text-fill: white;");
        statusLabel.setLayoutX(-50);
        statusLabel.setLayoutY(650);

        HBox bottomBox = new HBox(880, checkInButton, backButton);

        this.getChildren().addAll(logoContainer, searchBox, ticketsTable, statusLabel, bottomBox);
        this.setSpacing(0);
        this.setPadding(new Insets(10));

        ticketsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> checkInButton.setDisable(newSelection == null)
        );
    }

    /**
     * Sets up the columns for the tickets table, including ticket ID, performance ID, seat, price, and checked-in status.
     */
    private void setupTableColumns() {
        TableColumn<TicketSale, Integer> idCol = new TableColumn<>("Ticket ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ticketSaleId"));

        TableColumn<TicketSale, Integer> performanceCol = new TableColumn<>("Performance ID");
        performanceCol.setCellValueFactory(new PropertyValueFactory<>("performanceID"));

        TableColumn<TicketSale, String> seatCol = new TableColumn<>("Seat");
        seatCol.setCellValueFactory(new PropertyValueFactory<>("seatID"));

        TableColumn<TicketSale, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<TicketSale, Boolean> checkedInCol = new TableColumn<>("Checked In");
        checkedInCol.setCellValueFactory(new PropertyValueFactory<>("checkedIn"));

        ticketsTable.getColumns().addAll(idCol, performanceCol, seatCol, priceCol, checkedInCol);
    }

    /**
     * Searches for tickets associated with the given customer name.
     * If no tickets are found, an appropriate message is displayed. If tickets are found, they are displayed in the table.
     */
    private void searchCustomerTickets() {
        String customerName = searchField.getText().trim();

        if (customerName.isEmpty()) {
            statusLabel.setText("Please enter a customer name to search.");
            return;
        }

        try {
            List<TicketSale> customerTickets = TicketSaleRepository.getTicketsByCustomerName(customerName);

            if (customerTickets.isEmpty()) {
                statusLabel.setText("No tickets found for customer: " + customerName);
            } else {
                statusLabel.setText("Found " + customerTickets.size() + " tickets for customer: " + customerName);
            }

            ticketsTable.setItems(FXCollections.observableArrayList(customerTickets));
        } catch (SQLException e) {
            statusLabel.setText("Error accessing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Checks in the selected ticket in the table.
     * If the ticket is already checked in, an appropriate message is displayed.
     * If the check-in is successful, the status label is updated and the ticket is marked as checked in.
     */
    private void checkInTicket() {
        TicketSale selectedTicket = ticketsTable.getSelectionModel().getSelectedItem();

        if (selectedTicket == null) {
            statusLabel.setText("No ticket selected for check-in.");
            return;
        }

        if (selectedTicket.isCheckedIn()) {
            statusLabel.setText("This ticket has already been checked in.");
            return;
        }

        try {
            boolean success = TicketSaleRepository.markAsCheckedIn(selectedTicket.getTicketSaleId());

            if (success) {
                selectedTicket.setCheckedIn(true);
                ticketsTable.refresh();
                statusLabel.setText("Ticket #" + selectedTicket.getTicketSaleId() + " has been checked in successfully.");
            } else {
                statusLabel.setText("Failed to check in ticket #" + selectedTicket.getTicketSaleId());
            }
        } catch (SQLException e) {
            statusLabel.setText("Error checking in ticket: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
