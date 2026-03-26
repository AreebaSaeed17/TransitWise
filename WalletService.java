// this is my class to manage user wallet 
// user will get ability to buy tickets using balance in their wallets
// they can send money to the wallet in the beginning as well
public class WalletService {
    // method to deposit money
    // it takes user object and the amount user wants to enter
    // it checks and then assigns that money to that user's attribute of balance
    // to do this, user object is passed
    public boolean depositMoney(User user, double amount) {
        if (amount <= 0) return false;
        user.setWalletBalance(user.getWalletBalance() + amount);
        return true;
    }

    public boolean deduct(User user, double amount) {
        if (user.getWalletBalance() < amount) return false; // insufficient balance
        user.setWalletBalance(user.getWalletBalance() - amount);
        return true;
    }

    public double getBalance(User user) {
        return user.getWalletBalance();
    }
} 
