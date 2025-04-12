package boxoffice.database;

import boxoffice.models.CustomerRepository;
import boxoffice.models.PerformanceRepository;

import java.sql.SQLException;

/**
 * Represents a ticket sale in the box office system.
 * This class contains information about the ticket, the customer,
 * the performance, and booking details.
 */
public class TicketSale {
    private int ticketSaleId;
    private double price;
    private int performanceID;
    private String seatID;
    private int discountID;
    private int groupBookingID;
    private int staffID;
    private String customerID;
    private boolean checkedIn;

    private transient String customerName;

    /**
     * Constructs a TicketSale object with the specified details.
     *
     * @param ticketSaleId  The unique identifier for the ticket sale.
     * @param price         The price of the ticket.
     * @param customerID    The unique identifier for the customer who purchased the ticket.
     * @param performanceID The performance ID associated with the ticket.
     * @param seatID        The seat ID associated with the ticket.
     * @param discountID    The discount ID applied to the ticket.
     * @param groupBookingID The group booking ID if the ticket was part of a group booking.
     * @param staffID       The ID of the staff member who processed the sale.
     */
    public TicketSale(int ticketSaleId, double price, String customerID, int performanceID, String seatID,
                      int discountID, int groupBookingID, int staffID) {
        this.ticketSaleId = ticketSaleId;
        this.price = price;
        this.performanceID = performanceID;
        this.seatID = seatID;
        this.discountID = discountID;
        this.groupBookingID = groupBookingID;
        this.staffID = staffID;
        this.customerID = customerID;
        this.checkedIn = false;
    }

    // Getters and Setters

    /**
     * Gets the checked-in status of the ticket.
     *
     * @return true if the ticket is checked-in, false otherwise.
     */
    public boolean isCheckedIn() {
        return checkedIn;
    }

    /**
     * Sets the checked-in status of the ticket.
     *
     * @param checkedIn The checked-in status of the ticket.
     */
    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    /**
     * Gets the ticket sale ID.
     *
     * @return The ticket sale ID.
     */
    public int getTicketSaleId() {
        return ticketSaleId;
    }

    /**
     * Sets the ticket sale ID.
     *
     * @param ticketSaleId The ticket sale ID.
     */
    public void setTicketSaleId(int ticketSaleId) {
        this.ticketSaleId = ticketSaleId;
    }

    /**
     * Gets the price of the ticket.
     *
     * @return The ticket price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the ticket.
     *
     * @param price The ticket price.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the performance ID associated with the ticket.
     *
     * @return The performance ID.
     */
    public int getPerformanceID() {
        return performanceID;
    }

    /**
     * Sets the performance ID associated with the ticket.
     *
     * @param performanceID The performance ID.
     */
    public void setPerformanceID(int performanceID) {
        this.performanceID = performanceID;
    }

    /**
     * Gets the seat ID associated with the ticket.
     *
     * @return The seat ID.
     */
    public String getSeatID() {
        return seatID;
    }

    /**
     * Sets the seat ID associated with the ticket.
     *
     * @param seatID The seat ID.
     */
    public void setSeatID(String seatID) {
        this.seatID = seatID;
    }

    /**
     * Gets the discount ID applied to the ticket.
     *
     * @return The discount ID.
     */
    public int getDiscountID() {
        return discountID;
    }

    /**
     * Sets the discount ID applied to the ticket.
     *
     * @param discountID The discount ID.
     */
    public void setDiscountID(int discountID) {
        this.discountID = discountID;
    }

    /**
     * Gets the group booking ID if the ticket is part of a group booking.
     *
     * @return The group booking ID.
     */
    public int getGroupBookingID() {
        return groupBookingID;
    }

    /**
     * Sets the group booking ID if the ticket is part of a group booking.
     *
     * @param groupBookingID The group booking ID.
     */
    public void setGroupBookingID(int groupBookingID) {
        this.groupBookingID = groupBookingID;
    }

    /**
     * Gets the staff ID who processed the sale.
     *
     * @return The staff ID.
     */
    public int getStaffID() {
        return staffID;
    }

    /**
     * Sets the staff ID who processed the sale.
     *
     * @param staffID The staff ID.
     */
    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    /**
     * Gets the customer ID associated with the ticket sale.
     *
     * @return The customer ID.
     */
    public String getCustomerID() {
        return customerID;
    }

    /**
     * Sets the customer ID associated with the ticket sale.
     *
     * @param customerID The customer ID.
     */
    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    /**
     * Fetches the customer's name based on their customer ID.
     *
     * @return The customer's name, or "Unknown" if the customer cannot be found.
     */
    public String getCustomerName() {
        try {
            Customer customer = CustomerRepository.getCustomerById(this.customerID);
            return (customer != null) ? customer.getCustomerName() : "Unknown";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    /**
     * Fetches the performance title based on the performance ID.
     *
     * @return The performance title, or "Unknown" if the performance cannot be found.
     */
    public String getPerformanceTitle() {
        try {
            Performance performance = PerformanceRepository.getPerformanceById(this.performanceID);
            return (performance != null) ? performance.getTitle() : "Unknown";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    /**
     * Returns a string representation of the TicketSale object.
     * This includes the ticket sale ID, price, customer ID, performance ID, and other details.
     *
     * @return A string representation of the ticket sale.
     */
    @Override
    public String toString() {
        return "TicketSale{" +
                "ticketSaleId=" + ticketSaleId +
                ", price=" + price +
                ", customerId=" + customerID +
                ", performanceId=" + performanceID +
                ", seatId=" + seatID +
                ", discountId=" + discountID +
                ", groupId=" + groupBookingID +
                ", staffId=" + staffID +
                '}';
    }
}
