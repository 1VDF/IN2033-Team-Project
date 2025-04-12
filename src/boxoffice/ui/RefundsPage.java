package boxoffice.ui;

import boxoffice.database.*;
import boxoffice.models.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Represents the page in the user interface where refunds can be processed.
 * This class allows the selection of a date, performance, customer, and ticket,
 * and facilitates the processing of refunds.
 * <p>
 * The refund process involves the following steps:
 * <ul>
 *     <li>Select a date</li>
 *     <li>Select a performance</li>
 *     <li>Search for a customer</li>
 *     <li>Select the relevant ticket for refund</li>
 *     <li>Process the refund (either full or partial)</li>
 * </ul>
 * </p>
 */
public class RefundsPage extends VBox {

    private final DatePicker datePicker = new DatePicker(LocalDate.now());
    private final Button dateNextButton = new Button("Next");
    private final ComboBox<Performance> performanceComboBox = new ComboBox<>();
    private final Button performanceNextButton = new Button("Next");
    private final TextField customerSearchField = new TextField();
    private final Button searchButton = new Button("Search");
    private final TableView<TicketSale> ticketTable = new TableView<>();
    private final ObservableList<TicketSale> ticketData = FXCollections.observableArrayList();
    private final Button refundButton = new Button("Process Refund");
    private final TextArea reasonField = new TextArea();
    private final ComboBox<String> refundTypeComboBox = new ComboBox<>();
    private final TextField refundAmountField = new TextField();

    // TitledPanes for organising UI components.
    private final TitledPane dateTitledPane;
    private final TitledPane performanceTitledPane;
    private final TitledPane customerTitledPane;
    private final TitledPane ticketsTitledPane;
    private final TitledPane refundTitledPane;

    private TicketSale selectedTicket;
    private Performance selectedPerformance;

    /**
     * Constructs the RefundsPage UI components and sets up event handlers.
     * This includes the date selection, performance selection, customer search,
     * ticket table setup, and the refund details UI.
     */
    public RefundsPage() {
        System.setProperty("javafx.accessibility.force", "false");

        setupDateSelection();
        setupPerformanceSelection();
        setupCustomerSearch();
        setupTicketTable();
        setupRefundDetails();

        dateTitledPane = new TitledPane("1. Select Date", createDateBox());
        performanceTitledPane = new TitledPane("2. Select Performance", createPerformanceBox());
        customerTitledPane = new TitledPane("3. Search Customer", createCustomerSearchBox());
        ticketsTitledPane = new TitledPane("Ticket Sales", ticketTable);
        refundTitledPane = new TitledPane("Refund Details", createRefundBox());

        performanceTitledPane.setVisible(false);
        customerTitledPane.setVisible(false);
        ticketsTitledPane.setVisible(false);
        refundTitledPane.setVisible(false);

        Label authStatus = new Label();
        authStatus.setStyle("-fx-font-weight: bold;");
        if (Session.getInstance().isLoggedIn()) {
            authStatus.setText("Logged in as: " + Session.getInstance().getCurrentStaff().getRole());
            authStatus.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        } else {
            authStatus.setText("Unauthorised - Only Managers can process refunds");
            authStatus.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        }

        HBox bottomBox = new HBox(createBackButton());
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(3, 0, 0, 0));

        this.getChildren().addAll(
                authStatus,
                dateTitledPane,
                performanceTitledPane,
                customerTitledPane,
                ticketsTitledPane,
                refundTitledPane,
                bottomBox
        );
    }

    /**
     * Configures the date selection UI and its functionality.
     * This includes setting the current date as default and handling the "Next" button action.
     */
    private void setupDateSelection() {
        datePicker.setValue(LocalDate.now());
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(String.valueOf(date.getDayOfMonth()));
                }
            }
        });
        dateNextButton.setOnAction(e -> loadPerformancesForDate());
    }

    /**
     * Creates and returns the HBox containing the date selection UI components.
     *
     * @return The HBox for date selection.
     */
    private HBox createDateBox() {
        HBox dateBox = new HBox(10, new Label("Select Date:"), datePicker, dateNextButton);
        dateBox.setAlignment(Pos.CENTER_LEFT);
        return dateBox;
    }

    /**
     * Configures the performance selection UI and its functionality.
     * The list of performances is loaded based on the selected date.
     */
    private void setupPerformanceSelection() {
        performanceComboBox.setPromptText("Select Performance");
        performanceComboBox.setPrefWidth(300);
        performanceNextButton.setDisable(true);
        performanceNextButton.setOnAction(e -> showCustomerSearch());

        performanceComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Performance performance, boolean empty) {
                super.updateItem(performance, empty);
                setText(empty || performance == null ? null : performance.getDisplayText());
            }
        });

        performanceComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Performance performance, boolean empty) {
                super.updateItem(performance, empty);
                setText(empty || performance == null ? null : performance.getDisplayText());
            }
        });

        performanceComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            performanceNextButton.setDisable(newVal == null);
            selectedPerformance = newVal;
        });
    }

    /**
     * Creates and returns the HBox containing the performance selection UI components.
     *
     * @return The HBox for performance selection.
     */
    private HBox createPerformanceBox() {
        HBox performanceBox = new HBox(10, new Label("Select Performance:"), performanceComboBox, performanceNextButton);
        performanceBox.setAlignment(Pos.CENTER_LEFT);
        return performanceBox;
    }

    /**
     * Configures the customer search UI and its functionality.
     * This allows the search for a customer by name and filters tickets accordingly.
     */
    private void setupCustomerSearch() {
        customerSearchField.setPromptText("Enter Customer Name");
        searchButton.setOnAction(e -> searchTickets());
    }

    /**
     * Creates and returns the HBox containing the customer search UI components.
     *
     * @return The HBox for customer search.
     */
    private HBox createCustomerSearchBox() {
        HBox customerSearchBox = new HBox(10, new Label("Customer Name:"), customerSearchField, searchButton);
        customerSearchBox.setAlignment(Pos.CENTER_LEFT);
        return customerSearchBox;
    }

    /**
     * Configures the ticket table UI and its functionality.
     * This includes setting up columns and handling ticket selection for refund processing.
     */
    private void setupTicketTable() {
        setupTable();
        ticketTable.setItems(ticketData);
        ticketTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedTicket = newSelection;
            if (newSelection != null) {
                if(Session.getInstance().getCurrentStaff().getRole() == Staff.Role.Manager || Session.getInstance().getCurrentStaff().getRole() == Staff.Role.DeputyManager)
                    handleRefundTypeChange();
                refundButton.setDisable(false);
            } else {
                refundButton.setDisable(true);
            }
        });
    }

    /**
     * Configures and sets up the columns for the ticket table.
     */
    private void setupTable() {
        TableColumn<TicketSale, Integer> idColumn = new TableColumn<>("Ticket ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ticketSaleId"));

        TableColumn<TicketSale, String> customerNameColumn = new TableColumn<>("Customer");
        customerNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCustomerName()));

        TableColumn<TicketSale, String> performanceColumn = new TableColumn<>("Performance");
        performanceColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(selectedPerformance != null ?
                        selectedPerformance.getTitle() : "Unknown"));

        TableColumn<TicketSale, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty || price == null ? null : String.format("£%.2f", price));
            }
        });

        TableColumn<TicketSale, String> seatColumn = new TableColumn<>("Seat");
        seatColumn.setCellValueFactory(new PropertyValueFactory<>("seatID"));

        TableColumn<TicketSale, String> saleDateColumn = new TableColumn<>("Sale Date");
        saleDateColumn.setCellValueFactory(cellData -> {
            try {
                Timestamp timestamp = TicketSaleRepository.getSaleTimestamp(cellData.getValue().getTicketSaleId());
                return new SimpleStringProperty(timestamp != null ?
                        timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) : "Unknown");
            } catch (SQLException e) {
                return new SimpleStringProperty("Error");
            }
        });

        ticketTable.getColumns().addAll(
                idColumn, customerNameColumn, performanceColumn,
                priceColumn, seatColumn, saleDateColumn
        );
    }

    /**
     * Configures the refund details UI and its functionality.
     * This includes setting up the refund type (full or partial), amount, and reason fields.
     */
    private void setupRefundDetails() {
        refundButton.setDisable(true);
        refundButton.setStyle("-fx-background-color: #ff5733; -fx-text-fill: white;");
        refundButton.setOnAction(e -> processRefund());

        if (Session.getInstance().getCurrentStaff().getRole() == Staff.Role.Staff) {
            refundButton.setTooltip(new Tooltip("Only Managers can process refunds"));
        }

        reasonField.setPromptText("Enter refund reason...");
        reasonField.setPrefRowCount(3);
        reasonField.setWrapText(true);

        refundTypeComboBox.getItems().addAll("Full", "Partial");
        refundTypeComboBox.setValue("Full");
        refundTypeComboBox.setOnAction(e -> handleRefundTypeChange());

        refundAmountField.setPromptText("Enter refund amount");
        refundAmountField.setDisable(true);
    }

    /**
     * Creates and returns the VBox containing the refund details UI components.
     *
     * @return The VBox for refund details.
     */
    private VBox createRefundBox() {
        VBox refundBox = new VBox(10,
                new Label("Refund Type:"),
                refundTypeComboBox,
                new Label("Refund Amount:"),
                refundAmountField,
                new Label("Refund Reason:"),
                reasonField,
                refundButton
        );
        refundBox.setPadding(new Insets(10));
        refundBox.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-padding: 15;");
        return refundBox;
    }

    /**
     * Loads the available performances for the selected date.
     * This is triggered when the "Next" button on the date selection pane is clicked.
     */
    private void loadPerformancesForDate() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate != null) {
            try {
                List<Performance> performances = PerformanceRepository.getPerformancesByDate(selectedDate);
                performanceComboBox.setItems(FXCollections.observableArrayList(performances));

                performanceTitledPane.setVisible(true);
                performanceTitledPane.setExpanded(true);
                customerTitledPane.setVisible(false);
                ticketsTitledPane.setVisible(false);
                refundTitledPane.setVisible(false);

                performanceComboBox.getSelectionModel().clearSelection();
                customerSearchField.clear();
                ticketData.clear();
            } catch (SQLException ex) {
                showAlert("Error Loading Performances", ex.getMessage(), false);
            }
        }
    }

    /**
     * Displays the customer search section and prepares for ticket searching.
     */
    private void showCustomerSearch() {
        customerTitledPane.setVisible(true);
        customerTitledPane.setExpanded(true);
        ticketsTitledPane.setVisible(false);
        refundTitledPane.setVisible(false);
    }

    /**
     * Searches for tickets related to a specific customer and performance.
     */
    private void searchTickets() {
        String customerName = customerSearchField.getText().trim();
        if (selectedPerformance != null && !customerName.isEmpty()) {
            try {
                List<TicketSale> tickets = TicketSaleRepository.getTicketsForRefund(
                        selectedPerformance.getPerformanceId(),
                        customerName
                );

                ticketData.setAll(tickets);
                ticketsTitledPane.setVisible(true);
                ticketsTitledPane.setExpanded(true);
                refundTitledPane.setVisible(true);
            } catch (SQLException ex) {
                showAlert("Error Searching Tickets", ex.getMessage(), false);
            }
        }
    }

    /**
     * Handles the change of refund type (full or partial) and adjusts the amount field.
     */
    private void handleRefundTypeChange() {
        if (selectedTicket != null) {
            String refundType = refundTypeComboBox.getValue();
            if ("Full".equals(refundType)) {
                refundAmountField.setText(String.format("%.2f", selectedTicket.getPrice()));
                refundAmountField.setDisable(true);
            } else {
                refundAmountField.setDisable(false);
                refundAmountField.clear();
            }
        }
    }

    /**
     * Processes the refund for the selected ticket, based on the entered amount and reason.
     */
    private void processRefund() {
        if (Session.getInstance().getCurrentStaff().getRole() == Staff.Role.Staff) {
            showAlert("Access Denied", "Only Managers and Deputy Managers can process refunds.", false);
            return;
        }

        if (selectedTicket == null) {
            showAlert("Error", "No ticket selected", false);
            return;
        }

        String reason = reasonField.getText().trim();
        if (reason.isEmpty()) {
            showAlert("Error", "Please enter a refund reason", false);
            return;
        }

        try {
            double refundAmount = "Full".equals(refundTypeComboBox.getValue()) ?
                    selectedTicket.getPrice() :
                    validateRefundAmount();

            Refund refund = new Refund(
                    0,
                    LocalDate.now(),
                    refundAmount,
                    reason,
                    "Done",
                    selectedTicket.getTicketSaleId(),
                    Session.getInstance().getCurrentStaff().getStaffId()
            );

            if (RefundRepository.addRefund(refund)) {
                TicketSaleRepository.markAsRefunded(selectedTicket.getTicketSaleId());
                showAlert("Success", "Refund processed successfully", true);
                ticketData.remove(selectedTicket);
                selectedTicket = null;
                refundButton.setDisable(true);
                reasonField.clear();
                refundAmountField.clear();
                refundTypeComboBox.setValue("Full");
            } else {
                showAlert("Error", "Failed to process refund", false);
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid refund amount format", false);
        } catch (IllegalArgumentException e) {
            showAlert("Error", e.getMessage(), false);
        } catch (SQLException ex) {
            showAlert("Error", "Database error: " + ex.getMessage(), false);
        }
    }

    /**
     * Validates the refund amount for partial refunds.
     *
     * @return The validated refund amount.
     * @throws IllegalArgumentException if the amount is invalid.
     */
    private double validateRefundAmount() throws IllegalArgumentException {
        double amount = Double.parseDouble(refundAmountField.getText());
        if (amount <= 0 || amount > selectedTicket.getPrice()) {
            throw new IllegalArgumentException("Refund amount must be between 0 and the ticket price.");
        }
        return amount;
    }

    /**
     * Shows an alert dialog with the specified title, message, and success status.
     *
     * @param title The title of the alert.
     * @param message The message to display.
     * @param isSuccess Whether the alert indicates success.
     */
    private void showAlert(String title, String message, boolean isSuccess) {
        Alert alert = new Alert(isSuccess ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Creates and returns a back button that navigates to the previous page.
     *
     * @return The back button.
     */
    private Button createBackButton() {
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            // Navigate back to previous page
        });
        return backButton;
    }
}
