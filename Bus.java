// class for each bus which will become specific to each route by creating objects
public class Bus {
    private String busId;   // id for a bus to book and get details of
    private Route route;    // uses route number for route class
    private int totalSeats; // fixed for all buses 

    // array for bus seats
    // true = booked & false = available
    private boolean[] seatMap;   

    // constructor that takes arguments
    public Bus(String busId, Route route, int totalSeats) {
        this.busId = busId;
        this.route = route;
        this.totalSeats = totalSeats;
        this.seatMap = new boolean[totalSeats + 1]; // to start bus seat numbering from 1
    }
    // method to check if user can or cant book that seat
    // if false, it returns !false = true
    // false = not booked
    // true in this method = yes it is available to be booked
    public boolean isSeatAvailable(int seatNo) {
        return !seatMap[seatNo];
    }

    // after passing above method, available seat can be booked by user by choosing seat no.
    public void bookSeat(int seatNo) {
        // now it will assign true =  booked
        // status of that particular seat will change from default false to true
        seatMap[seatNo] = true;
    }

    public String getBusId()   
     { return busId; }
    public Route getRoute()     
    { return route; }
    public int getTotalSeats()  
    { return totalSeats; }
    public boolean[] getSeatMap() 
    { return seatMap; }
} 
