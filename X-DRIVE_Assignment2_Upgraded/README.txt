X-DRIVE — SMART CAR RENTAL & MANAGEMENT SYSTEM
================================================

REQUIREMENTS
- Windows 10 or Windows 11
- Java JDK 17 or later (JDK 19 is suitable)
- No external libraries, database server or internet connection required

PROJECT CONTENTS
- Car.java             Base class and encapsulated vehicle attributes
- Sedan.java           Derived class (inheritance and method overriding)
- SUV.java              Derived class (inheritance and method overriding)
- Rentable.java         Common interface (polymorphism)
- Customer.java         Customer model
- Rental.java            Rental model and vehicle-return logic
- Payment.java           Payment model
- PaymentMethod.java     Enum of simulated payment channels
- DataStore.java         Local file saving/loading
- CarRentalApp.java      Main controller and main() method
- CarRentalGUI.java      Java Swing graphical user interface

RUN ON WINDOWS 11
1. Extract the ZIP file.
2. Open the extracted X-DRIVE_Assignment2_Upgraded folder.
3. Click the folder address bar in File Explorer, type powershell and press Enter.
4. In PowerShell, compile all Java files:

   javac *.java

5. Start the program:

   java CarRentalApp

6. The first launch creates a "data" folder automatically.
   Your vehicles, customers, rentals, payments and sequence numbers are saved in:
   data\xdrive-data.dat

IMPORTANT
- Payment methods are simulated for this academic project. The program does NOT
  connect to Maybank, Touch 'n Go, DuitNow, Visa, Mastercard or any live payment gateway.
- Records are stored locally on the same computer. Keep the "data" folder with the
  project if you want to keep your records.
- Do not delete data\xdrive-data.dat if you want to preserve saved records.
- If you want a clean demonstration, close the program and temporarily rename the
  data folder to data_backup. Launch again to create fresh sample records.

TESTING CHECKLIST
1. Launch the program. Four sample vehicles and two sample customers should appear.
2. Vehicle Management > Add vehicle. Add a Proton X90 SUV at RM180/day.
3. Customers > Register customer. Add a test customer.
4. Rental & Payment. Select that customer and the Proton X90, choose 3 days,
   select Touch 'n Go eWallet, then confirm.
5. Expected SUV total: RM180 x 3 x 1.10 = RM594.00.
6. Open Returns & History. Confirm rental and payment appear.
7. Process vehicle return. The vehicle should become available again.
8. Close and reopen the application. Records should still be present.

OOP CONCEPTS TO EXPLAIN IN THE REPORT
- Encapsulation: model fields are private and accessed through getters/setters.
- Inheritance: Sedan and SUV extend the Car base class.
- Polymorphism: Car references can point to Sedan or SUV objects; the overridden
  calculateRentalPrice() method applies each category's pricing rule.
- Abstraction: Rentable defines the rental-price contract.
- Constructors: initialise valid objects and enforce basic validation.
- Control structures: if/else decisions and loops are used in the model/controller/GUI.
- Additional classes: Customer, Rental, Payment, PaymentMethod and DataStore.
