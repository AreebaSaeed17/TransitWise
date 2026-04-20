// importing the classes for file handling
import java.io.*;
import java.util.*;

public class FileManagement {

    // Directory + file paths
    private static final String DATA_DIR     = "data/";
    private static final String USERS_FILE   = DATA_DIR + "users.txt";
    private static final String TICKETS_FILE = DATA_DIR + "tickets.txt";

    // Called ONCE at application startup.
     * Ensures "data/" directory exists so files can be saved.
     */
    public static void init() {
        new File(DATA_DIR).mkdirs();
    }

    // ─────────────────────────────────────────
    // USER SAVE / LOAD
    // ─────────────────────────────────────────

    /**
     * Saves ALL users to users.txt.
     *
     * Format:
     * name|cnic|phone|password|walletBalance
     *
     * Note: We escape "|" inside names so the file format never breaks.
     */
    public static void saveUsers(Collection<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE))) {

            for (User u : users) {
                // Convert user → CSV line
                bw.write(
                    escape(u.getName()) + "|" +
                    u.getCnic()         + "|" +
                    u.getPhone()        + "|" +
                    u.getPassword()     + "|" +
                    u.getWalletBalance()
                );
                bw.newLine();
            }

        } catch (IOException e) {
            System.err.println("[FileManager] Failed to save users: " + e.getMessage());
        }
    }

    /**
     * Loads ALL users from users.txt.
     *
     * Returns: Map<cnic, User>
     */
    public static Map<String, User> loadUsers() {
        Map<String, User> map = new LinkedHashMap<>();
        File file = new File(USERS_FILE);

        // If file doesn't exist yet → return empty map
        if (!file.exists()) return map;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] parts = line.split("\\|", -1);
                if (parts.length < 5) continue; // safety check

                String name     = unescape(parts[0]);
                String cnic     = parts[1];
                String phone    = parts[2];
                String password = parts[3];
                double balance  = Double.parseDouble(parts[4]);

                User u = new User(name, cnic, phone, password);
                u.setWalletBalance(balance);

                map.put(cnic, u);
            }

        } catch (Exception e) {
            System.err.println("[FileManager] Failed to load users: " + e.getMessage());
        }

        return map;
    }

    // ─────────────────────────────────────────
    // TICKET SAVE / LOAD
    // ─────────────────────────────────────────

    /**
     * Appends ONE ticket to the tickets.txt (does NOT rewrite the whole file)
     *
     * Format:
     * ticketId|userCnic|busId|seat|travelDate|bookingTime|originalFare|amountPaid
     */
    public static void appendTicket(Ticket t) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TICKETS_FILE, true))) {

            bw.write(
                t.getTicketId()             + "|" +
                t.getUser().getCnic()       + "|" +
                t.getBus().getBusId()       + "|" +
                t.getSeatNumber()           + "|" +
                t.getTravelDate()           + "|" +
                t.getBookingTime()          + "|" +
                t.getOriginalFare()         + "|" +
                t.getAmountPaid()
            );
            bw.newLine();

        } catch (IOException e) {
            System.err.println("[FileManager] Failed to save ticket: " + e.getMessage());
        }
    }

    /**
     * Loads *all* tickets.
     * Returns raw rows because rebuilding full Ticket objects would require circular references.
     */
    public static List<String[]> loadAllTicketRecords() {
        List<String[]> records = new ArrayList<>();
        File file = new File(TICKETS_FILE);

        if (!file.exists()) return records;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

