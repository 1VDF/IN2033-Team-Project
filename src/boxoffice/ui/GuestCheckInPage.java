package boxoffice.ui;

import boxoffice.BoxOfficeManager;
import boxoffice.models.Ticket;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class GuestCheckInPage extends VBox {

    private final BoxOfficeManager boxOfficeManager;

    public GuestCheckInPage(BoxOfficeManager boxOfficeManager) {
        this.boxOfficeManager = boxOfficeManager;

        // Title
        Label title = new Label("Guest Check-In");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        title.setStyle("-fx-text-fill: #333333;");

        // Ticket code input
        Label ticketCodeLabel = new Label("Enter Ticket ID / Barcode:");
        TextField ticketCodeField = new TextField();
        ticketCodeField.setPromptText("Ticket ID / Barcode");

        Button checkInButton = new Button("Check-In Ticket");
        checkInButton.setOnAction(e -> {
            String ticketCode = ticketCodeField.getText();
            Ticket ticket = boxOfficeManager.getTicketByCode(ticketCode);
            if (ticket != null) {
                System.out.println("Ticket Checked-In: " + ticket.getTicketCode());
            } else {
                System.out.println("Ticket Not Found.");
            }
        });

        // Layout setup
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20;");
        layout.getChildren().addAll(title, ticketCodeLabel, ticketCodeField, checkInButton);

        this.getChildren().add(layout);
    }
}
