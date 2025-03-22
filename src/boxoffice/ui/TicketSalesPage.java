package boxoffice.ui;

import boxoffice.BoxOfficeManager;
import boxoffice.models.Show;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;
import java.util.UUID;

public class TicketSalesPage extends VBox {

    private BoxOfficeManager boxOfficeManager;

    public TicketSalesPage(BoxOfficeManager boxOfficeManager) {
        this.boxOfficeManager = boxOfficeManager;

        Label showLabel = new Label("Select Show:");
        ComboBox<String> showComboBox = new ComboBox<>();
        List<Show> shows = boxOfficeManager.getAvailableShows();
        for (Show show : shows) {
            showComboBox.getItems().add(show.getShowName());
        }

        Label seatLabel = new Label("Select Seat:");
        ComboBox<String> seatComboBox = new ComboBox<>();
        showComboBox.setOnAction(e -> {
            String selectedShowName = showComboBox.getValue();
            Show selectedShow = boxOfficeManager.getShowByName(selectedShowName);
            if (selectedShow != null) {
                seatComboBox.getItems().setAll(selectedShow.getAvailableSeats());
            }
        });

        Label ticketQuantityLabel = new Label("Ticket Quantity:");
        Spinner<Integer> ticketQuantitySpinner = new Spinner<>(1, 12, 1);

        Button sellTicketButton = new Button("Sell Ticket");
        sellTicketButton.setOnAction(e -> {
            String ticketCode = UUID.randomUUID().toString();
            String showName = showComboBox.getValue();
            String seat = seatComboBox.getValue();
            String customerName = "Customer";
            boxOfficeManager.sellTicket(ticketCode, showName, seat, customerName);
            System.out.println("Sold ticket for: " + showName + ", Seat: " + seat);
        });

        getChildren().addAll(
                showLabel, showComboBox,
                seatLabel, seatComboBox,
                ticketQuantityLabel, ticketQuantitySpinner,
                sellTicketButton
        );
    }
}
