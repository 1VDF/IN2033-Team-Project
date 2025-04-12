package boxoffice.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The GroupBookingsPage class represents a user interface for creating group bookings in the box office system.
 * It allows users to input a group name and a list of seat numbers to create a group booking.
 */
public class GroupBookingsPage extends VBox {

    /**
     * Constructs a new GroupBookingsPage and initialises its user interface components.
     * This includes the title, input fields for group name and seat numbers, and a button to create a group booking.
     */
    public GroupBookingsPage() {

        // Title
        Label title = new Label("Group Booking");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        title.setStyle("-fx-text-fill: #333333;");

        // Group name input
        Label groupNameLabel = new Label("Enter Group Name:");
        TextField groupNameField = new TextField();
        groupNameField.setPromptText("Group Name");

        // Seat numbers input
        Label seatLabel = new Label("Enter Seats (comma separated):");
        TextField seatField = new TextField();
        seatField.setPromptText("Seat1, Seat2, Seat3");

        Button createGroupButton = new Button("Create Group Booking");
        createGroupButton.setOnAction(e -> {
            String groupName = groupNameField.getText();
            String seatsInput = seatField.getText();

            if (groupName.isEmpty() || seatsInput.isEmpty()) {
                showError("Both group name and seats must be provided.");
                return;
            }

            // Split seats by comma and create a list
            String[] seatArray = seatsInput.split(",");
            List<String> seats = new ArrayList<>();
            for (String seat : seatArray) {
                seats.add(seat.trim());
            }

            // Confirmation message
            showConfirmation("Group booking created successfully.");
        });

        // Layout setup
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20;");
        layout.getChildren().addAll(title, groupNameLabel, groupNameField, seatLabel, seatField, createGroupButton);

        this.getChildren().add(layout);
    }

    /**
     * Displays an error message in an alert dialog box.
     *
     * @param message The error message to be displayed
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays a confirmation message in an alert dialog box.
     *
     * @param message The confirmation message to be displayed
     */
    private void showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
