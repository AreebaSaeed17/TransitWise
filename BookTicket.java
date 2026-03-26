import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
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

        // ill get these values to pass into the methods i made above
        // get the route fare from bus class 
        // ill enter the bus class and from there ill go into route
        // from the route ill get its set fares
        double originalFare = bus.getRoute().getBaseFare();
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), travelDate);
        double finalFare = discountCalculator.applyDiscount(originalFare, daysLeft);

        // user buying ticket
        boolean paymentDone = walletService.deductMoney(user, finalFare);
        // if money wasnt enough for transaction, payment didnt get done
        // and false was returned and stored in the above var
        // just stop here as no need for any other check
        if (!paymentDone) {
            return null;   
        }

        // if above was all passed then give the ticket to user
        bus.bookSeat(seatNo);

        // now create a ticket ID using UUID class e.g. TW-ABC56DHW
        // im storing it in a string and making it capital 
        String randomPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        // im adding TW for my project name TransitWise
        String ticketId = "TW-" + randomPart;

        // creating ticket object
        Ticket ticket = new Ticket(ticketId, user, bus, seatNo, travelDate, originalFare, finalFare);

        // now save the ticket in user's history so that they can buy more tickets later on
        user.addTicket(ticket);

        // return the ticket that the user just bought 
        return ticket;
    }
} 
