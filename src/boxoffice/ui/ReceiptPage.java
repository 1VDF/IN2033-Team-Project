package boxoffice.ui;

import boxoffice.database.Seat;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class ReceiptPage {

    public static boolean showReceipt(Stage owner, List<Seat> selectedSeats, String customerName, double totalAmount) {
        // Separate accessibility and standard seats
        List<Seat> accessibilitySeats = selectedSeats.stream()
                .filter(Seat::isAccesible)
                .collect(Collectors.toList());

        List<Seat> standardSeats = selectedSeats.stream()
                .filter(seat -> !seat.isAccesible())
                .collect(Collectors.toList());

        // Create receipt content
        StringBuilder receiptContent = new StringBuilder();
        receiptContent.append("=== BOOKING RECEIPT ===\n");
        receiptContent.append("Customer: ").append(customerName).append("\n\n");

        if (!standardSeats.isEmpty()) {
            receiptContent.append("STANDARD SEATS:\n");
            standardSeats.forEach(seat ->
                    receiptContent.append(String.format("- %s (Row %s, Seat %d): $%.2f%n",
                            seat.getSeatID(),
                            seat.getRowNumber(),
                            seat.getSeatNumber(),
                            25.00)));
            receiptContent.append("\n");
        }

        if (!accessibilitySeats.isEmpty()) {
            receiptContent.append("ACCESSIBILITY SEATS:\n");
            accessibilitySeats.forEach(seat ->
                    receiptContent.append(String.format("- %s (Row %s, Seat %d): $%.2f%n",
                            seat.getSeatID(),
                            seat.getRowNumber(),
                            seat.getSeatNumber(),
                            25.00)));
            receiptContent.append("\n");
        }

        receiptContent.append(String.format("%nTOTAL AMOUNT: $%.2f%n%n", totalAmount));
        receiptContent.append("Do you confirm this booking?");

        // Create receipt dialog
        Stage receiptDialog = new Stage();
        receiptDialog.initModality(Modality.APPLICATION_MODAL);
        receiptDialog.initOwner(owner);
        receiptDialog.setTitle("Booking Receipt");

        TextArea receiptText = new TextArea(receiptContent.toString());
        receiptText.setEditable(false);
        receiptText.setStyle("-fx-font-family: monospace;");

        Button confirmButton = new Button("Confirm Booking");
        Button cancelButton = new Button("Cancel");

        HBox buttons = new HBox(10, confirmButton, cancelButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox layout = new VBox(20, receiptText, buttons);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Create a flag to track confirmation
        AtomicBoolean isConfirmed = new AtomicBoolean(false);

        confirmButton.setOnAction(e -> {
            isConfirmed.set(true);
            receiptDialog.close();
        });

        cancelButton.setOnAction(e -> receiptDialog.close());

        Scene scene = new Scene(layout, 400, 400);
        receiptDialog.setScene(scene);
        receiptDialog.showAndWait();

        return isConfirmed.get();
    }
}
