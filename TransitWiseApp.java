import javafx.application.Application;
import javafx.geometry.*;
import javafx.print.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class TransitWiseApp extends Application {

    private final AuthService   authService   = new AuthService();
    private final Authenticator authenticator = new Authenticator(authService);
    private final WalletService walletService = new WalletService();
    private final BookTicket    bookTicket    = new BookTicket(walletService);
    private User  currentUser = null;
    private Stage primaryStage;

    // Colour palette
    private static final String BG          = "#EEF6FB";
    private static final String ACCENT      = "#6B8FBF";
    private static final String ACCENT_DARK = "#4A6FA5";
    private static final String PASTEL_BLUE = "#B8D4EA";
    private static final String TEXT_DARK   = "#2C3E50";
    private static final String TEXT_MID    = "#5D7289";
    private static final String GREEN_OK    = "#73C6A0";
    private static final String RED_ERR     = "#E88080";

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("TransitWise - Bus Booking");
        stage.setMinWidth(820);
        stage.setMinHeight(580);

        // Load saved data on startup
        Map<String, User> loaded = FileHandler.loadUsers(authService);
        FileHandler.loadTickets(loaded);

        // Save on close
        stage.setOnCloseRequest(e -> {
            FileHandler.saveUsers(authService.getUsersByCnic());
            FileHandler.saveTickets(authService.getUsersByCnic());
        });

        showLoginScene();
        stage.show();
    }

    // =========================================================
    // LOGIN / REGISTER
    // =========================================================
    private void showLoginScene() {
        VBox left = new VBox(14);
        left.setPrefWidth(270);
        left.setAlignment(Pos.CENTER);
        left.setPadding(new Insets(40));
        left.setStyle("-fx-background-color:" + ACCENT + ";");
        Label logo    = new Label("🚌");     logo.setFont(Font.font(60));
        Label brand   = new Label("TransitWise");
        brand.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        brand.setTextFill(Color.WHITE);
        Label tagline = new Label("Smart bus booking for Pakistan");
        tagline.setFont(Font.font("Segoe UI", 12));
        tagline.setTextFill(Color.web("#D6E8F5"));
        tagline.setWrapText(true);
        tagline.setTextAlignment(TextAlignment.CENTER);
        left.getChildren().addAll(logo, brand, tagline);

        VBox right = new VBox(10);
        right.setPadding(new Insets(40, 50, 40, 50));
        right.setStyle("-fx-background-color:#FFFFFF;");

        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // --- Login tab ---
        Tab loginTab = new Tab("  Login  ");
        VBox lb = new VBox(10);
        lb.setPadding(new Insets(18, 0, 0, 0));
        TextField     loginId  = styledField("CNIC or Phone Number");
        PasswordField loginPw  = styledPass("Password");
        Label         loginMsg = msgLabel();
        Button        loginBtn = accentBtn("Login");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setOnAction(e -> {
            User u = authenticator.login(loginId.getText(), loginPw.getText());
            if (u != null) { currentUser = u; showDashboard(); }
            else { err(loginMsg, "Invalid credentials. Please try again."); }
        });
        lb.getChildren().addAll(sectionTitle("Welcome back!"),
            fieldLabel("CNIC or Phone"), loginId,
            fieldLabel("Password"), loginPw, loginBtn, loginMsg);
        loginTab.setContent(lb);

        // --- Register tab ---
        Tab regTab = new Tab("  Register  ");
        VBox rb = new VBox(8);
        rb.setPadding(new Insets(18, 0, 0, 0));
        TextField     rName  = styledField("Full Name");
        TextField     rCnic  = styledField("13-digit CNIC, no dashes");
        TextField     rPhone = styledField("Phone e.g. 03001234567");
        PasswordField rPw    = styledPass("Password (min 6 characters)");
        Label         regMsg = msgLabel();
        Button        regBtn = accentBtn("Create Account");
        regBtn.setMaxWidth(Double.MAX_VALUE);
        regBtn.setOnAction(e -> {
            String res = authenticator.register(
                rName.getText(), rCnic.getText(), rPhone.getText(), rPw.getText());
            boolean ok = res.startsWith("Registration");
            if (ok) { ok(regMsg, res); rName.clear(); rCnic.clear(); rPhone.clear(); rPw.clear(); }
            else      err(regMsg, res);
        });
        rb.getChildren().addAll(sectionTitle("Create Account"),
            fieldLabel("Name"), rName, fieldLabel("CNIC"), rCnic,
            fieldLabel("Phone"), rPhone, fieldLabel("Password"), rPw,
            regBtn, regMsg);
        regTab.setContent(rb);

        tabs.getTabs().addAll(loginTab, regTab);
        right.getChildren().add(tabs);
        HBox root = new HBox(left, right);
        HBox.setHgrow(right, Priority.ALWAYS);
        primaryStage.setScene(new Scene(root, 820, 520));
    }

    // =========================================================
    // DASHBOARD
    // =========================================================
    private void showDashboard() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:" + BG + ";");

        // Top bar
        HBox bar = new HBox(12);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(12, 24, 12, 24));
        bar.setStyle("-fx-background-color:" + ACCENT + ";");
        Label name = new Label("🚌  TransitWise");
        name.setFont(Font.font("Segoe UI", FontWeight.BOLD, 17));
        name.setTextFill(Color.WHITE);
        Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
        Label uLbl = new Label("👤  " + currentUser.getName());
        uLbl.setFont(Font.font("Segoe UI", 13));
        uLbl.setTextFill(Color.web("#D6E8F5"));
        Button logoutBtn = ghostBtn("Logout");
        logoutBtn.setOnAction(e -> {
            FileHandler.saveUsers(authService.getUsersByCnic());
            FileHandler.saveTickets(authService.getUsersByCnic());
            currentUser = null;
            showLoginScene();
        });
        bar.getChildren().addAll(name, sp, uLbl, logoutBtn);
        root.setTop(bar);

        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.getTabs().addAll(buildBookingTab(), buildWalletTab(), buildHistoryTab());
        root.setCenter(tabs);

        primaryStage.setScene(new Scene(root, 920, 620));
    }

    // =========================================================
    // TAB: BOOK TICKET
    // =========================================================
    private Tab buildBookingTab() {
        Tab tab = new Tab("  🎫  Book Ticket  ");

        VBox page = new VBox(16);
        page.setPadding(new Insets(24));
        page.setStyle("-fx-background-color:" + BG + ";");

        VBox card = card();
        card.getChildren().add(sectionTitle("Search Buses"));

        List<String> cities = Data.getAllCities();
        ComboBox<String> fromBox = styledCombo(cities, "From");
        ComboBox<String> toBox   = styledCombo(cities, "To");
        DatePicker dp = new DatePicker(LocalDate.now().plusDays(1));
        dp.setMaxWidth(Double.MAX_VALUE);
        dp.setStyle(fieldStyle());

        GridPane grid = new GridPane();
        grid.setHgap(14); grid.setVgap(8);
        grid.add(fieldLabel("From"),        0, 0); grid.add(fromBox, 0, 1);
        grid.add(fieldLabel("To"),          1, 0); grid.add(toBox,   1, 1);
        grid.add(fieldLabel("Travel Date"), 2, 0); grid.add(dp,      2, 1);
        GridPane.setHgrow(fromBox, Priority.ALWAYS);
        GridPane.setHgrow(toBox,   Priority.ALWAYS);
        GridPane.setHgrow(dp,      Priority.ALWAYS);

        Button searchBtn = accentBtn("Search Buses");
        Label  searchMsg = msgLabel();
        VBox   results   = new VBox(10);

        searchBtn.setOnAction(e -> {
            results.getChildren().clear();
            searchMsg.setText("");
            String from = fromBox.getValue(), to = toBox.getValue();
            if (from == null || to == null || from.equals(to)) {
                err(searchMsg, "Please select valid From / To cities."); return;
            }
            List<Bus> found = bookTicket.searchBuses(from, to);
            if (found.isEmpty()) { err(searchMsg, "No buses found for this route."); return; }
            for (Bus b : found)
                results.getChildren().add(buildBusRow(b, dp, searchMsg));
        });

        card.getChildren().addAll(grid, searchBtn, searchMsg);
        page.getChildren().addAll(card, results);

        ScrollPane scroll = new ScrollPane(page);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color:" + BG + ";-fx-background:" + BG + ";");
        tab.setContent(scroll);
        return tab;
    }

    private HBox buildBusRow(Bus bus, DatePicker dp, Label msg) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(14, 16, 14, 16));
        row.setStyle("-fx-background-color:#FFFFFF;-fx-background-radius:10;" +
                     "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.06),6,0,0,2);");

        VBox info = new VBox(4); HBox.setHgrow(info, Priority.ALWAYS);
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), dp.getValue());
        double orig = bus.getRoute().getBaseFare();
        double disc = discountCalculator.applyDiscount(orig, daysLeft);
        int pct = (int)((1 - disc / orig) * 100);

        Label route = bold(bus.getRoute().getOrigin() + "  ->  " + bus.getRoute().getDestination(), 14);
        Label dep   = subtle("Departs: " + bus.getRoute().getDepartureTime() + "   |   Bus: " + bus.getBusId());
        Label fare  = bold("Rs. " + String.format("%.0f", disc), 15);
        fare.setTextFill(Color.web(ACCENT_DARK));
        Label orig2 = subtle("Original: Rs." + String.format("%.0f", orig) +
                             (pct > 0 ? "   (save " + pct + "%)" : ""));
        info.getChildren().addAll(route, dep, fare, orig2);

        Spinner<Integer> spin = new Spinner<>(1, bus.getTotalSeats(), 1);
        spin.setEditable(true); spin.setMaxWidth(80);
        spin.setStyle(fieldStyle());

        Button bookBtn = accentBtn("Book");
        bookBtn.setOnAction(e -> {
            Ticket t = bookTicket.bookTicket(currentUser, bus, spin.getValue(), dp.getValue());
            if (t == null) err(msg, "Booking failed - seat taken or insufficient wallet balance.");
            else {
                ok(msg, "Booked! Ticket ID: " + t.getTicketId());
                // Save immediately after booking
                FileHandler.saveUsers(authService.getUsersByCnic());
                FileHandler.saveTickets(authService.getUsersByCnic());
                showTicketWindow(t);
            }
        });

        VBox seatBox = new VBox(4, fieldLabel("Seat #"), spin);
        row.getChildren().addAll(info, seatBox, bookBtn);
        return row;
    }

    // =========================================================
    // TAB: WALLET
    // =========================================================
    private Tab buildWalletTab() {
        Tab tab = new Tab("  💰  Wallet  ");
        VBox page = new VBox(16);
        page.setPadding(new Insets(24));
        page.setStyle("-fx-background-color:" + BG + ";");

        VBox card = card();
        Label balLbl = new Label("Rs. " + String.format("%.2f", currentUser.getWalletBalance()));
        balLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 38));
        balLbl.setTextFill(Color.web(ACCENT_DARK));

        TextField amtField = styledField("Enter amount to add");
        Label msg = msgLabel();
        Button addBtn = accentBtn("Add to Wallet");
        addBtn.setOnAction(e -> {
            try {
                double amt = Double.parseDouble(amtField.getText().trim());
                if (walletService.depositMoney(currentUser, amt)) {
                    balLbl.setText("Rs. " + String.format("%.2f", currentUser.getWalletBalance()));
                    ok(msg, "Rs. " + String.format("%.0f", amt) + " added!");
                    amtField.clear();
                    FileHandler.saveUsers(authService.getUsersByCnic());
                } else err(msg, "Amount must be greater than 0.");
            } catch (NumberFormatException ex) { err(msg, "Enter a valid number."); }
        });

        card.getChildren().addAll(
            sectionTitle("Wallet"),
            subtle("Available Balance"), balLbl,
            new Separator(),
            fieldLabel("Top Up"), amtField, addBtn, msg
        );
        page.getChildren().add(card);
        tab.setContent(page);
        return tab;
    }

    // =========================================================
    // TAB: HISTORY + SEARCH
    // =========================================================
    private Tab buildHistoryTab() {
        Tab tab = new Tab("  📋  My Tickets  ");

        VBox page = new VBox(14);
        page.setPadding(new Insets(24));
        page.setStyle("-fx-background-color:" + BG + ";");

        page.getChildren().add(sectionTitle("My Booking History"));

        // Search bar
        TextField searchField = styledField("Search by city, ticket ID, date...");
        searchField.setMaxWidth(Double.MAX_VALUE);

        // Results container
        VBox results = new VBox(10);
        populateHistory(results, currentUser.getHistory(), "");

        // Live search on every keystroke
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            results.getChildren().clear();
            populateHistory(results, currentUser.getHistory(), newVal.trim().toLowerCase());
        });

        page.getChildren().addAll(searchField, results);

        ScrollPane scroll = new ScrollPane(page);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color:" + BG + ";-fx-background:" + BG + ";");
        tab.setContent(scroll);
        return tab;
    }

    private void populateHistory(VBox container, List<Ticket> tickets, String query) {
        if (tickets.isEmpty()) {
            container.getChildren().add(subtle("No tickets booked yet."));
            return;
        }
        boolean any = false;
        for (Ticket t : tickets) {
            // Build searchable string from ticket fields
            String searchable = (
                t.getTicketId() + " " +
                t.getBus().getRoute().getOrigin() + " " +
                t.getBus().getRoute().getDestination() + " " +
                t.getTravelDate().toString() + " " +
                t.getBus().getBusId()
            ).toLowerCase();

            if (query.isEmpty() || searchable.contains(query)) {
                container.getChildren().add(buildHistoryRow(t));
                any = true;
            }
        }
        if (!any) container.getChildren().add(subtle("No tickets match your search."));
    }

    private HBox buildHistoryRow(Ticket t) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(14, 18, 14, 18));
        row.setStyle("-fx-background-color:#FFFFFF;-fx-background-radius:10;" +
                     "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.06),6,0,0,2);");

        VBox info = new VBox(4); HBox.setHgrow(info, Priority.ALWAYS);
        String date = t.getTravelDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        Label id    = bold("Ticket: " + t.getTicketId(), 13);
        Label route = subtle(t.getBus().getRoute().getOrigin() + " -> " +
                             t.getBus().getRoute().getDestination() +
                             "  |  Seat: " + t.getSeatNumber() +
                             "  |  Rs. " + String.format("%.0f", t.getAmountPaid()));
        Label dt    = subtle("Travel Date: " + date + "   |   Bus: " + t.getBus().getBusId());
        info.getChildren().addAll(id, route, dt);

        Button printBtn = new Button("Print");
        printBtn.setStyle("-fx-background-color:" + PASTEL_BLUE + ";-fx-text-fill:" + TEXT_DARK +
                          ";-fx-background-radius:6;-fx-cursor:hand;-fx-padding:6 14;");
        printBtn.setOnAction(e -> showTicketWindow(t));

        row.getChildren().addAll(info, printBtn);
        return row;
    }

    // =========================================================
    // BOARDING PASS POPUP
    // =========================================================
    private void showTicketWindow(Ticket t) {
        Stage s = new Stage();
        s.initOwner(primaryStage);
        s.initModality(Modality.APPLICATION_MODAL);
        s.setTitle("Boarding Pass - " + t.getTicketId());
        s.setResizable(false);

        VBox wrapper = new VBox(20);
        wrapper.setPadding(new Insets(28));
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setStyle("-fx-background-color:" + BG + ";");

        Node pass = buildBoardingPass(t);

        Button printBtn = accentBtn("Print Ticket");
        Button closeBtn = ghostBtn("Close");
        closeBtn.setOnAction(e -> s.close());
        printBtn.setOnAction(e -> {
            PrinterJob job = PrinterJob.createPrinterJob();
            if (job != null && job.showPrintDialog(s)) {
                if (job.printPage(pass)) job.endJob();
            }
        });
        HBox btns = new HBox(12, printBtn, closeBtn);
        btns.setAlignment(Pos.CENTER);

        wrapper.getChildren().addAll(pass, btns);
        s.setScene(new Scene(wrapper));
        s.show();
    }

    private Node buildBoardingPass(Ticket t) {
        String passenger = t.getUser().getName().toUpperCase();
        String from      = t.getBus().getRoute().getOrigin().toUpperCase();
        String to        = t.getBus().getRoute().getDestination().toUpperCase();
        String date      = t.getTravelDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String time      = t.getBus().getRoute().getDepartureTime();
        String busId     = t.getBus().getBusId();
        String seat      = String.valueOf(t.getSeatNumber());
        String gate      = "G" + (t.getSeatNumber() % 5 + 1);
        String ticketId  = t.getTicketId();

        // --- Left main section ---
        Label sideText = new Label("Transit Boarding");
        sideText.setRotate(-90);
        sideText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 10));
        sideText.setTextFill(Color.WHITE);
        StackPane sidebar = new StackPane(sideText);
        sidebar.setPrefWidth(26);
        sidebar.setStyle("-fx-background-color:" + ACCENT + ";-fx-background-radius:12 0 0 12;");

        VBox mainContent = new VBox(10);
        mainContent.setPadding(new Insets(18, 20, 18, 12));
        HBox.setHgrow(mainContent, Priority.ALWAYS);

        // Name
        mainContent.getChildren().add(twoLine("Name", passenger));

        // From / To
        HBox routeRow = new HBox(8);
        routeRow.setAlignment(Pos.CENTER_LEFT);
        Label arrow = new Label("  ->  ");
        arrow.setFont(Font.font(13)); arrow.setTextFill(Color.web(TEXT_MID));
        routeRow.getChildren().addAll(twoLine("From", from), arrow, twoLine("To", to));
        mainContent.getChildren().add(routeRow);

        // Date / Time
        HBox dtRow = new HBox(28);
        dtRow.getChildren().addAll(twoLine("Date", date), twoLine("Boarding Time", time));
        mainContent.getChildren().add(dtRow);

        // Dashed line
        Label dash = new Label("- - - - - - - - - - - - - - - - - - - - -");
        dash.setFont(Font.font("Courier New", 10));
        dash.setTextFill(Color.web("#AACCDD"));
        mainContent.getChildren().add(dash);

        // Gate / Seat / Flight big text
        HBox bigRow = new HBox(20);
        bigRow.setAlignment(Pos.BASELINE_LEFT);
        bigRow.getChildren().addAll(
            new VBox(2, smallKey("Gate"),   bigVal(gate)),
            new VBox(2, smallKey("Seat"),   bigVal(seat)),
            new VBox(2, smallKey("Flight"), bigVal(busId))
        );
        mainContent.getChildren().add(bigRow);

        // Fare
        Label fareLabel = new Label("Rs. " + String.format("%.0f", t.getAmountPaid()) +
                                    "  (Original: Rs. " + String.format("%.0f", t.getOriginalFare()) + ")");
        fareLabel.setFont(Font.font("Segoe UI", 11));
        fareLabel.setTextFill(Color.web(TEXT_MID));
        mainContent.getChildren().add(fareLabel);

        HBox mainSection = new HBox(sidebar, mainContent);
        mainSection.setPrefWidth(400);
        mainSection.setStyle("-fx-background-color:#F0F7FF;-fx-background-radius:12 0 0 12;");

        // --- Divider ---
        Rectangle divider = new Rectangle(2, 200);
        divider.setFill(Color.web("#AACCDD"));

        // --- Right stub ---
        VBox stub = new VBox(8);
        stub.setPrefWidth(170);
        stub.setPadding(new Insets(18, 16, 18, 16));
        stub.setAlignment(Pos.TOP_CENTER);
        stub.setStyle("-fx-background-color:" + ACCENT + ";-fx-background-radius:0 12 12 0;");

        Label sn = new Label("Name"); sn.setFont(Font.font("Segoe UI", 9)); sn.setTextFill(Color.web("#C5DDEF"));
        Label sv = new Label(passenger); sv.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        sv.setTextFill(Color.WHITE); sv.setWrapText(true); sv.setMaxWidth(150);

        HBox r1 = new HBox(16, stubTwo("Gate", gate), stubTwo("Date", date));
        HBox r2 = new HBox(16, stubTwo("Seat", seat), stubTwo("Time", time));
        HBox r3 = new HBox(stubTwo("Flight", busId));

        Label dashStub = new Label("- - - - - - - -");
        dashStub.setFont(Font.font("Courier New", 9)); dashStub.setTextFill(Color.web("#94B8D0"));

        HBox barcode = new HBox(1.5);
        barcode.setAlignment(Pos.CENTER);
        barcode.setPadding(new Insets(6, 0, 4, 0));
        int[] bars = {1,2,1,3,1,1,2,1,2,3,1,2,1,1,3,2,1,2,1,3,1,2,1,1,2,3,1,2};
        for (int w : bars) { Rectangle b = new Rectangle(w, 32); b.setFill(Color.WHITE); barcode.getChildren().add(b); }

        Label idLbl = new Label(ticketId);
        idLbl.setFont(Font.font("Courier New", 8)); idLbl.setTextFill(Color.web("#C5DDEF"));

        stub.getChildren().addAll(sn, sv, new Separator(), r1, r2, r3, dashStub, barcode, idLbl);

        HBox pass = new HBox(mainSection, divider, stub);
        pass.setStyle("-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.15),14,0,0,4);");
        return pass;
    }

    // =========================================================
    // UI HELPERS
    // =========================================================
    private VBox twoLine(String key, String val) {
        Label k = new Label(key); k.setFont(Font.font("Segoe UI", 10)); k.setTextFill(Color.web(TEXT_MID));
        Label v = new Label(val); v.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13)); v.setTextFill(Color.web(TEXT_DARK));
        return new VBox(1, k, v);
    }
    private VBox stubTwo(String key, String val) {
        Label k = new Label(key); k.setFont(Font.font("Segoe UI", 9)); k.setTextFill(Color.web("#C5DDEF"));
        Label v = new Label(val); v.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12)); v.setTextFill(Color.WHITE);
        return new VBox(1, k, v);
    }
    private Label smallKey(String t) {
        Label l = new Label(t); l.setFont(Font.font("Segoe UI", 9)); l.setTextFill(Color.web(TEXT_MID)); return l;
    }
    private Label bigVal(String t) {
        Label l = new Label(t); l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22)); l.setTextFill(Color.web(TEXT_DARK)); return l;
    }
    private VBox card() {
        VBox v = new VBox(12); v.setPadding(new Insets(20));
        v.setStyle("-fx-background-color:#FFFFFF;-fx-background-radius:12;" +
                   "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.07),8,0,0,2);");
        return v;
    }
    private Label sectionTitle(String t) {
        Label l = new Label(t); l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 17)); l.setTextFill(Color.web(ACCENT_DARK)); return l;
    }
    private Label fieldLabel(String t) {
        Label l = new Label(t); l.setFont(Font.font("Segoe UI", 12)); l.setTextFill(Color.web(TEXT_MID)); return l;
    }
    private Label subtle(String t) {
        Label l = new Label(t); l.setFont(Font.font("Segoe UI", 12)); l.setTextFill(Color.web(TEXT_MID)); return l;
    }
    private Label bold(String t, int size) {
        Label l = new Label(t); l.setFont(Font.font("Segoe UI", FontWeight.BOLD, size)); l.setTextFill(Color.web(TEXT_DARK)); return l;
    }
    private Label msgLabel() {
        Label l = new Label(""); l.setFont(Font.font("Segoe UI", 12)); l.setWrapText(true); return l;
    }
    private void ok(Label l, String msg)  { l.setTextFill(Color.web(GREEN_OK)); l.setText(msg); }
    private void err(Label l, String msg) { l.setTextFill(Color.web(RED_ERR));  l.setText(msg); }

    private TextField styledField(String prompt) {
        TextField f = new TextField(); f.setPromptText(prompt); f.setStyle(fieldStyle()); f.setMaxWidth(Double.MAX_VALUE); return f;
    }
    private PasswordField styledPass(String prompt) {
        PasswordField f = new PasswordField(); f.setPromptText(prompt); f.setStyle(fieldStyle()); f.setMaxWidth(Double.MAX_VALUE); return f;
    }
    private ComboBox<String> styledCombo(List<String> items, String prompt) {
        ComboBox<String> cb = new ComboBox<>(); cb.getItems().addAll(items);
        cb.setPromptText(prompt); cb.setMaxWidth(Double.MAX_VALUE); cb.setStyle(fieldStyle()); return cb;
    }
    private Button accentBtn(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:" + ACCENT + ";-fx-text-fill:white;-fx-font-weight:bold;" +
                   "-fx-font-size:13;-fx-background-radius:8;-fx-cursor:hand;-fx-padding:8 18;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color:" + ACCENT_DARK + ";-fx-text-fill:white;-fx-font-weight:bold;" +
                   "-fx-font-size:13;-fx-background-radius:8;-fx-cursor:hand;-fx-padding:8 18;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color:" + ACCENT + ";-fx-text-fill:white;-fx-font-weight:bold;" +
                   "-fx-font-size:13;-fx-background-radius:8;-fx-cursor:hand;-fx-padding:8 18;"));
        return b;
    }
    private Button ghostBtn(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:transparent;-fx-text-fill:white;" +
                   "-fx-border-color:white;-fx-border-radius:6;-fx-cursor:hand;-fx-padding:6 14;");
        return b;
    }
    private String fieldStyle() {
        return "-fx-background-color:#F2F8FC;-fx-border-color:" + PASTEL_BLUE +
               ";-fx-border-radius:6;-fx-background-radius:6;-fx-padding:7 10;";
    }

    public static void main(String[] args) { launch(args); }
}
