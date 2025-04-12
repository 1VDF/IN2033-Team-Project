package boxoffice.database;

import java.time.LocalDate;

/**
 * Represents a discount offered in the box office system.
 * This class holds information about the discount's ID, type, value, and whether it is a percentage-based discount.
 */
public class Discount {
    private int discountId;
    private String discountType; // "NHS", "Military", "Student", "Staff"
    private double discountValue;
    private boolean isPercentage;

    /**
     * Constructs a new Discount instance.
     *
     * @param discountId      The unique ID for the discount.
     * @param discountType    The type of discount (e.g., "NHS", "Military", "Student", "Staff").
     * @param discountPercentage The value of the discount (either as a fixed amount or a percentage).
     * @param isPercentage    A flag indicating if the discount is percentage-based (true) or a fixed amount (false).
     */
    public Discount(int discountId, String discountType, double discountPercentage, boolean isPercentage) {
        this.discountId = discountId;
        this.discountType = discountType;
        this.discountValue = discountPercentage;
        this.isPercentage = isPercentage;
    }

    // Getters and Setters

    /**
     * @return The unique ID of the discount.
     */
    public int getDiscountId() {
        return discountId;
    }

    /**
     * @param discountId The discount ID to set.
     */
    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    /**
     * @return The type of discount (e.g., "NHS", "Military", "Student", "Staff").
     */
    public String getDiscountType() {
        return discountType;
    }

    /**
     * @param discountType The discount type to set.
     */
    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    /**
     * @return The value of the discount (either as a fixed amount or percentage).
     */
    public double getDiscountValue() {
        return discountValue;
    }

    /**
     * @param discountValue The discount value to set.
     */
    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    /**
     * @return A boolean indicating if the discount is percentage-based (true) or fixed (false).
     */
    public boolean isPercentage() {
        return isPercentage;
    }

    /**
     * @param percentage A boolean indicating if the discount is percentage-based (true) or fixed (false).
     */
    public void setPercentage(boolean percentage) {
        isPercentage = percentage;
    }

    /**
     * Returns a string representation of the Discount object.
     *
     * @return A string representing the Discount object.
     */
    @Override
    public String toString() {
        return "entity.discount{" +
                "discountId=" + discountId +
                ", discountType='" + discountType + '\'' +
                ", discountValue=" + discountValue +
                ", isPercentage=" + isPercentage +
                '}';
    }
}
