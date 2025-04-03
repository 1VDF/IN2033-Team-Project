package boxoffice.ui;

import boxoffice.BoxOfficeManager;
import boxoffice.database.TicketSale;
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

        // Ticket ID input
        Label ticketCodeLabel = new Label("Enter Ticket ID:");
        TextField ticketCodeField = new TextField();
        ticketCodeField.setPromptText("Ticket ID");

        Button checkInButton = new Button("Check-In Ticket");
        checkInButton.setOnAction(e -> {
            String ticketId = ticketCodeField.getText(); // Using Ticket ID instead of code
            TicketSale ticketSale = boxOfficeManager.getTicketById(ticketId); // Use getTicketById method
            if (ticketSale != null) {
                System.out.println("Ticket Checked-In: " + ticketSale.getTicketSaleId());
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
