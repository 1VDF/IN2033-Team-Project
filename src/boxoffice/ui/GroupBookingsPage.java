package boxoffice.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public class GroupBookingsPage extends VBox {

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

            //  confirmation message
            showConfirmation("Group booking created successfully.");
        });

        // Layout setup
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20;");
        layout.getChildren().addAll(title, groupNameLabel, groupNameField, seatLabel, seatField, createGroupButton);

        this.getChildren().add(layout);
    }

    // Helper method to show error message
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper method to show confirmation message
    private void showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
