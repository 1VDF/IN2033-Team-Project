package boxoffice;

import boxoffice.database.Staff;
import boxoffice.database.TicketSale;
import boxoffice.models.PerformanceRepository;
import boxoffice.models.TicketSaleRepository;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.util.List;

public class BoxOfficeManager {
    private PerformanceRepository performanceRepository;
    private Staff currentStaff;


    public BoxOfficeManager() {
        this.performanceRepository = new PerformanceRepository();
    }

    public void setCurrentStaff(Staff staff) {
        this.currentStaff = staff;
    }

    public Staff getCurrentStaff() {
        return currentStaff;
    }

    public boolean isManager() {
        return currentStaff != null &&
                ("Manager".equals(currentStaff.getRole()) ||
                        "Deputy Manager".equals(currentStaff.getRole()));
    }


    public TicketSale getTicketById(String ticketSaleId) {
        try {
            return TicketSaleRepository.getTicketSaleById(ticketSaleId);
        } catch (SQLException e) {
            System.err.println("Error fetching ticket with ID " + ticketSaleId + ": " + e.getMessage());
            return null;
        }
    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}