// the class for route details
public class Route {
    // creating private attributes & public getter/setter
    private String routeId;     // specific to each route to load details later
    private String origin;      // leaving city of bus
    private String destination; // bus destination
    private double baseFare;    //  actual ticket price without any discount
    private String departureTime;   

    // constructor that takes arguments from the main function during object creation
    public Route(String routeId, String origin, String destination, double baseFare,
        String departureTime) 
    {
        this.routeId = routeId;
        this.origin = origin;
        this.destination = destination;
        this.baseFare = baseFare;
        this.departureTime = departureTime;
    }

    // getters for each attribute matching their data type
    public String getRouteId()       
        { return routeId; }
    public String getOrigin()       
        { return origin; }
    public String getDestination()   
        { return destination; }
    public double getBaseFare()      
        { return baseFare; }
    public String getDepartureTime() 
        { return departureTime; }

    // Tostring method to print out the attributes
    @Override
    public String toString() {
        return origin + " → " + destination + " | Rs." + baseFare + " | " + departureTime;
    }
}   

