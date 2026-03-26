
import java.util.HashMap;
import java.util.Map;

// this is the class to authenticate user login
public class AuthService {
    // Key = CNIC string, value = User object
    private Map<String, User> usersByCnic  = new HashMap<>();
    private Map<String, User> usersByPhone = new HashMap<>();

    public boolean register(String name, String cnic, String phone, String password) {
        // Basic validation (use AI/regex for proper CNIC format check)
        if (usersByCnic.containsKey(cnic)) return false; // already registered
        User newUser = new User(name, cnic, phone, password);
        usersByCnic.put(cnic, newUser);
        usersByPhone.put(phone, newUser);
        return true;
    }

    // Login by either CNIC or phone number
    public User login(String identifier, String password) {
        User u = usersByCnic.getOrDefault(identifier,
                 usersByPhone.get(identifier));
        if (u != null && u.getPassword().equals(password)) return u;
        return null; // login failed
    }
} 
