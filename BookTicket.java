import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
// this is to use my data class and search in that
import java.util.List;
// this is to generate random digits as ticket id for user
// this is to model ticketing apps like daewoo express
import java.util.UUID;

public class BookTicket {
    // creating an object of WalletService
    private WalletService walletService;

    // passing wallet object 
    public BookTicket(WalletService walletService) {
        this.walletService = walletService;
    }

    // now ill check the city user wants to book the bus from
    // im passing the origin and destination to check if that bus is available

    public List<Bus> searchBuses(String origin, String destination) {
        // it will go to the data class and from there implement searching
        return Data.searchBuses(origin, destination);
    }

    // applying discount
    // this is the method for that
    // to count no of days left till the bus departure date im using chronounit Days
    // it is a built in library having the method of between
    // my unit is days so its ChronoUnit.DAYS
    // the between takes 2 arguments and checks no of days btw them
    public double getDiscountedFare(double baseFare, LocalDate travelDate) {
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), travelDate);

        /*
        the discounted price will be calculated by applying the algorithm defined 
        in discount class 
        it uses the method from that class and days left are passed using above variable
        */
        double discountedFare = discountCalculator.applyDiscount(baseFare, daysLeft);
        // simply return the discounted price
        return discountedFare;
    }

    // main method
    public Ticket bookTicket(User user, Bus bus, int seatNo, LocalDate travelDate) {

        // first ill check if seat requested by user is available or not
        // this method returns boolean from user class so ill store it in a boolean var
        // ill pass the seat no and apply alg on that
        boolean seatFree = bus.isSeatAvailable(seatNo);
        // just stop if seat not free 
        if (!seatFree) {
            return null;  
        }

        // step 2 - calculate how much to charge
        double originalFare = bus.getRoute().getBaseFare();
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), travelDate);
        double finalFare = DiscountCalculator.applyDiscount(originalFare, daysLeft);

        // step 3 - deduct money from wallet
        boolean paymentDone = walletService.deduct(user, finalFare);
        if (!paymentDone) {
            return null;   // not enough balance, stop here
        }

        // step 4 - mark seat as booked
        bus.bookSeat(seatNo);

        // step 5 - generate unique ticket ID e.g. TW-A1B2C3D4
        String randomPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String ticketId = "TW-" + randomPart;

        // step 6 - create ticket object
        Ticket ticket = new Ticket(ticketId, user, bus, seatNo, travelDate, originalFare, finalFare);

        // step 7 - save ticket in user's history
        user.addTicket(ticket);

        // step 8 - return the completed ticket
        return ticket;
    }
} {
    
}
