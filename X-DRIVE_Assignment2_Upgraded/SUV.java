/** SUV: applies a 10% category surcharge to the base rental price. */
public class SUV extends Car {
    private static final long serialVersionUID = 1L;
    private boolean fourWheelDrive;

    public SUV(String id, String brand, String model, int year, String fuel,
               double rate, double mileage, boolean available, boolean fourWheelDrive) {
        super(id, brand, model, year, fuel, rate, mileage, available);
        this.fourWheelDrive = fourWheelDrive;
    }
    public boolean isFourWheelDrive() { return fourWheelDrive; }
    public void setFourWheelDrive(boolean value) { fourWheelDrive = value; }
    @Override public String getCategory() { return "SUV"; }
    @Override public double calculateRentalPrice(int days) {
        return super.calculateRentalPrice(days) * 1.10;
    }
}
