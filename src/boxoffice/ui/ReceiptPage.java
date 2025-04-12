package boxoffice.ui;

import boxoffice.database.Seat;
import boxoffice.models.RestrictedRepository;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * This class handles the receipt page for displaying booking details and confirming the booking.
 */
public class ReceiptPage {

    /**
     * Displays the booking receipt for the selected seats and allows the user to confirm or cancel the booking.
     *
     * @param owner The owner window that owns the receipt dialog.
     * @param selectedSeats The list of selected seats for the booking.
     * @param customerName The name of the customer making the booking.
     * @param totalAmount The total amount for the booking.
     * @param wheelchairAdjacentSeatIds A set of wheelchair-adjacent seat IDs.
     * @return {@code true} if the user confirmed the booking, {@code false} if they cancelled it.
     */
    public static boolean showReceipt(Stage owner, List<Seat> selectedSeats, String customerName, double totalAmount, Set<String> wheelchairAdjacentSeatIds) {
        List<Seat> accessibilitySeats = selectedSeats.stream()
                .filter(Seat::isAccesible)
                .collect(Collectors.toList());

        List<Seat> standardSeats = selectedSeats.stream()
                .filter(seat -> !seat.isAccesible())
                .collect(Collectors.toList());

        StringBuilder receiptContent = new StringBuilder();
        receiptContent.append("=== BOOKING RECEIPT ===\n");
        receiptContent.append("Customer: ").append(customerName).append("\n\n");

        if (!standardSeats.isEmpty()) {
            receiptContent.append("STANDARD SEATS:\n");
            standardSeats.forEach(seat -> {
                double price = wheelchairAdjacentSeatIds.contains(seat.getSeatID()) ? 0.00 : 25.00;
                try {
                    if (RestrictedRepository.getPartialRestrictedSeats().contains(seat.getSeatID())&& !wheelchairAdjacentSeatIds.contains(seat.getSeatID())) {
                        price = 21;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                receiptContent.append(String.format("- %s (Row %s, Seat %d): £%.2f%n",
                        seat.getSeatID(),
                        seat.getRowNumber(),
                        seat.getSeatNumber(),
                        price));
            });
            receiptContent.append("\n");
        }

        if (!accessibilitySeats.isEmpty()) {
            receiptContent.append("ACCESSIBILITY SEATS:\n");
            accessibilitySeats.forEach(seat -> {
                double price = wheelchairAdjacentSeatIds.contains(seat.getSeatID()) ? 0.00 : 25.00;
                try {
                    if (RestrictedRepository.getPartialRestrictedSeats().contains(seat.getSeatID()) && !wheelchairAdjacentSeatIds.contains(seat.getSeatID())) {
                        price = 21;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                receiptContent.append(String.format("- %s (Row %s, Seat %d): £%.2f%n",
                        seat.getSeatID(),
                        seat.getRowNumber(),
                        seat.getSeatNumber(),
                        price));
            });
            receiptContent.append("\n");
        }

        receiptContent.append(String.format("%nTOTAL AMOUNT: £%.2f%n%n", totalAmount));
        receiptContent.append("Do you confirm this booking?");

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
