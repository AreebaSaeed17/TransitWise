// class to implement algorithm for discounting
public class discountCalculator {
    // it checks how many days ahead of the bus departure it was bought
    // based on early booking, it offers discount
    public static double calculateDiscount(long daysAhead) {
        if (daysAhead >= 7) 
            {return 0.30;}  
        if (daysAhead >= 6) 
            {return 0.25;}  
        if (daysAhead >= 5) 
            { return 0.20;}
        if (daysAhead >= 4)  
            {return 0.15;}
        if (daysAhead >= 3 || daysAhead >= 2 ) 
            { return 0.10;}
        if (daysAhead >= 1)  
            {return 0.05;}                      
        // if none of these days, no discount
        // actual price will be carried on
        else
            {return 0.0; }              
    }

    // applying discount
    public static double applyDiscount(double fare, long daysAhead) {
        // getting discount value from the upper method
        double discount = calculateDiscount(daysAhead);
        // subtracting discount value from original fare
        return (fare-=discount);
    }
}
