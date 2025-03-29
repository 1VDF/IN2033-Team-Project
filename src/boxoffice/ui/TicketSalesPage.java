package boxoffice.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import java.sql.*;
import java.util.*;

public class TicketSalesPage extends VBox {

    private ComboBox<String> performanceComboBox;
    private GridPane seatingPlan;
    private Map<String, Button> seatButtons = new HashMap<>();
    private List<String> selectedSeats = new ArrayList<>();
    private Label statusLabel;
    private ComboBox<String> discountComboBox;
    private TextField customerNameField;
    private Connection connection;

    public TicketSalesPage() {
        initializeUI();
    }

    private void initializeUI() {
        this.setSpacing(10);
        this.setPadding(new Insets(15));

        // Performance selection
        Label performanceLabel = new Label("Select Performance:");
        performanceComboBox = new ComboBox<>();
        updatePerformanceComboBox();

        // Customer information
        Label customerLabel = new Label("Customer Name:");
        customerNameField = new TextField();

        // Discount selection
        Label discountLabel = new Label("Discount Type:");
        discountComboBox = new ComboBox<>();
        discountComboBox.getItems().addAll("None", "NHS", "Military", "Student", "Staff");
        discountComboBox.setValue("None");

        // Seating plan container
        seatingPlan = new GridPane();
        seatingPlan.setHgap(5);
        seatingPlan.setVgap(5);
        ScrollPane scrollPane = new ScrollPane(seatingPlan);
        scrollPane.setFitToWidth(true);

        // Status label
        statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: #333; -fx-font-weight: bold;");

        // Action buttons
        Button sellTicketsButton = new Button("Confirm Booking");
        sellTicketsButton.setOnAction(e -> confirmBooking());

        Button resetSelectionButton = new Button("Reset Selection");
        resetSelectionButton.setOnAction(e -> resetSelection());

        // Layout
        HBox buttonBox = new HBox(10, sellTicketsButton, resetSelectionButton);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        getChildren().addAll(
                performanceLabel, performanceComboBox,
                customerLabel, customerNameField,
                discountLabel, discountComboBox,
                new Label("Seating Plan:"), scrollPane,
                statusLabel,
                buttonBox
        );

        // Event handlers
        performanceComboBox.setOnAction(e -> {
            String selectedPerformance = performanceComboBox.getValue();
            if (selectedPerformance != null) {
                updateSeatingPlan(selectedPerformance);
            }
        });
    }

    private void updatePerformanceComboBox() {
        performanceComboBox.getItems().clear();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT title FROM Performance")) {

            while (rs.next()) {
                performanceComboBox.getItems().add(rs.getString("title"));
            }

            if (!performanceComboBox.getItems().isEmpty()) {
                performanceComboBox.setValue(performanceComboBox.getItems().get(0));
                updateSeatingPlan(performanceComboBox.getValue());
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Could not load performances: " + e.getMessage());
        }
    }

    private void updateSeatingPlan(String performanceTitle) {
        seatingPlan.getChildren().clear();
        seatButtons.clear();
        selectedSeats.clear();
        statusLabel.setText("");

        try {
            // Get room assignment for this performance
            int roomId = -1;
            String roomQuery = "SELECT room_id FROM Room_Assignment ra " +
                    "JOIN Performance p ON ra.performance_id = p.performance_id " +
                    "WHERE p.title = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(roomQuery)) {
                pstmt.setString(1, performanceTitle);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    roomId = rs.getInt("room_id");
                }
            }

            if (roomId == -1) {
                showAlert("Error", "No room assigned for this performance");
                return;
            }

            // Get all seats for this room and their booking status
            String seatQuery = "SELECT s.row_number, s.seat_number, s.is_booked, " +
                    "pb.pre_booked_id IS NOT NULL AS is_prebooked " +
                    "FROM Seat s " +
                    "LEFT JOIN Pre_Booked pb ON s.row_number = pb.row_number AND s.seat_number = pb.seat_number " +
                    "WHERE s.room_id = ?";

            // Add screen at the top
            Label screenLabel = new Label(" S C R E E N ");
            screenLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
            seatingPlan.add(screenLabel, 0, 0, 20, 1);

            try (PreparedStatement pstmt = connection.prepareStatement(seatQuery)) {
                pstmt.setInt(1, roomId);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    String row = rs.getString("row_number");
                    int seatNum = rs.getInt("seat_number");
                    boolean isBooked = rs.getBoolean("is_booked") || rs.getBoolean("is_prebooked");
                    String seatId = row + seatNum;

                    Button seatButton = new Button(seatId);
                    seatButton.setPrefSize(40, 40);
                    seatButtons.put(seatId, seatButton);

                    if (isBooked) {
                        seatButton.setStyle("-fx-base: #ff6b6b;"); // Red for booked
                        seatButton.setDisable(true);
                    } else {
                        seatButton.setStyle("-fx-base: #a5d8ff;"); // Blue for available
                        seatButton.setOnMouseClicked(e -> handleSeatSelection(seatId));
                    }

                    // Add to grid - adjust positioning as needed
                    seatingPlan.add(seatButton, seatNum, getRowIndex(row) + 1); // +1 for screen row
                }
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Could not load seating plan: " + e.getMessage());
        }
    }

    private int getRowIndex(String rowLetter) {
        return rowLetter.charAt(0) - 'A' + 1;
    }

    private void handleSeatSelection(String seatId) {
        Button seatButton = seatButtons.get(seatId);
        if (selectedSeats.contains(seatId)) {
            selectedSeats.remove(seatId);
            seatButton.setStyle("-fx-base: #a5d8ff;"); // Blue for available
        } else {
            selectedSeats.add(seatId);
            seatButton.setStyle("-fx-base: #51cf66;"); // Green for selected
        }
        updateStatusLabel();
    }

    private void updateStatusLabel() {
        if (selectedSeats.isEmpty()) {
            statusLabel.setText("No seats selected");
            statusLabel.setTextFill(Color.BLACK);
        } else {
            statusLabel.setText("Selected: " + selectedSeats.size() + " seat(s) - " + String.join(", ", selectedSeats));
            statusLabel.setTextFill(Color.DARKGREEN);
        }
    }

    private void confirmBooking() {
        if (selectedSeats.isEmpty()) {
            statusLabel.setText("Please select at least one seat");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        String performance = performanceComboBox.getValue();
        if (performance == null) return;

        String customerName = customerNameField.getText().trim();
        if (customerName.isEmpty()) {
            statusLabel.setText("Please enter customer name");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        String discountType = discountComboBox.getValue();
        if (discountType.equals("None")) {
            discountType = null;
        }

        try {
            connection.setAutoCommit(false); // Start transaction

            // Get performance ID
            int performanceId = -1;
            try (PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT performance_id FROM Performance WHERE title = ?")) {
                pstmt.setString(1, performance);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    performanceId = rs.getInt("performance_id");
                }
            }

            if (performanceId == -1) {
                showAlert("Error", "Performance not found");
                return;
            }

            // Create FOL record (assuming simple customer tracking)
            int folId = -1;
            try (PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT INTO FOL (name, email) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, customerName);
                pstmt.setString(2, customerName.replaceAll("\\s+", "") + "@example.com");
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    folId = rs.getInt(1);
                }
            }

            // Get discount ID if applicable
            int discountId = 0; // 0 or some default for no discount
            if (discountType != null) {
                try (PreparedStatement pstmt = connection.prepareStatement(
                        "SELECT discount_id FROM Discount WHERE discount_type = ? LIMIT 1")) {
                    pstmt.setString(1, discountType);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        discountId = rs.getInt("discount_id");
                    }
                }
            }

            // Book each selected seat
            for (String seatId : selectedSeats) {
                String[] seatParts = seatId.split("(?<=\\D)(?=\\d)");
                String rowNumber = seatParts[0];
                int seatNumber = Integer.parseInt(seatParts[1]);

                // Create Pre_Booked record
                try (PreparedStatement pstmt = connection.prepareStatement(
                        "INSERT INTO Pre_Booked (performance_id, row_number, seat_number, fol_id) " +
                                "VALUES (?, ?, ?, ?)")) {
                    pstmt.setInt(1, performanceId);
                    pstmt.setString(2, rowNumber);
                    pstmt.setInt(3, seatNumber);
                    pstmt.setInt(4, folId);
                    pstmt.executeUpdate();
                }

                // Update Seat status
                try (PreparedStatement pstmt = connection.prepareStatement(
                        "UPDATE Seat SET is_booked = 1 " +
                                "WHERE row_number = ? AND seat_number = ?")) {
                    pstmt.setString(1, rowNumber);
                    pstmt.setInt(2, seatNumber);
                    pstmt.executeUpdate();
                }
            }

            connection.commit(); // Commit transaction
            statusLabel.setText("Successfully booked " + selectedSeats.size() + " seat(s)");
            statusLabel.setTextFill(Color.DARKGREEN);

            // Refresh the seating plan
            updateSeatingPlan(performance);

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                showAlert("Database Error", "Could not rollback transaction: " + ex.getMessage());
            }
            showAlert("Database Error", "Could not complete booking: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                showAlert("Database Error", "Could not reset auto-commit: " + e.getMessage());
            }
        }
    }

    private void resetSelection() {
        selectedSeats.clear();
        updateSeatingPlan(performanceComboBox.getValue());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}