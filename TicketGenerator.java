import java.time.format.DateTimeFormatter;
// this is the class to get the data from user details
// and then use them together to print out a ticket with details on the terminal

public class TicketGenerator {
    // passing an object t of the ticket class to get data to print on user ticket
    // method is of string type as everything to be displayed is in string format
    // I want to return instead of System.out.println
    // so that i can easily call this in main instead of rechecking how to pass it
    public static String generate(Ticket t) {

        // making a line at top and bottom to make it look neater 
        // ive made the variable formatting_line so that i can use it in return operation

    String formatting_line = "=============================================";
    // to print the route Ive made the route variable
    // I enter a bus object from getBus
    // inside that bus i get the route where the bus was going using getRoute
    // from that route i can get 2 infos
    // origin and destination
    // i'm repeating the same to get origin and destination one by one from route object
    // i'm storing it in the route variable and returning this in this method
    String route = t.getBus().getRoute().getOrigin() + " → " + t.getBus().getRoute().getDestination();
    // getting the booking time using the object t of Ticket class
    // im printing that time using datetimeformatter to get it in a defined pattern
    String date  = t.getBookingTime().format(DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a"));

    return formatting_line+ "\n" +
            // prints each of these in new line
           "Ticket ID   : " + t.getTicketId()                          + "\n" +
           "Passenger   : " + t.getUser().getName()                    + "\n" +
           "Route       : " + route                                    + "\n" +
           "Seat No.    : " + t.getSeatNumber()                        + "\n" +
           "Travel Date : " + t.getTravelDate()                        + "\n" +
           "Booked At   : " + date                                     + "\n" +

           // prints formatted price to 2 digits after decimal for formatting
           "Orig. Fare  : Rs." + String.format("%.2f", t.getOriginalFare()) + "\n" +
           "Amount Paid : Rs." + String.format("%.2f", t.getAmountPaid())   + "\n" +
           formatting_line;
    }
}
