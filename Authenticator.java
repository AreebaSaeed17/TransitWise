// this java class will check if user is logging in correctly or not
// this involves simple checks and isnt affiliated with nadra or google
// as those APIs arent easily accessible
public class Authenticator {
    // private attributes
    private AuthService authService;
    // constructor
    public Authenticator(AuthService authService) {
        this.authService = authService;
    }

    // Validate a 13-digit CNIC 
    public boolean isValidCnic(String cnic) {
        return cnic != null && cnic.matches("\\d{13}");
    }

    // Validates Pakistani mobile number starting with 03
    public boolean isValidPhone(String phone) {
        return phone != null && phone.matches("03\\d{9}");
    }

    // Password must be at least 6 characters
    public boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    // Name must not be blank
    public boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    /**
     * once all checks are passes, the app registers a new user.
     * Returns a message(for GUI display).
     */
    // method for gui error display
    public String register(String name, String cnic, String phone, String password) {
        if (!isValidName(name))
            return "Name cannot be empty.";
        if (!isValidCnic(cnic))
            return "CNIC must be exactly 13 digits (no dashes).";
        if (!isValidPhone(phone))
            return "Phone must start with 03 and be 11 digits.";
        if (!isValidPassword(password))
            return "Password must be at least 6 characters.";

        // using trim function to eliminate any extra spaces
        boolean success = authService.register(name.trim(), cnic.trim(), phone.trim(), password);
        if (success)
            return "Registration successful! You can now log in.";
        else
            return "This CNIC is already registered.";
    }

    // for logging in
    // if the string is empty or password is empty then return nothing
    // if user entered details then it trims anyy spaces and verifies for authentic detaills
    public User login(String identifier, String password) {
        if (identifier == null || identifier.isBlank()) return null;
        if (password == null || password.isBlank())    return null;
        return authService.login(identifier.trim(), password);
    }
}