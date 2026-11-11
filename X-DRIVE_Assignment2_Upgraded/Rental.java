import java.io.Serializable;
import java.time.LocalDate;

/** Represents the relationship between a customer, a car and the rental period. */
public class Rental implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String rentalId;
    private final Customer customer;
    private final Car car;
    private final int days;
    private final LocalDate startDate;
    private final double totalAmount;
    private String status;

    public Rental(String id, Customer customer, Car car, int days, LocalDate startDate) {
        if (id == null || id.trim().isEmpty()) throw new IllegalArgumentException("Rental ID is required.");
        if (customer == null) throw new IllegalArgumentException("Select a customer.");
        if (car == null) throw new IllegalArgumentException("Select a vehicle.");
        if (days <= 0) throw new IllegalArgumentException("Rental days must be at least one.");
        if (startDate == null) throw new IllegalArgumentException("Start date is required.");
        if (!car.isAvailable()) throw new IllegalStateException("This vehicle is not available.");
        this.rentalId = id;
        this.customer = customer;
        this.car = car;
        this.days = days;
        this.startDate = startDate;
        this.totalAmount = car.calculateRentalPrice(days);
        this.status = "ACTIVE";
        car.setAvailable(false);
    }
    public String getRentalId() { return rentalId; }
    public Customer getCustomer() { return customer; }
    public Car getCar() { return car; }
    public int getDays() { return days; }
    public LocalDate getStartDate() { return startDate; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public void completeReturn() {
        if (!"ACTIVE".equals(status)) throw new IllegalStateException("This rental has already been returned.");
        status = "RETURNED";
        car.setAvailable(true);
    }
}
