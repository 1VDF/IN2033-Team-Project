package boxoffice.ui;

import boxoffice.BoxOfficeManager;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class RefundsPage extends VBox {

    private BoxOfficeManager boxOfficeManager;

    public RefundsPage(BoxOfficeManager boxOfficeManager) {
        this.boxOfficeManager = boxOfficeManager;

        Label ticketCodeLabel = new Label("Ticket Code:");
        TextField ticketCodeField = new TextField();

        Button refundTicketButton = new Button("Refund Ticket");
        refundTicketButton.setOnAction(e -> {
            String ticketCode = ticketCodeField.getText();
            boolean refunded = boxOfficeManager.refundTicket(ticketCode);
            if (refunded) {
                System.out.println("Ticket refunded: " + ticketCode);
            } else {
                System.out.println("Ticket not found for refund: " + ticketCode);
            }
        });

        getChildren().addAll(
                ticketCodeLabel, ticketCodeField,
                refundTicketButton
        );
    }
}
