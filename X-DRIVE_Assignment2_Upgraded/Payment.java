import java.io.Serializable;
import java.time.LocalDateTime;

/** Stores a simulated payment record for a rental. */
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String paymentId, rentalId;
    private final double amount;
    private final PaymentMethod method;
    private final LocalDateTime paymentDate;
    private String status, transactionReference;

    public Payment(String id, Rental rental, PaymentMethod method) {
        if (id == null || id.trim().isEmpty()) throw new IllegalArgumentException("Payment ID is required.");
        if (rental == null) throw new IllegalArgumentException("Rental is required.");
        if (method == null) throw new IllegalArgumentException("Select a payment method.");
        paymentId = id;
        rentalId = rental.getRentalId();
        amount = rental.getTotalAmount();
        this.method = method;
        paymentDate = LocalDateTime.now();
        status = "PENDING";
    }
    public void processPayment() {
        if (!"PENDING".equals(status)) throw new IllegalStateException("Payment has already been processed.");
        status = "PAID (DEMO)";
        transactionReference = "XDR-" + paymentId;
    }
    public String getPaymentId() { return paymentId; }
    public String getRentalId() { return rentalId; }
    public double getAmount() { return amount; }
    public PaymentMethod getMethod() { return method; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public String getStatus() { return status; }
    public String getTransactionReference() { return transactionReference; }
}
