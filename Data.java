// importing data structures
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

// AI help has been taken to build these data structures
public class Data {
    private static List<Route> routes = new ArrayList<>();
    private static List<Bus>   buses  = new ArrayList<>();

    static {
        
        // this data has been taken from daewoo express booking app

        // Lahore departures
        Route r01 = new Route("R01", "Lahore",     "Karachi",     7770, "07:00 AM");
        Route r02 = new Route("R02", "Lahore",     "Islamabad",   2620, "06:00 AM");
        Route r03 = new Route("R03", "Lahore",     "Rawalpindi",  2510, "05:30 AM");
        Route r04 = new Route("R04", "Lahore",     "Peshawar",    2740, "08:00 AM");
        Route r05 = new Route("R05", "Lahore",     "Multan",      1610, "09:00 AM");
        Route r06 = new Route("R06", "Lahore",     "Faisalabad",   920, "10:00 AM");
        Route r07 = new Route("R07", "Lahore",     "Hyderabad",   7080, "06:30 AM");
        Route r08 = new Route("R08", "Lahore",     "Sialkot",     1060, "11:00 AM");
        Route r09 = new Route("R09", "Lahore",     "Bahawalpur",  2050, "08:30 AM");
        Route r10 = new Route("R10", "Lahore",     "Abbottabad",  2670, "07:30 AM");

        // Islamabad departures
        Route r11 = new Route("R11", "Islamabad",  "Lahore",      2620, "06:00 AM");
        Route r12 = new Route("R12", "Islamabad",  "Karachi",     8050, "05:00 AM");
        Route r13 = new Route("R13", "Islamabad",  "Peshawar",     960, "07:00 AM");
        Route r14 = new Route("R14", "Islamabad",  "Multan",      2920, "08:00 AM");

        // Rawalpindi departures
        Route r15 = new Route("R15", "Rawalpindi", "Lahore",      2510, "06:30 AM");
        Route r16 = new Route("R16", "Rawalpindi", "Karachi",     7940, "05:30 AM");
        Route r17 = new Route("R17", "Rawalpindi", "Peshawar",     840, "09:00 AM");
        Route r18 = new Route("R18", "Rawalpindi", "Multan",      2780, "10:00 AM");

        // Other city departures
        Route r19 = new Route("R19", "Peshawar",   "Islamabad",    960, "06:00 AM");
        Route r20 = new Route("R20", "Multan",     "Lahore",      1610, "07:00 AM");
        Route r21 = new Route("R21", "Karachi",    "Lahore",      7770, "04:00 PM");
        Route r22 = new Route("R22", "Faisalabad", "Lahore",       920, "08:00 AM");

        routes.addAll(Arrays.asList(
            r01,r02,r03,r04,r05,r06,r07,r08,r09,r10,
            r11,r12,r13,r14,r15,r16,r17,r18,r19,r20,r21,r22
        ));

        // One bus per route, 40 seats 
        for (int i = 0; i < routes.size(); i++) {
            String busId = String.format("B%02d", i + 1);
            buses.add(new Bus(busId, routes.get(i), 40));
        }
    }

    public static List<Route> getAllRoutes() 
        { return routes; }
    public static List<Bus>   getAllBuses() 
        { return buses; }

    public static List<Bus> searchBuses(String origin, String destination) {
        List<Bus> result = new ArrayList<>();
        for (Bus b : buses) {
            String o = b.getRoute().getOrigin().trim().toLowerCase();
            String d = b.getRoute().getDestination().trim().toLowerCase();
            if (o.equals(origin.trim().toLowerCase()) &&
                d.equals(destination.trim().toLowerCase())) {
                result.add(b);
            }
        }
        return result;
    }

    //for dropdown menu in GUI
    public static List<String> getAllCities() {
        Set<String> cities = new LinkedHashSet<>();
        for (Route r : routes) {
            cities.add(r.getOrigin());
            cities.add(r.getDestination());
        }
        return new ArrayList<>(cities);
    }
}
