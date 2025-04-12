package boxoffice.ui;

import boxoffice.database.Performance;
import boxoffice.database.Seat;
import boxoffice.database.TicketSale;
import boxoffice.models.*;
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

/**
 * This class represents the payment page for processing ticket payments in the box office system.
 * It allows the user to select a payment method, enter discount codes or gift card codes,
 * and process payments for selected seats in a performance.
 */
public class PaymentPage {

    private static Set<String> wheelchairAdjacentSeatIds = new HashSet<>();
    static double price = 25;

    /**
     * Processes the payment for the selected seats, showing the payment dialog and confirming the payment.
     *
     * @param owner The owner window that owns the payment dialog.
     * @param selectedSeats A list of seats selected by the customer.
     * @param customerId The ID of the customer making the payment.
     * @param customerName The name of the customer making the payment.
     * @param selectedPerformance The performance for which the payment is being made.
     * @return {@code true} if the payment was successfully processed, otherwise {@code false}.
     * @throws SQLException If there is an issue with the database interaction.
     */
    public static boolean processPayment(Stage owner, List<Seat> selectedSeats, String customerId, String customerName,
                                         Performance selectedPerformance) throws SQLException {
        List<Seat> allSeats = SeatRepository.getAllSeatsFromMainHall();
        wheelchairAdjacentSeatIds.clear();

        for (Seat seat : selectedSeats) {
            if (seat.isAccesible()) {
                String adjacentSeatId = getAdjacentSeatId(seat.getSeatID(), allSeats);
                if (adjacentSeatId != null && selectedSeats.stream()
                        .anyMatch(s -> s.getSeatID().equals(adjacentSeatId))) {
                    wheelchairAdjacentSeatIds.add(adjacentSeatId);
                }
            }
        }

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

        Label discountLabel = new Label("Discount Code (if any):");
        TextField discountField = new TextField();

        Label giftCardLabel = new Label("Gift Card Code:");
        TextField giftCardField = new TextField();
        giftCardLabel.setVisible(false);
        giftCardField.setVisible(false);

        paymentGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            boolean isGiftCard = newVal == giftCardButton;
            giftCardLabel.setVisible(isGiftCard);
            giftCardField.setVisible(isGiftCard);
        });

        double initialTotalAmount = calculateTotalAmount(selectedSeats, allSeats, "");
        Label totalLabel = new Label(String.format("Total Amount: £%.2f", initialTotalAmount));

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

                if (!discountCode.isEmpty() && getDiscountIdByCode(discountCode) == null) {
                    new Alert(Alert.AlertType.ERROR, "Invalid discount code").showAndWait();
                    return;
                }

                double finalTotalAmount = calculateTotalAmount(selectedSeats, allSeats, discountCode);

                boolean confirmed = ReceiptPage.showReceipt(
                        paymentDialog,
                        selectedSeats,
                        customerName,
                        finalTotalAmount,
                        wheelchairAdjacentSeatIds
                );

                if (confirmed) {
                    createTicketSales(selectedSeats,allSeats, customerId, selectedPerformance,discountCode);

                    new Alert(Alert.AlertType.INFORMATION,
                            String.format("Payment processed successfully!\nMethod: %s\nAmount: £%.2f",
                                    paymentMethod, finalTotalAmount)).showAndWait();

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

    /**
     * Calculates the total amount to be paid for the selected seats, considering any applicable discounts.
     *
     * @param selectedSeats A list of seats selected by the customer.
     * @param allSeats A list of all available seats in the main hall.
     * @param discountName The discount code entered by the customer (if any).
     * @return The total amount to be paid.
     * @throws SQLException If there is an issue with the database interaction.
     */
    private static double calculateTotalAmount(List<Seat> selectedSeats, List<Seat> allSeats, String discountName) throws SQLException {
        double total = 0;
        Set<String> processedCompanionSeats = new HashSet<>();
        Integer discountId = getDiscountIdByCode(discountName);

        for (Seat seat : selectedSeats) {
            if (processedCompanionSeats.contains(seat.getSeatID())) {
                continue;
            }

            List<String> partialRestrictedSeats = RestrictedRepository.getPartialRestrictedSeats();

            if(partialRestrictedSeats.contains(seat.getSeatID())){
                price = 21;
            }else{
                price = 25;
            }

            double finalPrice = applyDiscount(price, discountId);

            if (seat.isAccesible()) {
                String companionSeatId = getAdjacentSeatId(seat.getSeatID(), allSeats);
                boolean hasCompanion = selectedSeats.stream()
                        .anyMatch(s -> s.getSeatID().equals(companionSeatId));

                if (hasCompanion) {
                    total += finalPrice;
                    processedCompanionSeats.add(companionSeatId);
                } else {
                    total += finalPrice;
                }
            } else {
                total += finalPrice;
            }
        }
        return total;
    }

    /**
     * Creates the ticket sales entries in the database for the selected seats and processes the payment.
     *
     * @param selectedSeats A list of seats selected by the customer.
     * @param allSeats A list of all available seats in the main hall.
     * @param customerId The ID of the customer making the payment.
     * @param performance The performance for which the payment is being made.
     * @param discountName The discount code entered by the customer (if any).
     * @throws Exception If an error occurs while creating the ticket sales.
     */
    private static void createTicketSales(List<Seat> selectedSeats, List<Seat> allSeats, String customerId,
                                          Performance performance, String discountName) throws Exception {
        Set<String> processedCompanionSeats = new HashSet<>();
        Integer discountId = getDiscountIdByCode(discountName);

        for (Seat seat : selectedSeats) {
            if (processedCompanionSeats.contains(seat.getSeatID())) {
                continue;
            }

            List<String> partialRestrictedSeats = RestrictedRepository.getPartialRestrictedSeats();

            if(partialRestrictedSeats.contains(seat.getSeatID())){
                price = 21;
            }else{
                price = 25;
            }

            double finalPrice = applyDiscount(price, discountId);

            boolean isAccessible = seat.isAccesible();
            String companionSeatId = getAdjacentSeatId(seat.getSeatID(),allSeats);
            boolean hasCompanion = companionSeatId != null &&
                    selectedSeats.stream().anyMatch(s -> s.getSeatID().equals(companionSeatId));

            if (isAccessible && hasCompanion) {
                TicketSale accessibleTicket = new TicketSale(
                        0,
                        finalPrice,
                        customerId,
                        performance.getPerformanceId(),
                        seat.getSeatID(),
                        discountId,
                        NULL,
                        Session.getInstance().getCurrentStaff().getStaffId()
                );
                TicketSaleRepository.addTicketSale(accessibleTicket);

                TicketSale companionTicket = new TicketSale(
                        0,
                        0.0,
                        "WHEELCHAIR_ADJACENT",
                        performance.getPerformanceId(),
                        companionSeatId,
                        NULL,
                        NULL,
                        Session.getInstance().getCurrentStaff().getStaffId()
                );
                TicketSaleRepository.addTicketSale(companionTicket);

                processedCompanionSeats.add(companionSeatId);
            } else {
                TicketSale ticket = new TicketSale(
                        0,
                        finalPrice,
                        customerId,
                        performance.getPerformanceId(),
                        seat.getSeatID(),
                        discountId,
                        NULL,
                        Session.getInstance().getCurrentStaff().getStaffId()
                );
                TicketSaleRepository.addTicketSale(ticket);
            }
        }
    }

    /**
     * Retrieves the ID of the adjacent seat for a given seat.
     *
     * @param seatId The ID of the seat for which to find the adjacent seat.
     * @param allSeats A list of all available seats in the main hall.
     * @return The ID of the adjacent seat, or {@code null} if no adjacent seat exists.
     */
    private static String getAdjacentSeatId(String seatId, List<Seat> allSeats) {
        try {
            String prefix = seatId.substring(0, 3);
            int number = Integer.parseInt(seatId.substring(3));

            String forwardSeatId = prefix + (number + 1);
            boolean forwardExists = allSeats.stream()
                    .anyMatch(s -> s.getSeatID().equals(forwardSeatId));

            if (forwardExists) {
                return forwardSeatId;
            }

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

    /**
     * Retrieves the discount ID for the given discount code.
     *
     * @param discountName The discount code entered by the customer.
     * @return The discount ID, or {@code null} if no valid discount is found.
     * @throws SQLException If there is an issue with the database interaction.
     */
    private static Integer getDiscountIdByCode(String discountName) throws SQLException {
        if (discountName == null) {
            return null;
        }
        return DiscountRepository.getDiscountIdByName(discountName);
    }

    /**
     * Applies the discount to the original price of a seat.
     *
     * @param originalPrice The original price of the seat.
     * @param discountId The ID of the discount to be applied.
     * @return The final price after the discount is applied.
     * @throws SQLException If there is an issue with the database interaction.
     */
    private static double applyDiscount(double originalPrice, Integer discountId) throws SQLException {
        if (discountId == null) {
            return originalPrice;
        }
        double discountValue = DiscountRepository.getDiscountValueById(discountId);
        return originalPrice * (1 - discountValue/100);
    }
}
