////package boxoffice;
////
////import boxoffice.database.Staff;
////import boxoffice.database.TicketSale;
////import boxoffice.models.Show;
////import boxoffice.models.TicketSaleRepository;
////
////import java.sql.SQLException;
////import java.util.List;
////
////public class BoxOfficeManager {
////    private ShowManager showManager;
////
////    public BoxOfficeManager() {
////        this.showManager = new ShowManager();
////    }
////
////    public boolean refundTicket(String ticketSaleId) {
////        // Use TicketSaleRepository to mark the ticket as refunded
////        try {
////            TicketSale ticketSale = TicketSaleRepository.getTicketSaleById(ticketSaleId);
////            if (ticketSale != null) {
////                // Mark as refunded
////                return TicketSaleRepository.markAsRefunded(ticketSale.getTicketSaleId());
////            }
////            return false; // Ticket not found
////        } catch (SQLException e) {
////            System.err.println("Error while processing refund: " + e.getMessage());
////            return false;
////        }
////    }
////
////    public void sellTicket(String ticketSaleId, String showName, String seat, String customerName) {
////        // Assume some logic to sell the ticket
////        // Typically, you might create a TicketSale and add it to the database
////    }
////
////    public TicketSale getTicketById(String ticketSaleId) {
////        // Retrieve the TicketSale object from the database using the ticketSaleId
////        try {
////            return TicketSaleRepository.getTicketSaleById(ticketSaleId);
////        } catch (SQLException e) {
////            // Handle the SQLException
////            System.err.println("Error fetching ticket with ID " + ticketSaleId + ": " + e.getMessage());
////            return null; // Return null or handle appropriately
////        }
////    }
////
////    public List<Show> getAvailableShows() {
////        return showManager.getAvailableShows(); // Fetch available shows from ShowManager
////    }
////
////    public Show getShowByName(String showName) {
////        return showManager.getShowByName(showName); // Fetch a specific show by name
////    }
////
////    public void createGroupBooking(String groupName, List<String> bookedSeats) {
////        // Logic to create a group booking (can be implemented as needed)
////    }
////
////    public List<TicketSale> getRecentTickets() {
////        // Returns a list of recent ticket sales (could be from the TicketSaleRepository)
////        try {
////            // Assuming you have a method that fetches all tickets
////            return TicketSaleRepository.getAllTicketSales();
////        } catch (SQLException e) {
////            System.err.println("Error fetching recent tickets: " + e.getMessage());
////            return null; // Return null or handle appropriately
////        }
////    }
////
////    public Staff getCurrentStaff() {
////        // Return details of the current staff, can be assumed static or fetched from a session
////        return new Staff(1, "John", "Doe", "Manager", "john.doe@example.com", "password123");
////    }
////
////    // Add this method to retrieve a ticket by its ticket code
////
////}
//
//
//package boxoffice;
//
//import boxoffice.database.Staff;
//import boxoffice.database.TicketSale;
//import boxoffice.models.Show;
//import boxoffice.models.TicketSaleRepository;
//
//import java.sql.SQLException;
//import java.util.List;
//
//public class BoxOfficeManager {
//    private ShowManager showManager;
//
//    public BoxOfficeManager() {
//        this.showManager = new ShowManager();
//    }
//
//    public boolean refundTicket(String ticketSaleId) {
//        try {
//            TicketSale ticketSale = TicketSaleRepository.getTicketSaleById(ticketSaleId);
//            if (ticketSale != null) {
//                return TicketSaleRepository.markAsRefunded(ticketSale.getTicketSaleId());
//            }
//            return false;
//        } catch (SQLException e) {
//            System.err.println("Error while processing refund: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public void sellTicket(String ticketSaleId, String showName, String seat, String customerName) {
//        // Logic for selling ticket
//    }
//
//    public TicketSale getTicketById(String ticketSaleId) {
//        try {
//            return TicketSaleRepository.getTicketSaleById(ticketSaleId);
//        } catch (SQLException e) {
//            System.err.println("Error fetching ticket with ID " + ticketSaleId + ": " + e.getMessage());
//            return null;
//        }
//    }
//
//    public List<Show> getAvailableShows() {
//        return showManager.getAvailableShows();
//    }
//
//    public Show getShowByName(String showName) {
//        return showManager.getShowByName(showName);
//    }
//
//    public void createGroupBooking(String groupName, List<String> bookedSeats) {
//        // Logic for group booking
//    }
//
//    public List<TicketSale> getRecentTickets() {
//        try {
//            return TicketSaleRepository.getAllTicketSales();
//        } catch (SQLException e) {
//            System.err.println("Error fetching recent tickets: " + e.getMessage());
//            return null;
//        }
//    }
//
//    public Staff getCurrentStaff() {
//        return new Staff(1, "John", "Doe", "Manager", "john.doe@example.com", "password123");
//    }
//}

package boxoffice;

import boxoffice.database.Staff;
import boxoffice.database.TicketSale;
import boxoffice.database.Performance;
import boxoffice.models.PerformanceRepository;
import boxoffice.models.TicketSaleRepository;

import java.sql.SQLException;
import java.util.List;

public class BoxOfficeManager {
    private PerformanceRepository performanceRepository;

    public BoxOfficeManager() {
        this.performanceRepository = new PerformanceRepository();
    }

    public boolean refundTicket(String ticketSaleId) {
        try {
            TicketSale ticketSale = TicketSaleRepository.getTicketSaleById(ticketSaleId);
            if (ticketSale != null) {
                return TicketSaleRepository.markAsRefunded(ticketSale.getTicketSaleId());
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error while processing refund: " + e.getMessage());
            return false;
        }
    }

    public void sellTicket(String ticketSaleId, int performanceId, String seat, String customerName) {
        // Logic for selling ticket
        try {
            Performance performance = performanceRepository.getPerformanceById(performanceId);
            if (performance == null) {
                System.out.println("Performance not found.");
                return;
            }
            // Logic to reserve seat and create TicketSale entry
        } catch (SQLException e) {
            System.err.println("Error while selling ticket: " + e.getMessage());
        }
    }

    public TicketSale getTicketById(String ticketSaleId) {
        try {
            return TicketSaleRepository.getTicketSaleById(ticketSaleId);
        } catch (SQLException e) {
            System.err.println("Error fetching ticket with ID " + ticketSaleId + ": " + e.getMessage());
            return null;
        }
    }

    public List<Performance> getAvailablePerformances() {
        try {
            return performanceRepository.getAllPerformances();
        } catch (SQLException e) {
            System.err.println("Error fetching performances: " + e.getMessage());
            return null;
        }
    }

    public Performance getPerformanceById(int performanceId) {
        try {
            return performanceRepository.getPerformanceById(performanceId);
        } catch (SQLException e) {
            System.err.println("Error fetching performance: " + e.getMessage());
            return null;
        }
    }

    public void createGroupBooking(String groupName, List<String> bookedSeats, int performanceId) {
        // Logic for group booking
    }

    public List<TicketSale> getRecentTickets() {
        try {
            return TicketSaleRepository.getAllTicketSales();
        } catch (SQLException e) {
            System.err.println("Error fetching recent tickets: " + e.getMessage());
            return null;
        }
    }

    public Staff getCurrentStaff() {
        return new Staff(1, "John", "Doe", "Manager", "john.doe@example.com", "password123");
    }
}
