import javax.swing.*;
import java.time.LocalDate;
import java.util.*;

/** Main controller: coordinates the model classes, persistence and Swing interface. */
public class CarRentalApp {
    private final List<Car> cars = new ArrayList<Car>();
    private final List<Customer> customers = new ArrayList<Customer>();
    private final List<Rental> rentals = new ArrayList<Rental>();
    private final List<Payment> payments = new ArrayList<Payment>();
    private int customerSequence = 3, rentalSequence = 1, paymentSequence = 1, vehicleSequence = 5;
    private final DataStore store = new DataStore();
    private CarRentalGUI gui;

    public CarRentalApp() {
        boolean loaded = store.load(this);
        if (!loaded) {
            cars.add(new Sedan("XD-S001", "Proton", "Persona", 2024, "Petrol", 120, 12000, true, 510));
            cars.add(new SUV("XD-U001", "Proton", "X50", 2025, "Petrol", 180, 6500, true, false));
            cars.add(new SUV("XD-U002", "Perodua", "Ativa", 2024, "Petrol", 150, 9000, true, false));
            cars.add(new Sedan("XD-S002", "Honda", "City", 2023, "Petrol", 160, 18000, true, 506));
            customers.add(new Customer("CUS-001", "Aiman Hakim", "0123456789", "D1234567"));
            customers.add(new Customer("CUS-002", "Nur Aisyah", "01122334455", "D7654321"));
            save();
        }
    }

    public List<Car> getCars() { return Collections.unmodifiableList(cars); }
    public List<Customer> getCustomers() { return Collections.unmodifiableList(customers); }
    public List<Rental> getRentals() { return Collections.unmodifiableList(rentals); }
    public List<Payment> getPayments() { return Collections.unmodifiableList(payments); }

    public int getCustomerSequence() { return customerSequence; }
    public int getRentalSequence() { return rentalSequence; }
    public int getPaymentSequence() { return paymentSequence; }
    public int getVehicleSequence() { return vehicleSequence; }
    public void setCustomerSequence(int v) { customerSequence = v; }
    public void setRentalSequence(int v) { rentalSequence = v; }
    public void setPaymentSequence(int v) { paymentSequence = v; }
    public void setVehicleSequence(int v) { vehicleSequence = v; }
    public void replaceCars(ArrayList<Car> v) { cars.clear(); cars.addAll(v); }
    public void replaceCustomers(ArrayList<Customer> v) { customers.clear(); customers.addAll(v); }
    public void replaceRentals(ArrayList<Rental> v) { rentals.clear(); rentals.addAll(v); }
    public void replacePayments(ArrayList<Payment> v) { payments.clear(); payments.addAll(v); }

    public void save() { store.save(this); }

    public Customer addCustomer(String name, String phone, String licence) {
        Customer c = new Customer(String.format("CUS-%03d", customerSequence++), name, phone, licence);
        customers.add(c); save(); return c;
    }

    public Car addCar(String category, String brand, String model, int year, String fuel, double rate) {
        String id = String.format("XD-%04d", vehicleSequence++);
        Car car;
        if ("SUV".equalsIgnoreCase(category))
            car = new SUV(id, brand, model, year, fuel, rate, 0, true, false);
        else
            car = new Sedan(id, brand, model, year, fuel, rate, 0, true, 450);
        cars.add(car); save(); return car;
    }

    public Rental createRental(Customer customer, Car car, int days) {
        Rental rental = new Rental(String.format("RNT-%04d", rentalSequence++),
                customer, car, days, LocalDate.now());
        rentals.add(rental);
        save();
        return rental;
    }

    public Payment pay(Rental rental, PaymentMethod method) {
        Payment payment = new Payment(String.format("PAY-%04d", paymentSequence++), rental, method);
        payment.processPayment();
        payments.add(payment); save(); return payment;
    }

    public void returnRental(Rental rental) { rental.completeReturn(); save(); }

    public double totalRevenue() {
        double total = 0;
        for (Payment payment : payments)
            if (payment.getStatus().startsWith("PAID")) total += payment.getAmount();
        return total;
    }

    public int availableVehicleCount() {
        int count = 0;
        for (Car car : cars) if (car.isAvailable()) count++;
        return count;
    }

    public int activeRentalCount() {
        int count = 0;
        for (Rental rental : rentals) if ("ACTIVE".equals(rental.getStatus())) count++;
        return count;
    }

    public void show() {
        gui = new CarRentalGUI(this);
        gui.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) { }
            new CarRentalApp().show();
        });
    }
}
