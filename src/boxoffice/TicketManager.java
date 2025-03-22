package boxoffice;

import boxoffice.models.Ticket;
import java.util.ArrayList;
import java.util.List;

public class TicketManager {
    private List<Ticket> tickets;

    public TicketManager() {
        this.tickets = new ArrayList<>();
    }

    // Method to refund a ticket based on the ticket code
    public boolean refundTicket(String ticketCode) {
        for (Ticket ticket : tickets) {
            if (ticket.getTicketCode().equals(ticketCode)) {
                ticket.setRefunded(true);
                return true;
            }
        }
        return false;
    }

    // Method to sell a ticket
    public void sellTicket(String ticketCode, String showName, String seat, String customerName) {
        Ticket newTicket = new Ticket(ticketCode, showName, seat, customerName);
        tickets.add(newTicket);
    }

    public List<Ticket> getTickets() {
        return tickets;
    }
}
