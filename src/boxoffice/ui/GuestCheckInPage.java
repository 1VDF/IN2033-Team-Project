package boxoffice.ui;

import boxoffice.database.TicketSale;
import boxoffice.models.TicketSaleRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;

public class GuestCheckInPage extends VBox {
    private final TextField searchField;
    private final Button searchButton;
    private final TableView<TicketSale> ticketsTable;
    private final Button checkInButton;
    private final Label statusLabel;

    public GuestCheckInPage() {
        // Set up the search components
        searchField = new TextField();
        searchField.setPromptText("Enter customer name...");

        searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchCustomerTickets());

        HBox searchBox = new HBox(10, searchField, searchButton);
        searchBox.setPadding(new Insets(10));
        HBox.setHgrow(searchField, Priority.ALWAYS);

        // Set up the tickets table
        ticketsTable = new TableView<>();
        setupTableColumns();

        // Set up the check-in button
        checkInButton = new Button("Check In Selected Ticket");
        checkInButton.setOnAction(e -> checkInTicket());
        checkInButton.setDisable(true);

        // Status label for messages
        statusLabel = new Label();
        statusLabel.setPadding(new Insets(5, 10, 10, 10));

        // Add all components to the VBox
        this.getChildren().addAll(searchBox, ticketsTable, checkInButton, statusLabel);
        this.setSpacing(10);
        this.setPadding(new Insets(10));

        // Enable check-in button only when a ticket is selected
        ticketsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> checkInButton.setDisable(newSelection == null)
        );
    }

    private void setupTableColumns() {
        // Ticket ID column
        TableColumn<TicketSale, Integer> idCol = new TableColumn<>("Ticket ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ticketSaleId"));

        // Performance ID column
        TableColumn<TicketSale, Integer> performanceCol = new TableColumn<>("Performance ID");
        performanceCol.setCellValueFactory(new PropertyValueFactory<>("performanceID"));

        // Seat ID column
        TableColumn<TicketSale, String> seatCol = new TableColumn<>("Seat");
        seatCol.setCellValueFactory(new PropertyValueFactory<>("seatID"));

        // Price column
        TableColumn<TicketSale, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Checked-in status column
        TableColumn<TicketSale, Boolean> checkedInCol = new TableColumn<>("Checked In");
        checkedInCol.setCellValueFactory(new PropertyValueFactory<>("checkedIn"));

        ticketsTable.getColumns().addAll(idCol, performanceCol, seatCol, priceCol, checkedInCol);
    }

    private void searchCustomerTickets() {
        String customerName = searchField.getText().trim();

        if (customerName.isEmpty()) {
            statusLabel.setText("Please enter a customer name to search.");
            return;
        }

        try {
            // Use the repository method that joins with customer table
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
            // Use the repository method to update the database
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