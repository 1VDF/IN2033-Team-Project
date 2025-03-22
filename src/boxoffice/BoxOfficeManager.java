package boxoffice;

import boxoffice.models.Ticket;
import boxoffice.models.Show;
import java.util.List;

public class BoxOfficeManager {
    private TicketManager ticketManager;
    private ShowManager showManager;

    public BoxOfficeManager() {
        this.ticketManager = new TicketManager();
        this.showManager = new ShowManager();
    }

    public boolean refundTicket(String ticketCode) {
        return ticketManager.refundTicket(ticketCode);
    }

    public void sellTicket(String ticketCode, String showName, String seat, String customerName) {
        ticketManager.sellTicket(ticketCode, showName, seat, customerName);
    }

    public Ticket getTicketByCode(String ticketCode) {
        // Call the TicketManager to find the ticket by its code
        for (Ticket ticket : ticketManager.getTickets()) {
            if (ticket.getTicketCode().equals(ticketCode)) {
                return ticket;
            }
        }
        return null;
    }

    public List<Show> getAvailableShows() {
        return showManager.getAvailableShows();
    }

    public Show getShowByName(String showName) {
        return showManager.getShowByName(showName);
    }

    public void createGroupBooking(String groupName, List<String> bookedSeats) {
    }
}
