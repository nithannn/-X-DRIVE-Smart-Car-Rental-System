/** Simulated payment options. No real bank, card or e-wallet transaction is made. */
public enum PaymentMethod {
    TOUCH_N_GO("Touch 'n Go eWallet"),
    MAYBANK("Maybank2u"),
    DUITNOW_QR("DuitNow QR"),
    VISA_MASTERCARD("Debit / Credit Card"),
    CIMB("CIMB Clicks"),
    FPX("FPX Online Banking"),
    GRABPAY("GrabPay"),
    CASH("Cash at Counter");

    private final String label;
    PaymentMethod(String label) { this.label = label; }
    @Override public String toString() { return label; }
}
