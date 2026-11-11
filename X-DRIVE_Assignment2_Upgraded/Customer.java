import java.io.Serializable;

/** Stores the personal and driving-licence details of a registered customer. */
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    private String customerId, name, phone, licenceNumber;

    public Customer(String id, String name, String phone, String licence) {
        setCustomerId(id); setName(name); setPhone(phone); setLicenceNumber(licence);
    }
    private String required(String value, String label) {
        if (value == null || value.trim().isEmpty())
            throw new IllegalArgumentException(label + " is required.");
        return value.trim();
    }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String v) { customerId = required(v, "Customer ID"); }
    public String getName() { return name; }
    public void setName(String v) { name = required(v, "Customer name"); }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String v) {
        String cleaned = required(v, "Phone number");
        if (!cleaned.matches("[0-9+() -]{7,18}"))
            throw new IllegalArgumentException("Enter a valid phone number.");
        phone = cleaned;
    }
    public String getLicenceNumber() { return licenceNumber; }
    public void setLicenceNumber(String v) { licenceNumber = required(v, "Driving licence"); }
    @Override public String toString() { return customerId + " - " + name; }
}
