import java.io.Serializable;

/** Base class for every vehicle managed by X-DRIVE. */
public class Car implements Rentable, Serializable {
    private static final long serialVersionUID = 1L;
    private String carId, brand, model, fuelType;
    private int year;
    private double dailyRate, mileage;
    private boolean available;

    public Car() {
        this("CAR-000", "Unknown", "Unspecified", 2024, "Petrol", 100.0, 0.0, true);
    }

    public Car(String carId, String brand, String model, int year, String fuelType,
               double dailyRate, double mileage, boolean available) {
        setCarId(carId); setBrand(brand); setModel(model); setYear(year);
        setFuelType(fuelType); setDailyRate(dailyRate); setMileage(mileage);
        this.available = available;
    }

    public String getCarId() { return carId; }
    public void setCarId(String v) { carId = required(v, "Vehicle ID"); }
    public String getBrand() { return brand; }
    public void setBrand(String v) { brand = required(v, "Brand"); }
    public String getModel() { return model; }
    public void setModel(String v) { model = required(v, "Model"); }
    public int getYear() { return year; }
    public void setYear(int v) {
        if (v < 1990 || v > 2100) throw new IllegalArgumentException("Enter a valid vehicle year.");
        year = v;
    }
    public String getFuelType() { return fuelType; }
    public void setFuelType(String v) { fuelType = required(v, "Fuel type"); }
    public double getDailyRate() { return dailyRate; }
    public void setDailyRate(double v) {
        if (v <= 0 || Double.isNaN(v) || Double.isInfinite(v))
            throw new IllegalArgumentException("Daily rate must be greater than RM0.");
        dailyRate = v;
    }
    public double getMileage() { return mileage; }
    public void setMileage(double v) {
        if (v < 0) throw new IllegalArgumentException("Mileage cannot be negative.");
        mileage = v;
    }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean v) { available = v; }
    public String getCategory() { return "Standard"; }

    private String required(String value, String label) {
        if (value == null || value.trim().isEmpty())
            throw new IllegalArgumentException(label + " is required.");
        return value.trim();
    }

    @Override
    public double calculateRentalPrice(int days) {
        if (days <= 0) throw new IllegalArgumentException("Rental days must be at least one.");
        return dailyRate * days;
    }

    public boolean needsService() { return mileage >= 10000; }
    public String getDisplayName() { return brand + " " + model; }

    @Override
    public String toString() {
        return carId + " - " + getDisplayName() + " (" + getCategory() + ")";
    }
}
