package boxoffice.ui;

import boxoffice.database.Performance;
import boxoffice.database.Seat;
import boxoffice.database.TicketSale;
import boxoffice.models.SeatRepository;
import boxoffice.models.TicketSaleRepository;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.sql.Types.NULL;

public class PaymentPage {

    public static boolean processPayment(Stage owner, List<Seat> selectedSeats, String customerId, String customerName,
                                         Performance selectedPerformance) throws SQLException {
        List<Seat> allSeats = SeatRepository.getAllSeatsFromMainHall();

        // Create payment dialog
        Stage paymentDialog = new Stage();
        paymentDialog.initModality(Modality.APPLICATION_MODAL);
        paymentDialog.initOwner(owner);
        paymentDialog.setTitle("Payment Information");

        GridPane paymentGrid = new GridPane();
        paymentGrid.setPadding(new Insets(20));
        paymentGrid.setHgap(10);
        paymentGrid.setVgap(10);

        // Payment type selection
        Label paymentTypeLabel = new Label("Payment Method:");
        ToggleGroup paymentGroup = new ToggleGroup();
        RadioButton cashButton = new RadioButton("Cash");
        RadioButton cardButton = new RadioButton("Card");
        RadioButton giftCardButton = new RadioButton("Gift Card");
        cashButton.setToggleGroup(paymentGroup);
        cardButton.setToggleGroup(paymentGroup);
        giftCardButton.setToggleGroup(paymentGroup);
        cashButton.setSelected(true);

        // Discount code field
        Label discountLabel = new Label("Discount Code (if any):");
        TextField discountField = new TextField();

        // Gift card code field
        Label giftCardLabel = new Label("Gift Card Code:");
        TextField giftCardField = new TextField();
        giftCardLabel.setVisible(false);
        giftCardField.setVisible(false);

        paymentGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            boolean isGiftCard = newVal == giftCardButton;
            giftCardLabel.setVisible(isGiftCard);
            giftCardField.setVisible(isGiftCard);
        });

        // Calculate total amount (excluding companion seats)
        double totalAmount = calculateTotalAmount(selectedSeats,allSeats);
        Label totalLabel = new Label(String.format("Total Amount: $%.2f", totalAmount));

        // Buttons
        Button processPaymentButton = new Button("Process Payment");
        Button cancelPaymentButton = new Button("Cancel");

        // Layout
        paymentGrid.add(paymentTypeLabel, 0, 0);
        paymentGrid.add(cashButton, 1, 0);
        paymentGrid.add(cardButton, 2, 0);
        paymentGrid.add(giftCardButton, 3, 0);
        paymentGrid.add(discountLabel, 0, 1);
        paymentGrid.add(discountField, 1, 1, 3, 1);
        paymentGrid.add(giftCardLabel, 0, 2);
        paymentGrid.add(giftCardField, 1, 2, 3, 1);
        paymentGrid.add(totalLabel, 0, 3, 4, 1);
        paymentGrid.add(processPaymentButton, 2, 4);
        paymentGrid.add(cancelPaymentButton, 3, 4);

        // Create a flag to track payment success
        AtomicBoolean paymentSuccess = new AtomicBoolean(false);

        // Process payment action
        processPaymentButton.setOnAction(e -> {
            try {
                String paymentMethod = ((RadioButton) paymentGroup.getSelectedToggle()).getText();
                String discountCode = discountField.getText().trim();
                String giftCardCode = giftCardField.getText().trim();

                if (paymentMethod.equals("Gift Card") && giftCardCode.isEmpty()) {
                    new Alert(Alert.AlertType.ERROR, "Please enter gift card code").showAndWait();
                    return;
                }

                // Show receipt for confirmation
                boolean confirmed = ReceiptPage.showReceipt(
                        paymentDialog,
                        selectedSeats,
                        customerName,
                        totalAmount
                );

                if (confirmed) {
                    // Create ticket sales
                    createTicketSales(selectedSeats,allSeats, customerId, selectedPerformance);

                    new Alert(Alert.AlertType.INFORMATION,
                            String.format("Payment processed successfully!\nMethod: %s\nAmount: $%.2f",
                                    paymentMethod, totalAmount)).showAndWait();

                    paymentSuccess.set(true);
                    paymentDialog.close();
                }
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error processing payment: " + ex.getMessage()).showAndWait();
                ex.printStackTrace();
            }
        });

        cancelPaymentButton.setOnAction(e -> paymentDialog.close());

        Scene paymentScene = new Scene(paymentGrid);
        paymentDialog.setScene(paymentScene);
        paymentDialog.showAndWait();

        return paymentSuccess.get();
    }

    private static double calculateTotalAmount(List<Seat> selectedSeats, List<Seat> allSeats) {
        double total = 0;
        Set<String> processedCompanionSeats = new HashSet<>();

        for (Seat seat : selectedSeats) {
            // Skip if this is a companion seat we've already processed
            if (processedCompanionSeats.contains(seat.getSeatID())) {
                continue;
            }

            // Check if this is an accessible seat with a companion
            if (seat.isAccesible()) {
                String companionSeatId = getAdjacentSeatId(seat.getSeatID(), allSeats);
                boolean hasCompanion = selectedSeats.stream()
                        .anyMatch(s -> s.getSeatID().equals(companionSeatId));

                if (hasCompanion) {
                    // Charge only for the accessible seat (companion is free)
                    total += 25.0;
                    processedCompanionSeats.add(companionSeatId);
                } else {
                    // Accessible seat booked alone
                    total += 25.0;
                }
            } else {
                // Normal seat
                total += 25.0;
            }
        }
        return total;
    }

    private static void createTicketSales(List<Seat> selectedSeats, List<Seat> allSeats, String customerId,
                                          Performance performance) throws Exception {
        Set<String> processedCompanionSeats = new HashSet<>();

        for (Seat seat : selectedSeats) {
            // Skip if this is a companion seat we've already processed
            if (processedCompanionSeats.contains(seat.getSeatID())) {
                continue;
            }

            // Check if this is an accessible seat with a companion
            boolean isAccessible = seat.isAccesible();
            String companionSeatId = getAdjacentSeatId(seat.getSeatID(),allSeats);
            boolean hasCompanion = companionSeatId != null &&
                    selectedSeats.stream().anyMatch(s -> s.getSeatID().equals(companionSeatId));

            if (isAccessible && hasCompanion) {
                // Create ticket for accessible seat
                TicketSale accessibleTicket = new TicketSale(
                        0,
                        25.0,
                        customerId,
                        performance.getPerformanceId(),
                        seat.getSeatID(),
                        NULL,
                        NULL,
                        1
                );
                TicketSaleRepository.addTicketSale(accessibleTicket);

                // Create companion ticket (free)
                TicketSale companionTicket = new TicketSale(
                        0,
                        0.0,
                        "WHEELCHAIR_ADJACENT",
                        performance.getPerformanceId(),
                        companionSeatId,
                        NULL,
                        NULL,
                        1
                );
                TicketSaleRepository.addTicketSale(companionTicket);

                processedCompanionSeats.add(companionSeatId);
            } else {
                // Normal seat booking
                TicketSale ticket = new TicketSale(
                        0, // auto-generated ID
                        25.0, // normal price
                        customerId,
                        performance.getPerformanceId(),
                        seat.getSeatID(),
                        NULL, // no discount
                        NULL, // no group booking
                        1  // staff ID
                );
                TicketSaleRepository.addTicketSale(ticket);
            }
        }
    }

    private static String getAdjacentSeatId(String seatId, List<Seat> allSeats) {
        try {
            String prefix = seatId.substring(0, 3); // "MHA"
            int number = Integer.parseInt(seatId.substring(3)); // "1" from "MHA1"

            // Try forward adjacent first
            String forwardSeatId = prefix + (number + 1);
            boolean forwardExists = allSeats.stream()
                    .anyMatch(s -> s.getSeatID().equals(forwardSeatId));

            if (forwardExists) {
                return forwardSeatId;
            }

            // If no forward seat, try backward adjacent
            if (number > 1) {
                String backwardSeatId = prefix + (number - 1);
                boolean backwardExists = allSeats.stream()
                        .anyMatch(s -> s.getSeatID().equals(backwardSeatId));

                if (backwardExists) {
                    return backwardSeatId;
                }
            }

            return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
