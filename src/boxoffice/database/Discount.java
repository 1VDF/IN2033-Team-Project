package boxoffice.database;
import java.time.LocalDate;

public class Discount {
    private int discountId;
    private String discountType; // "NHS", "Military", "Student", "Staff"
    private double discountValue;
    private boolean isPercentage;

    // Constructors

    public Discount(int discountId, String discountType, double discountPercentage,
                    boolean isPercentage) {
        this.discountId = discountId;
        this.discountType = discountType;
        this.discountValue = discountPercentage;
        this.isPercentage = isPercentage;
    }

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public boolean isPercentage() {
        return isPercentage;
    }

    public void setPercentage(boolean percentage) {
        isPercentage = percentage;
    }

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