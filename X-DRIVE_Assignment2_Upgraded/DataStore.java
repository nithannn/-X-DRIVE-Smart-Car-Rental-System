import java.io.*;
import java.util.*;

/** Saves and loads application records in one local data file. */
public class DataStore {
    private final File dataFile;

    public DataStore() {
        File folder = new File("data");
        if (!folder.exists()) folder.mkdirs();
        dataFile = new File(folder, "xdrive-data.dat");
    }

    public void save(CarRentalApp app) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            out.writeObject(new ArrayList<Car>(app.getCars()));
            out.writeObject(new ArrayList<Customer>(app.getCustomers()));
            out.writeObject(new ArrayList<Rental>(app.getRentals()));
            out.writeObject(new ArrayList<Payment>(app.getPayments()));
            out.writeInt(app.getCustomerSequence());
            out.writeInt(app.getRentalSequence());
            out.writeInt(app.getPaymentSequence());
            out.writeInt(app.getVehicleSequence());
        } catch (IOException ex) {
            throw new IllegalStateException("Could not save X-DRIVE data: " + ex.getMessage(), ex);
        }
    }

    @SuppressWarnings("unchecked")
    public boolean load(CarRentalApp app) {
        if (!dataFile.exists()) return false;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(dataFile))) {
            app.replaceCars((ArrayList<Car>) in.readObject());
            app.replaceCustomers((ArrayList<Customer>) in.readObject());
            app.replaceRentals((ArrayList<Rental>) in.readObject());
            app.replacePayments((ArrayList<Payment>) in.readObject());
            app.setCustomerSequence(in.readInt());
            app.setRentalSequence(in.readInt());
            app.setPaymentSequence(in.readInt());
            app.setVehicleSequence(in.readInt());
            return true;
        } catch (IOException | ClassNotFoundException | ClassCastException ex) {
            throw new IllegalStateException("Saved data could not be loaded. " + ex.getMessage(), ex);
        }
    }
}
