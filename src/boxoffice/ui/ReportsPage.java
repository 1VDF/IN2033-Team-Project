package boxoffice.ui;

import boxoffice.BoxOfficeManager;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ReportsPage extends VBox {

    private BoxOfficeManager boxOfficeManager;

    public ReportsPage(BoxOfficeManager boxOfficeManager) {
        this.boxOfficeManager = boxOfficeManager;

        Label reportLabel = new Label("Generate Report:");

        Button ticketSalesReportButton = new Button("Ticket Sales Report");
        ticketSalesReportButton.setOnAction(e -> {
            generateTicketSalesReport();
        });

        Button groupBookingsReportButton = new Button("Group Bookings Report");
        groupBookingsReportButton.setOnAction(e -> {
            generateGroupBookingsReport();
        });

        getChildren().addAll(
                reportLabel,
                ticketSalesReportButton,
                groupBookingsReportButton
        );
    }

    private void generateTicketSalesReport() {
        System.out.println("Generating Ticket Sales Report...");
    }

    private void generateGroupBookingsReport() {
        System.out.println("Generating Group Bookings Report...");
    }
}
