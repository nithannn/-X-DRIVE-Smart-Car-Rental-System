/** Sedan: receives a 5% discount for rentals of seven days or more. */
public class Sedan extends Car {
    private static final long serialVersionUID = 1L;
    private int bootCapacityLitres;

    public Sedan(String id, String brand, String model, int year, String fuel,
                 double rate, double mileage, boolean available, int bootCapacityLitres) {
        super(id, brand, model, year, fuel, rate, mileage, available);
        setBootCapacityLitres(bootCapacityLitres);
    }

    public int getBootCapacityLitres() { return bootCapacityLitres; }
    public void setBootCapacityLitres(int value) {
        if (value < 0) throw new IllegalArgumentException("Boot capacity cannot be negative.");
        bootCapacityLitres = value;
    }
    @Override public String getCategory() { return "Sedan"; }
    @Override public double calculateRentalPrice(int days) {
        double total = super.calculateRentalPrice(days);
        return days >= 7 ? total * 0.95 : total;
    }
}
