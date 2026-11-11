
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

/**
 * Complete Swing user interface for X-DRIVE.
 * Features:
 * Dashboard, vehicle management, customers,
 * rental and payment, returns and history.
 */
public class CarRentalGUI extends JFrame {

    private final CarRentalApp app;

    // Main application colours
    private final Color NAVY = new Color(12, 19, 38);
    private final Color PANEL = new Color(24, 34, 59);
    private final Color PANEL2 = new Color(31, 44, 73);
    private final Color BLUE = new Color(42, 133, 255);
    private final Color BLUE_HOVER = new Color(70, 155, 255);
    private final Color PURPLE = new Color(135, 91, 255);
    private final Color BG = new Color(13, 20, 39);
    private final Color WHITE = new Color(245, 248, 255);
    private final Color MUTED = new Color(163, 177, 204);
    private final Color BORDER = new Color(49, 64, 94);

    private final NumberFormat money =
            NumberFormat.getCurrencyInstance(new Locale("en", "MY"));

    private JLabel vehiclesValue, availableValue, rentalsValue, revenueValue;

    private JTable vehicleTable, customerTable, rentalTable, paymentTable;

    private JComboBox<Customer> customerChoice;
    private JComboBox<Car> carChoice;
    private JComboBox<PaymentMethod> methodChoice;
    private JSpinner daysChoice;
    private JComboBox<Rental> returnChoice;

    private JTextArea receiptArea;

    private JPanel mainPanel;
    private CardLayout pageLayout;

    public CarRentalGUI(CarRentalApp app) {


    this.app = app;

    // Force Java to use its cross-platform Swing appearance.
    try {
        UIManager.setLookAndFeel(
            UIManager.getCrossPlatformLookAndFeelClassName()
        );
    } catch (Exception e) {
        System.out.println("Could not apply cross-platform theme.");
    }

    // Set consistent application colours.
    UIManager.put("Button.background", new Color(42, 133, 255));
    UIManager.put("Button.foreground", Color.WHITE);
    UIManager.put("Button.focus", new Color(135, 91, 255));

    setTitle("X-DRIVE | Smart Car Rental & Management");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setMinimumSize(new Dimension(1050, 700));
    setSize(1220, 780);
    setLocationRelativeTo(null);

    getContentPane().setBackground(BG);
    setLayout(new BorderLayout());

    add(buildSidebar(), BorderLayout.WEST);
    add(buildMain(), BorderLayout.CENTER);

    refreshAll();
}

    // ----------------------------------------------------
    // BUTTON STYLING
    // ----------------------------------------------------

    /**
     * Forces Swing buttons to display their assigned colours.
     * This prevents the operating system's default white
     * button appearance from overriding the dark theme.
     */
    private void styleButton(JButton button, Color normalColor) {

        button.setUI(new BasicButtonUI());

        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(WHITE);
        button.setBackground(normalColor);

        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.setBorder(
                new EmptyBorder(11, 15, 11, 15));

        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BLUE_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
            }
        });
    }

    /**
     * Creates a consistently styled action button.
     */
    private JButton actionButton(String text, Runnable action) {

        JButton button = new JButton(text);

        styleButton(button, BLUE);

        button.addActionListener(e -> action.run());

        return button;
    }

    // ----------------------------------------------------
    // SIDEBAR
    // ----------------------------------------------------

    private JPanel buildSidebar() {

        JPanel side = new JPanel();

        side.setBackground(NAVY);
        side.setPreferredSize(new Dimension(220, 0));

        side.setBorder(
                new EmptyBorder(25, 18, 20, 18));

        side.setLayout(
                new BoxLayout(side, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("X-DRIVE");

        logo.setFont(
                new Font("Segoe UI", Font.BOLD, 27));

        logo.setForeground(WHITE);

        JLabel sub = new JLabel("SMART MOBILITY");

        sub.setFont(
                new Font("Segoe UI", Font.BOLD, 10));

        sub.setForeground(BLUE);

        side.add(logo);
        side.add(Box.createVerticalStrut(4));
        side.add(sub);
        side.add(Box.createVerticalStrut(38));

        JLabel menu = new JLabel("WORKSPACE");

        menu.setForeground(MUTED);
        menu.setFont(
                new Font("Segoe UI", Font.BOLD, 10));

        side.add(menu);
        side.add(Box.createVerticalStrut(13));

        String[] names = {
                "Overview",
                "Vehicle Management",
                "Customers",
                "Rental & Payment",
                "Returns & History"
        };

        String[] icons = {
                "◈", "▣", "♙", "↗", "↩"
        };

        for (int i = 0; i < names.length; i++) {

            final int index = i;

            JButton button =
                    new JButton(icons[i] + "   " + names[i]);

            button.setHorizontalAlignment(
                    SwingConstants.LEFT);

            styleButton(button, i == 0 ? PANEL2 : NAVY);

            button.setFont(
                    new Font("Segoe UI", Font.PLAIN, 13));

            button.setBorder(
                    new EmptyBorder(13, 12, 13, 8));

            button.setMaximumSize(
                    new Dimension(190, 46));

            button.setAlignmentX(Component.LEFT_ALIGNMENT);

            button.addActionListener(
                    e -> showPage(index));

            side.add(button);
            side.add(Box.createVerticalStrut(7));
        }

        side.add(Box.createVerticalGlue());

        JLabel foot = new JLabel(
                "<html><font color='#A3B1CC'>" +
                "X-DRIVE SYSTEM<br>" +
                "Academic demonstration<br>" +
                "Malaysia • 2026</font></html>");

        foot.setFont(
                new Font("Segoe UI", Font.PLAIN, 11));

        side.add(foot);

        return side;
    }

    // ----------------------------------------------------
    // MAIN WINDOW
    // ----------------------------------------------------

    private JPanel buildMain() {

        JPanel wrapper =
                new JPanel(new BorderLayout());

        wrapper.setBackground(BG);

        JPanel top =
                new JPanel(new BorderLayout());

        top.setBackground(BG);

        top.setBorder(
                new EmptyBorder(23, 30, 17, 30));

        JLabel title =
                new JLabel("Smart Car Rental & Management");

        title.setForeground(WHITE);

        title.setFont(
                new Font("Segoe UI", Font.BOLD, 23));

        JLabel tag =
                new JLabel(
                        "Manage your fleet. Serve customers. Track every journey.");

        tag.setForeground(MUTED);

        tag.setFont(
                new Font("Segoe UI", Font.PLAIN, 12));

        JPanel words = new JPanel();

        words.setOpaque(false);

        words.setLayout(
                new BoxLayout(words, BoxLayout.Y_AXIS));

        words.add(title);
        words.add(Box.createVerticalStrut(5));
        words.add(tag);

        JLabel status =
                new JLabel("●  SYSTEM READY");

        status.setForeground(
                new Color(91, 220, 165));

        status.setFont(
                new Font("Segoe UI", Font.BOLD, 11));

        top.add(words, BorderLayout.WEST);
        top.add(status, BorderLayout.EAST);

        wrapper.add(top, BorderLayout.NORTH);

        pageLayout = new CardLayout();

        mainPanel = new JPanel(pageLayout);

        mainPanel.setBackground(BG);

        mainPanel.setBorder(
                new EmptyBorder(0, 25, 25, 25));

        mainPanel.add(buildDashboard(), "0");
        mainPanel.add(buildVehicles(), "1");
        mainPanel.add(buildCustomers(), "2");
        mainPanel.add(buildRental(), "3");
        mainPanel.add(buildHistory(), "4");

        wrapper.add(mainPanel, BorderLayout.CENTER);

        return wrapper;
    }

    private void showPage(int index) {

        pageLayout.show(
                mainPanel,
                String.valueOf(index));

        refreshAll();
    }

    // ----------------------------------------------------
    // COMMON COMPONENTS
    // ----------------------------------------------------

    private JPanel page() {

        JPanel panel =
                new JPanel(new BorderLayout(0, 16));

        panel.setBackground(BG);

        return panel;
    }

    private JLabel heading(
            String text,
            String description) {

        JLabel label = new JLabel(
                "<html><b>" + text +
                "</b><br><font color='#A3B1CC' size='3'>" +
                description +
                "</font></html>");

        label.setForeground(WHITE);

        label.setFont(
                new Font("Segoe UI", Font.BOLD, 19));

        return label;
    }

    private JPanel panel() {

        JPanel panel = new JPanel();

        panel.setBackground(PANEL);

        panel.setBorder(
                new CompoundBorder(
                        new LineBorder(BORDER, 1, true),
                        new EmptyBorder(18, 18, 18, 18)));

        return panel;
    }

    // ----------------------------------------------------
    // DASHBOARD
    // ----------------------------------------------------

    private JPanel buildDashboard() {

        JPanel p = page();

        JPanel intro =
                new JPanel(new BorderLayout());

        intro.setOpaque(false);

        intro.add(
                heading(
                        "Overview",
                        "A live snapshot of your rental operation."),
                BorderLayout.WEST);

        p.add(intro, BorderLayout.NORTH);

        JPanel content =
                new JPanel(new BorderLayout(0, 18));

        content.setOpaque(false);

        JPanel cards =
                new JPanel(new GridLayout(1, 4, 14, 0));

        cards.setOpaque(false);

        vehiclesValue = new JLabel("0");
        availableValue = new JLabel("0");
        rentalsValue = new JLabel("0");
        revenueValue = new JLabel("RM0.00");

        cards.add(statCard(
                "TOTAL VEHICLES",
                vehiclesValue,
                "Registered fleet",
                BLUE));

        cards.add(statCard(
                "AVAILABLE NOW",
                availableValue,
                "Ready for booking",
                new Color(54, 197, 145)));

        cards.add(statCard(
                "ACTIVE RENTALS",
                rentalsValue,
                "Currently on rent",
                PURPLE));

        cards.add(statCard(
                "PAYMENT REVENUE",
                revenueValue,
                "Demo transactions",
                new Color(236, 177, 75)));

        content.add(cards, BorderLayout.NORTH);

        JPanel lower =
                new JPanel(new GridLayout(1, 2, 14, 0));

        lower.setOpaque(false);

        JPanel welcome = panel();

        welcome.setLayout(
                new BoxLayout(welcome, BoxLayout.Y_AXIS));

        JLabel wl = new JLabel("Welcome to X-DRIVE");

        wl.setForeground(WHITE);

        wl.setFont(
                new Font("Segoe UI", Font.BOLD, 20));

        JLabel copy = new JLabel(
                "<html><div style='width:300px;color:#A3B1CC'>" +
                "A desktop management system built with Java " +
                "Object-Oriented Programming. Use the workspace " +
                "to register vehicles, manage customers, create " +
                "rentals, process simulated payments and record " +
                "vehicle returns.</div></html>");

        copy.setFont(
                new Font("Segoe UI", Font.PLAIN, 13));

        welcome.add(wl);
        welcome.add(Box.createVerticalStrut(15));
        welcome.add(copy);
        welcome.add(Box.createVerticalStrut(20));

        JLabel pill =
                new JLabel("  LOCAL DATA SAVING ENABLED  ");

        pill.setOpaque(true);

        pill.setBackground(
                new Color(25, 77, 78));

        pill.setForeground(
                new Color(125, 237, 197));

        pill.setBorder(
                new EmptyBorder(8, 10, 8, 10));

        welcome.add(pill);

        JPanel quick = panel();

        quick.setLayout(
                new BoxLayout(quick, BoxLayout.Y_AXIS));

        JLabel q = new JLabel("Quick actions");

        q.setForeground(WHITE);

        q.setFont(
                new Font("Segoe UI", Font.BOLD, 17));

        quick.add(q);
        quick.add(Box.createVerticalStrut(15));

        quick.add(actionButton(
                "＋   Register a vehicle",
                () -> showPage(1)));

        quick.add(Box.createVerticalStrut(9));

        quick.add(actionButton(
                "＋   Add a customer",
                () -> showPage(2)));

        quick.add(Box.createVerticalStrut(9));

        quick.add(actionButton(
                "↗   Create a rental",
                () -> showPage(3)));

        lower.add(welcome);
        lower.add(quick);

        content.add(lower, BorderLayout.CENTER);

        p.add(content, BorderLayout.CENTER);

        return p;
    }

    private JPanel statCard(
            String label,
            JLabel value,
            String note,
            Color accent) {

        JPanel c = panel();

        c.setLayout(
                new BoxLayout(c, BoxLayout.Y_AXIS));

        JLabel l = new JLabel(label);

        l.setForeground(MUTED);

        l.setFont(
                new Font("Segoe UI", Font.BOLD, 10));

        value.setForeground(WHITE);

        value.setFont(
                new Font("Segoe UI", Font.BOLD, 25));

        JLabel n = new JLabel(note);

        n.setForeground(MUTED);

        n.setFont(
                new Font("Segoe UI", Font.PLAIN, 11));

        JPanel bar = new JPanel();

        bar.setBackground(accent);

        bar.setMaximumSize(
                new Dimension(42, 4));

        bar.setPreferredSize(
                new Dimension(42, 4));

        c.add(l);
        c.add(Box.createVerticalStrut(13));
        c.add(value);
        c.add(Box.createVerticalStrut(8));
        c.add(n);
        c.add(Box.createVerticalStrut(13));
        c.add(bar);

        return c;
    }

    // ----------------------------------------------------
    // VEHICLE MANAGEMENT
    // ----------------------------------------------------

    private JPanel buildVehicles() {

        JPanel p = page();

        JPanel top =
                new JPanel(new BorderLayout());

        top.setOpaque(false);

        top.add(
                heading(
                        "Vehicle Management",
                        "Register and review the vehicles available in your fleet."),
                BorderLayout.WEST);

        top.add(
                actionButton(
                        "＋  Add vehicle",
                        this::addVehicleDialog),
                BorderLayout.EAST);

        p.add(top, BorderLayout.NORTH);

        vehicleTable = table(new String[]{
                "Vehicle ID",
                "Brand",
                "Model",
                "Year",
                "Category",
                "Fuel",
                "Daily Rate",
                "Mileage",
                "Availability"
        });

        p.add(
                tablePanel(vehicleTable),
                BorderLayout.CENTER);

        return p;
    }

    // ----------------------------------------------------
    // CUSTOMER MANAGEMENT
    // ----------------------------------------------------

    private JPanel buildCustomers() {

        JPanel p = page();

        JPanel top =
                new JPanel(new BorderLayout());

        top.setOpaque(false);

        top.add(
                heading(
                        "Customer Directory",
                        "Keep customer and driving-licence details organised."),
                BorderLayout.WEST);

        top.add(
                actionButton(
                        "＋  Register customer",
                        this::addCustomerDialog),
                BorderLayout.EAST);

        p.add(top, BorderLayout.NORTH);

        customerTable = table(new String[]{
                "Customer ID",
                "Full Name",
                "Phone",
                "Driving Licence"
        });

        p.add(
                tablePanel(customerTable),
                BorderLayout.CENTER);

        return p;
    }

    // ----------------------------------------------------
    // RENTAL AND PAYMENT
    // ----------------------------------------------------

    private JPanel buildRental() {

        JPanel p = page();

        p.add(
                heading(
                        "Rental & Payment Centre",
                        "Create a booking and record a simulated payment."),
                BorderLayout.NORTH);

        JPanel body =
                new JPanel(new GridLayout(1, 2, 15, 0));

        body.setOpaque(false);

        JPanel form = panel();

        form.setLayout(
                new BoxLayout(form, BoxLayout.Y_AXIS));

        JLabel formTitle = new JLabel("New rental");

        formTitle.setForeground(WHITE);

        formTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 18));

        form.add(formTitle);
        form.add(Box.createVerticalStrut(20));

        customerChoice =
                new JComboBox<Customer>();

        carChoice =
                new JComboBox<Car>();

        daysChoice =
                new JSpinner(
                        new SpinnerNumberModel(1, 1, 365, 1));

        methodChoice =
                new JComboBox<PaymentMethod>(
                        PaymentMethod.values());

        styleComboBox(customerChoice);
        styleComboBox(carChoice);
        styleComboBox(methodChoice);

        styleSpinner(daysChoice);

        addField(form, "Customer", customerChoice);
        addField(form, "Available vehicle", carChoice);
        addField(form, "Rental duration (days)", daysChoice);
        addField(form, "Payment method", methodChoice);

        form.add(Box.createVerticalStrut(10));

        form.add(
                actionButton(
                        "CONFIRM RENTAL & PROCESS PAYMENT",
                        this::createRental));

        JLabel note = new JLabel(
                "<html><font color='#A3B1CC'>" +
                "Payment options are for academic demonstration only. " +
                "No actual money is transferred.</font></html>");

        note.setFont(
                new Font("Segoe UI", Font.PLAIN, 11));

        form.add(Box.createVerticalStrut(12));
        form.add(note);

        JPanel receipt = panel();

        receipt.setLayout(
                new BorderLayout(0, 12));

        JLabel rt = new JLabel("Payment receipt");

        rt.setForeground(WHITE);

        rt.setFont(
                new Font("Segoe UI", Font.BOLD, 18));

        receiptArea = new JTextArea(
                "Your latest rental receipt will appear here.");

        receiptArea.setEditable(false);

        receiptArea.setBackground(
                new Color(15, 24, 44));

        receiptArea.setForeground(
                new Color(207, 220, 242));

        receiptArea.setFont(
                new Font("Consolas", Font.PLAIN, 12));

        receiptArea.setBorder(
                new EmptyBorder(14, 14, 14, 14));

        receipt.add(rt, BorderLayout.NORTH);

        receipt.add(
                new JScrollPane(receiptArea),
                BorderLayout.CENTER);

        body.add(form);
        body.add(receipt);

        p.add(body, BorderLayout.CENTER);

        return p;
    }

    // ----------------------------------------------------
    // RETURNS AND HISTORY
    // ----------------------------------------------------

    private JPanel buildHistory() {

        JPanel p = page();

        p.add(
                heading(
                        "Returns & History",
                        "Review rental records, payment transactions and process returns."),
                BorderLayout.NORTH);

        JPanel body =
                new JPanel(new GridLayout(2, 1, 0, 12));

        body.setOpaque(false);

        JPanel rentals = panel();

        rentals.setLayout(
                new BorderLayout(0, 8));

        JLabel rtitle = new JLabel("Rental records");

        rtitle.setForeground(WHITE);

        rtitle.setFont(
                new Font("Segoe UI", Font.BOLD, 15));

        rentalTable = table(new String[]{
                "Rental ID",
                "Customer",
                "Vehicle",
                "Days",
                "Start Date",
                "Amount",
                "Status"
        });

        rentals.add(rtitle, BorderLayout.NORTH);

        rentals.add(
                tablePanel(rentalTable),
                BorderLayout.CENTER);

        JPanel payments = panel();

        payments.setLayout(
                new BorderLayout(0, 8));

        JLabel ptitle =
                new JLabel("Payment transactions");

        ptitle.setForeground(WHITE);

        ptitle.setFont(
                new Font("Segoe UI", Font.BOLD, 15));

        paymentTable = table(new String[]{
                "Payment ID",
                "Rental ID",
                "Method",
                "Amount",
                "Date",
                "Status",
                "Reference"
        });

        JPanel returnBar =
                new JPanel(
                        new FlowLayout(FlowLayout.LEFT, 8, 4));

        returnBar.setOpaque(false);

        returnChoice =
                new JComboBox<Rental>();

        styleComboBox(returnChoice);

        JButton returnButton =
                actionButton(
                        "Process vehicle return",
                        this::processReturn);

        JLabel activeLabel = new JLabel("Active rental:");

        activeLabel.setForeground(WHITE);

        returnBar.add(activeLabel);
        returnBar.add(returnChoice);
        returnBar.add(returnButton);

        payments.add(ptitle, BorderLayout.NORTH);

        payments.add(
                tablePanel(paymentTable),
                BorderLayout.CENTER);

        payments.add(returnBar, BorderLayout.SOUTH);

        body.add(rentals);
        body.add(payments);

        p.add(body, BorderLayout.CENTER);

        return p;
    }

    // ----------------------------------------------------
    // TABLE STYLING
    // ----------------------------------------------------

    private JTable table(String[] columns) {

        DefaultTableModel model =
                new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {
                        return false;
                    }
                };

        JTable t = new JTable(model);

        t.setRowHeight(31);

        t.setBackground(PANEL);
        t.setForeground(WHITE);

        t.setGridColor(BORDER);

        t.setSelectionBackground(
                new Color(42, 83, 143));

        t.setSelectionForeground(Color.WHITE);

        t.setFont(
                new Font("Segoe UI", Font.PLAIN, 12));

        t.getTableHeader().setBackground(PANEL2);
        t.getTableHeader().setForeground(WHITE);

        t.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 11));

        t.getTableHeader().setReorderingAllowed(false);

        return t;
    }

    private JScrollPane tablePanel(JTable table) {

        JScrollPane s = new JScrollPane(table);

        s.getViewport().setBackground(PANEL);

        s.setBorder(
                new LineBorder(BORDER, 1));

        return s;
    }

    // ----------------------------------------------------
    // INPUT COMPONENT STYLING
    // ----------------------------------------------------

    private void styleComboBox(JComboBox<?> combo) {

        combo.setBackground(PANEL2);
        combo.setForeground(WHITE);

        combo.setFont(
                new Font("Segoe UI", Font.PLAIN, 12));

        combo.setBorder(
                new LineBorder(BORDER, 1));

        combo.setFocusable(true);

        combo.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {

                JLabel label = (JLabel)
                        super.getListCellRendererComponent(
                                list,
                                value,
                                index,
                                isSelected,
                                cellHasFocus);

                label.setOpaque(true);

                label.setBackground(
                        isSelected ? BLUE : PANEL2);

                label.setForeground(WHITE);

                label.setBorder(
                        new EmptyBorder(6, 8, 6, 8));

                return label;
            }
        });
    }

    private void styleSpinner(JSpinner spinner) {

        spinner.setBackground(PANEL2);
        spinner.setForeground(WHITE);

        spinner.setFont(
                new Font("Segoe UI", Font.PLAIN, 12));

        JComponent editor = spinner.getEditor();

        if (editor instanceof JSpinner.DefaultEditor) {

            JSpinner.DefaultEditor defaultEditor =
                    (JSpinner.DefaultEditor) editor;

            JFormattedTextField field =
                    defaultEditor.getTextField();

            field.setBackground(PANEL2);
            field.setForeground(WHITE);

            field.setCaretColor(WHITE);

            field.setFont(
                    new Font("Segoe UI", Font.PLAIN, 12));

            field.setBorder(
                    new EmptyBorder(5, 8, 5, 8));
        }

        for (Component component : spinner.getComponents()) {

            if (component instanceof JButton) {

                JButton button = (JButton) component;

                styleButton(button, PANEL2);

                button.setBorder(
                        new EmptyBorder(3, 8, 3, 8));
            }
        }
    }

    private void addField(
            JPanel parent,
            String label,
            JComponent component) {

        JLabel l = new JLabel(label);

        l.setForeground(MUTED);

        l.setFont(
                new Font("Segoe UI", Font.BOLD, 11));

        component.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 36));

        if (component instanceof JComboBox) {
            styleComboBox((JComboBox<?>) component);
        }

        parent.add(l);
        parent.add(Box.createVerticalStrut(5));
        parent.add(component);
        parent.add(Box.createVerticalStrut(12));
    }

    // ----------------------------------------------------
    // ADD VEHICLE
    // ----------------------------------------------------

    private void addVehicleDialog() {

        JTextField brand = new JTextField();
        JTextField model = new JTextField();
        JTextField year = new JTextField("2025");
        JTextField rate = new JTextField("150");

        JComboBox<String> category =
                new JComboBox<>(
                        new String[]{"Sedan", "SUV"});

        JComboBox<String> fuel =
                new JComboBox<>(
                        new String[]{
                                "Petrol",
                                "Diesel",
                                "Hybrid",
                                "Electric"
                        });

        JPanel form = dialogForm(
                new String[]{
                        "Category",
                        "Brand",
                        "Model",
                        "Year",
                        "Fuel type",
                        "Daily rate (RM)"
                },
                new JComponent[]{
                        category,
                        brand,
                        model,
                        year,
                        fuel,
                        rate
                });

        if (JOptionPane.showConfirmDialog(
                this,
                form,
                "Register new vehicle",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE)
                == JOptionPane.OK_OPTION) {

            try {

                app.addCar(
                        (String) category.getSelectedItem(),
                        brand.getText(),
                        model.getText(),
                        Integer.parseInt(year.getText().trim()),
                        (String) fuel.getSelectedItem(),
                        Double.parseDouble(rate.getText().trim()));

                refreshAll();

                info("Vehicle registered successfully.");

            } catch (Exception ex) {

                error(ex.getMessage());
            }
        }
    }

    // ----------------------------------------------------
    // ADD CUSTOMER
    // ----------------------------------------------------

    private void addCustomerDialog() {

        JTextField name = new JTextField();
        JTextField phone = new JTextField();
        JTextField licence = new JTextField();

        JPanel form = dialogForm(
                new String[]{
                        "Full name",
                        "Phone number",
                        "Driving licence number"
                },
                new JComponent[]{
                        name,
                        phone,
                        licence
                });

        if (JOptionPane.showConfirmDialog(
                this,
                form,
                "Register customer",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE)
                == JOptionPane.OK_OPTION) {

            try {

                app.addCustomer(
                        name.getText(),
                        phone.getText(),
                        licence.getText());

                refreshAll();

                info("Customer registered successfully.");

            } catch (Exception ex) {

                error(ex.getMessage());
            }
        }
    }

    // ----------------------------------------------------
    // DIALOG FORM
    // ----------------------------------------------------

    private JPanel dialogForm(
            String[] labels,
            JComponent[] fields) {

        JPanel p =
                new JPanel(
                        new GridLayout(labels.length, 2, 10, 10));

        p.setPreferredSize(
                new Dimension(420, labels.length * 42));

        p.setBackground(PANEL);

        for (int i = 0; i < labels.length; i++) {

            JLabel label = new JLabel(labels[i]);

            label.setForeground(WHITE);

            p.add(label);

            fields[i].setBackground(PANEL2);
            fields[i].setForeground(WHITE);

            p.add(fields[i]);
        }

        return p;
    }

    // ----------------------------------------------------
    // CREATE RENTAL AND PROCESS PAYMENT
    // ----------------------------------------------------

    private void createRental() {

        try {

            Customer customer =
                    (Customer) customerChoice.getSelectedItem();

            Car car =
                    (Car) carChoice.getSelectedItem();

            if (customer == null) {
                throw new IllegalArgumentException(
                        "Register or select a customer first.");
            }

            if (car == null) {
                throw new IllegalArgumentException(
                        "There are no available vehicles.");
            }

            int days =
                    (Integer) daysChoice.getValue();

            Rental rental =
                    app.createRental(customer, car, days);

            Payment payment =
                    app.pay(
                            rental,
                            (PaymentMethod) methodChoice.getSelectedItem());

            receiptArea.setText(
                    "             X-DRIVE\n" +
                    "       SMART CAR RENTAL\n" +
                    "--------------------------------\n" +
                    "Rental ID : " + rental.getRentalId() + "\n" +
                    "Payment ID: " + payment.getPaymentId() + "\n" +
                    "Date      : " + rental.getStartDate() + "\n" +
                    "Customer  : " + customer.getName() + "\n" +
                    "Vehicle   : " + car.getDisplayName() + "\n" +
                    "Category  : " + car.getCategory() + "\n" +
                    "Duration  : " + days + " day(s)\n" +
                    "Method    : " + payment.getMethod() + "\n" +
                    "--------------------------------\n" +
                    "TOTAL     : " + money.format(payment.getAmount()) + "\n" +
                    "Status    : " + payment.getStatus() + "\n" +
                    "Reference : " + payment.getTransactionReference() + "\n" +
                    "--------------------------------\n" +
                    "Thank you for choosing X-DRIVE.\n" +
                    "DEMO RECEIPT - NOT A REAL PAYMENT");

            refreshAll();

            info("Rental created and demo payment recorded.");

        } catch (Exception ex) {

            error(ex.getMessage());
        }
    }

    // ----------------------------------------------------
    // PROCESS VEHICLE RETURN
    // ----------------------------------------------------

    private void processReturn() {

        Rental rental =
                (Rental) returnChoice.getSelectedItem();

        if (rental == null) {

            error("There are no active rentals to return.");

            return;
        }

        int answer =
                JOptionPane.showConfirmDialog(
                        this,
                        "Confirm return of " +
                        rental.getCar().getDisplayName() +
                        " for " + rental.getRentalId() + "?",
                        "Confirm vehicle return",
                        JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) {

            try {

                app.returnRental(rental);

                refreshAll();

                info(
                        "Vehicle return recorded. " +
                        "Vehicle is available again.");

            } catch (Exception ex) {

                error(ex.getMessage());
            }
        }
    }

    // ----------------------------------------------------
    // REFRESH ALL DATA
    // ----------------------------------------------------

    private void refreshAll() {

        if (vehiclesValue != null) {

            vehiclesValue.setText(
                    String.valueOf(app.getCars().size()));

            availableValue.setText(
                    String.valueOf(app.availableVehicleCount()));

            rentalsValue.setText(
                    String.valueOf(app.activeRentalCount()));

            revenueValue.setText(
                    money.format(app.totalRevenue()));
        }

        if (vehicleTable != null) {

            DefaultTableModel m =
                    (DefaultTableModel) vehicleTable.getModel();

            m.setRowCount(0);

            for (Car c : app.getCars()) {

                m.addRow(new Object[]{
                        c.getCarId(),
                        c.getBrand(),
                        c.getModel(),
                        c.getYear(),
                        c.getCategory(),
                        c.getFuelType(),
                        money.format(c.getDailyRate()),
                        String.format("%.0f km", c.getMileage()),
                        c.isAvailable() ? "AVAILABLE" : "RENTED"
                });
            }
        }

        if (customerTable != null) {

            DefaultTableModel m =
                    (DefaultTableModel) customerTable.getModel();

            m.setRowCount(0);

            for (Customer c : app.getCustomers()) {

                m.addRow(new Object[]{
                        c.getCustomerId(),
                        c.getName(),
                        c.getPhone(),
                        c.getLicenceNumber()
                });
            }
        }

        if (rentalTable != null) {

            DefaultTableModel m =
                    (DefaultTableModel) rentalTable.getModel();

            m.setRowCount(0);

            for (Rental r : app.getRentals()) {

                m.addRow(new Object[]{
                        r.getRentalId(),
                        r.getCustomer().getName(),
                        r.getCar().getDisplayName(),
                        r.getDays(),
                        r.getStartDate(),
                        money.format(r.getTotalAmount()),
                        r.getStatus()
                });
            }
        }

        if (paymentTable != null) {

            DefaultTableModel m =
                    (DefaultTableModel) paymentTable.getModel();

            m.setRowCount(0);

            for (Payment pay : app.getPayments()) {

                m.addRow(new Object[]{
                        pay.getPaymentId(),
                        pay.getRentalId(),
                        pay.getMethod(),
                        money.format(pay.getAmount()),
                        pay.getPaymentDate(),
                        pay.getStatus(),
                        pay.getTransactionReference()
                });
            }
        }

        if (customerChoice != null) {

            Object selected =
                    customerChoice.getSelectedItem();

            customerChoice.removeAllItems();

            for (Customer c : app.getCustomers()) {
                customerChoice.addItem(c);
            }

            if (selected != null) {
                customerChoice.setSelectedItem(selected);
            }
        }

        if (carChoice != null) {

            carChoice.removeAllItems();

            for (Car c : app.getCars()) {

                if (c.isAvailable()) {
                    carChoice.addItem(c);
                }
            }
        }

        if (returnChoice != null) {

            returnChoice.removeAllItems();

            for (Rental r : app.getRentals()) {

                if ("ACTIVE".equals(r.getStatus())) {
                    returnChoice.addItem(r);
                }
            }
        }
    }

    // ----------------------------------------------------
    // MESSAGE DIALOGS
    // ----------------------------------------------------

    private void info(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "X-DRIVE",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void error(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Please check",
                JOptionPane.WARNING_MESSAGE);
    }
}
