package boxoffice.database;
import java.time.LocalDate;

public class Discount {
    private int discountId;
    private String discountType; // "NHS", "Military", "Student", "Staff"
    private double discountPercentage;
    private LocalDate validFrom;
    private LocalDate validUntil;

    // Constructors

    public Discount(int discountId, String discountType, double discountPercentage,
                    LocalDate validFrom, LocalDate validUntil) {
        this.discountId = discountId;
        this.discountType = discountType;
        this.discountPercentage = discountPercentage;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    // Getters and Setters
    public int getDiscountId() { return discountId; }
    public void setDiscountId(int discountId) { this.discountId = discountId; }

    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }

    public double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(double discountPercentage) { this.discountPercentage = discountPercentage; }

    public LocalDate getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDate validFrom) { this.validFrom = validFrom; }

    public LocalDate getValidUntil() { return validUntil; }
    public void setValidUntil(LocalDate validUntil) { this.validUntil = validUntil; }
}