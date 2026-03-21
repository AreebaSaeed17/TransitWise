import java.util.ArrayList;
import java.util.List;

// creating the class for user info
public class User {
    private String name;
    private String cnic;       // 13 digit string
    private String phone;      // again a string of numbers
    private String password;
    private double walletBalance;
    private List<Ticket> bookingHistory;

    public User(String name, String cnic, String phone, String password) {
        this.name = name;
        this.cnic = cnic;
        this.phone = phone;
        this.password = password;
        this.walletBalance = 0.0;
        this.bookingHistory = new ArrayList<>();
    }

    // Getters
    public String getName()           { return name; }
    public String getCnic()           { return cnic; }
    public String getPhone()          { return phone; }
    public String getPassword()       { return password; }
    public double getWalletBalance()  { return walletBalance; }
    public List<Ticket> getHistory()  { return bookingHistory; }

    // Setters
    public void setWalletBalance(double b) { this.walletBalance = b; }
    public void addTicket(Ticket t)        { bookingHistory.add(t); }
}