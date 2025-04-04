package boxoffice.ui;

import boxoffice.BoxOfficeManager;
import boxoffice.database.*;
import boxoffice.models.RefundRepository;
import boxoffice.models.TicketSaleRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.sql.SQLException;
import java.time.LocalDate;

public class RefundsPage extends VBox {

    private final BoxOfficeManager boxOfficeManager;
    private final TextField searchField;
    private final Button searchButton;
    private final Button refundButton;
    private final TableView<TicketSale> ticketTable;
    private final ObservableList<TicketSale> ticketData;
    private final Label ticketInfoLabel;
    private final TextArea reasonField;
    private final ComboBox<String> refundTypeComboBox;
    private final TextField refundAmountField;
    private TicketSale selectedTicket;

    public RefundsPage(BoxOfficeManager boxOfficeManager) {
        this.boxOfficeManager = boxOfficeManager;

        this.setSpacing(15);
        this.setPadding(new Insets(20));
        this.setStyle("-fx-background-color: #f4f4f4; -fx-border-radius: 8; -fx-border-width: 1; -fx-border-color: #ccc;");

        searchField = new TextField();
        searchField.setPromptText("Enter Ticket Sale ID");
        searchField.setPrefWidth(200);

        searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchTicketById());

        refundButton = new Button("Process Refund");
        refundButton.setDisable(true);
        refundButton.setStyle("-fx-background-color: #ff5733; -fx-text-fill: white;");
        refundButton.setOnAction(e -> processRefund());

        ticketInfoLabel = new Label("No ticket selected");
        ticketInfoLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        reasonField = new TextArea();
        reasonField.setPromptText("Enter refund reason...");
        reasonField.setPrefRowCount(3);
        reasonField.setWrapText(true);

        refundTypeComboBox = new ComboBox<>();
        refundTypeComboBox.getItems().addAll("Full", "Partial");
        refundTypeComboBox.setValue("Full");
        refundTypeComboBox.setOnAction(e -> handleRefundTypeChange());

        refundAmountField = new TextField();
        refundAmountField.setPromptText("Enter refund amount");
        refundAmountField.setDisable(true);

        ticketTable = new TableView<>();
        setupTable();
        ticketData = FXCollections.observableArrayList();
        ticketTable.setItems(ticketData);

        ticketTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedTicket = newSelection;
                updateTicketInfo(newSelection);
                refundButton.setDisable(false);
                handleRefundTypeChange();
            } else {
                ticketInfoLabel.setText("No ticket selected");
                refundButton.setDisable(true);
                selectedTicket = null;
            }
        });

        HBox searchBox = new HBox(10, new Label("Ticket Sale ID:"), searchField, searchButton);
        searchBox.setAlignment(Pos.CENTER_LEFT);

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

        this.getChildren().addAll(
                searchBox,
                ticketInfoLabel,
                new TitledPane("Ticket Sales", ticketTable),
                new TitledPane("Refund Details", refundBox)
        );
    }

    private void setupTable() {
        TableColumn<TicketSale, Integer> idColumn = new TableColumn<>("Ticket ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ticketSaleId"));
        idColumn.setMinWidth(120);

        TableColumn<TicketSale, String> customerIdColumn = new TableColumn<>("Customer ID");
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerIdColumn.setMinWidth(120);

        TableColumn<TicketSale, String> customerNameColumn = new TableColumn<>("Customer Name");
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerNameColumn.setMinWidth(180);

        TableColumn<TicketSale, String> performanceTitleColumn = new TableColumn<>("Performance");
        performanceTitleColumn.setCellValueFactory(new PropertyValueFactory<>("performanceTitle"));
        performanceTitleColumn.setMinWidth(200);

        TableColumn<TicketSale, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setMinWidth(100);

        TableColumn<TicketSale, String> seatColumn = new TableColumn<>("Seat");
        seatColumn.setCellValueFactory(new PropertyValueFactory<>("seatID"));
        seatColumn.setMinWidth(100);

        ticketTable.getColumns().addAll(idColumn, customerIdColumn, customerNameColumn,
                performanceTitleColumn, priceColumn, seatColumn);
    }

    private void searchTicketById() {
        String searchTerm = searchField.getText().trim();
        if (!searchTerm.isEmpty()) {
            try {
                TicketSale ticket = TicketSaleRepository.getTicketSaleById(searchTerm);
                if (ticket != null) {
                    ticketData.clear();
                    ticketData.add(ticket);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Ticket Not Found", "No ticket found with ID " + searchTerm);
                }
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Database error: " + ex.getMessage());
            }
        }
    }

    private void updateTicketInfo(TicketSale ticket) {
        ticketInfoLabel.setText("Ticket ID: " + ticket.getTicketSaleId() +
                " | Customer: " + ticket.getCustomerName() +
                " | Performance: " + ticket.getPerformanceTitle() +
                " | Seat: " + ticket.getSeatID());
    }

    private void handleRefundTypeChange() {
        if (selectedTicket != null) {
            String refundType = refundTypeComboBox.getValue();
            if ("Full".equals(refundType)) {
                refundAmountField.setText(String.valueOf(selectedTicket.getPrice()));
                refundAmountField.setDisable(true);
            } else if ("Partial".equals(refundType)) {
                refundAmountField.setDisable(false);
                refundAmountField.clear();
            }
        }
    }

    private void processRefund() {
        if (selectedTicket == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a ticket.");
            return;
        }

        String reason = reasonField.getText().trim();
        String refundType = refundTypeComboBox.getValue();
        double refundAmount;

        if (reason.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please provide a refund reason.");
            return;
        }

        try {
            refundAmount = refundType.equals("Full") ? selectedTicket.getPrice() : Double.parseDouble(refundAmountField.getText().trim());
            if (refundAmount <= 0 || refundAmount > selectedTicket.getPrice()) {
                showAlert(Alert.AlertType.ERROR, "Invalid Amount", "Partial refund must be a valid value.");
                return;
            }
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Invalid Amount", "Please enter a valid numeric value.");
            return;
        }

        Refund refund = new Refund(0, LocalDate.now(), refundAmount, reason, "Processing",
                selectedTicket.getTicketSaleId(), boxOfficeManager.getCurrentStaff().getStaffId());

        try {
            if (RefundRepository.addRefund(refund)) {
                TicketSaleRepository.markAsRefunded(selectedTicket.getTicketSaleId());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Refund processed successfully.");
                ticketData.remove(selectedTicket);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to process refund.");
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Database error: " + ex.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

