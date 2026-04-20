import java.io.*;
import java.util.*;

public class FileManagement {

    private static final String DATA_DIR = "data/";
    private static final String USERS_FILE = DATA_DIR + "users.txt";
    private static final String TICKETS_FILE = DATA_DIR + "tickets.txt";

    // Create data folder
    public static void init() {
        new File(DATA_DIR).mkdirs();
    }

    // ───────── USERS ─────────

    public static void saveUsers(Collection<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE))) {

            for (User u : users) {
                bw.write(
                        escape(u.getName()) + "|" +
                        u.getCnic() + "|" +
                        u.getPhone() + "|" +
                        u.getPassword() + "|" +
                        u.getWalletBalance()
                );
                bw.newLine();
            }

        } catch (IOException e) {
            System.err.println("Failed to save users: " + e.getMessage());
        }
    }

    public static Map<String, User> loadUsers() {
        Map<String, User> users = new LinkedHashMap<>();
        File file = new File(USERS_FILE);

        if (!file.exists()) return users;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] p = line.split("\\|");
                if (p.length < 5) continue;

                String name = unescape(p[0]);
                String cnic = p[1];
                String phone = p[2];
                String password = p[3];
                double balance = Double.parseDouble(p[4]);

                User u = new User(name, cnic, phone, password);
                u.setWalletBalance(balance);

                users.put(cnic, u);
            }

        } catch (Exception e) {
            System.err.println("Failed to load users: " + e.getMessage());
        }

        return users;
    }

    // ───────── TICKETS ─────────

    public static void appendTicket(Ticket t) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TICKETS_FILE, true))) {

            bw.write(
                    t.getTicketId() + "|" +
                    t.getUser().getCnic() + "|" +
                    t.getBus().getBusId() + "|" +
                    t.getSeatNumber() + "|" +
                    t.getTravelDate() + "|" +
                    t.getBookingTime() + "|" +
                    t.getOriginalFare() + "|" +
                    t.getAmountPaid()
            );
            bw.newLine();

        } catch (IOException e) {
            System.err.println("Failed to save ticket: " + e.getMessage());
        }
    }

    public static List<String[]> loadAllTicketRecords() {
        List<String[]> records = new ArrayList<>();
        File file = new File(TICKETS_FILE);

        if (!file.exists()) return records;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = br.readLine()) != null) {
                records.add(line.split("\\|"));
            }

        } catch (IOException e) {
            System.err.println("Failed to load tickets: " + e.getMessage());
        }

        return records;
    }

    // ───────── HELPERS ─────────

    private static String escape(String s) {
        return s.replace("|", "\\|");
    }

    private static String unescape(String s) {
        return s.replace("\\|", "|");
    }
}