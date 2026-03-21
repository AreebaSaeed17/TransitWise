import java.time.LocalDate;
import java.time.LocalDateTime;

// class for a ticket detailing and creation
public class Ticket {
    private String ticketId;
    // object of user class
    private User user;
    // object of bus class 
    private Bus bus;
    private int seatNumber;
    private LocalDate travelDate;
    private LocalDateTime bookingTime;
    private double originalFare;
    private double amountPaid;       // fare after discount

    // constructor with arguments
    public Ticket(String ticketId, User user, Bus bus,int seatNumber, LocalDate travelDate,
                  double originalFare, double amountPaid) {
        this.ticketId = ticketId;
        this.user = user;
        this.bus = bus;
        this.seatNumber = seatNumber;
        this.travelDate = travelDate;
        // getting the exact time of getting the ticket
        this.bookingTime = LocalDateTime.now();
        this.originalFare = originalFare;
        this.amountPaid = amountPaid;
    }

    public String getTicketId()      
    { return ticketId; }
    public User getUser()              
    { return user; }
    public Bus getBus()               
    { return bus; }
    public int getSeatNumber()         
    { return seatNumber; }
    public LocalDate getTravelDate()   
    { return travelDate; }
    public LocalDateTime getBookingTime() 
    { return bookingTime; }
    public double getOriginalFare()    
    { return originalFare; }
    public double getAmountPaid()      
    { return amountPaid; }
}
