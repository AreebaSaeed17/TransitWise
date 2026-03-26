// this is my class to manage user wallet 
// user will get ability to buy tickets using balance in their wallets
// they can send money to the wallet in the beginning as well
public class WalletService {
    // method to deposit money
    // it takes user object and the amount user wants to enter
    // it checks and then assigns that money to that user's attribute of balance
    // to do this, user object is passed
    public boolean depositMoney(User user, double amount) {
        if (amount <= 0){
             return false;}

        user.setWalletBalance(user.getWalletBalance() + amount);
        return true;
    }
    // this method deducts money from the wallet when user buys ticket
    public boolean deductMoney(User user, double amount) {
        // if balance is insufficient transaction fails.
        if (user.getWalletBalance() < amount) {
            System.out.println("Cant perform transaction!\n You hve insufficient balance in your wallet.");
            return false;} 
        else{
            // deducting amount from balance
            // first get the balance from user directly and then subtract that amount
            // now the subtracted amount gets assigned to the user's account balance using set methof
            user.setWalletBalance(user.getWalletBalance() - amount);
            // as it is a boolean function so it has to return a boolean
            return true;}
    }
    // getBalance to now print the new balance of user wallet
    public double getBalance(User user) {
        return user.getWalletBalance();
    }
} 
