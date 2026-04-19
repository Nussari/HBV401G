package g1t.hbv401g.view;

import g1t.hbv401g.model.Booking;
import g1t.hbv401g.model.DayTrip;
import g1t.hbv401g.model.Flight;
import g1t.hbv401g.model.Trip;
import g1t.hbv401g.model.User;
import g1t.hbv401g.view.components.InfoRow;
import g1t.hbv401g.view.components.TripTitleEditor;
import g1t.hbv401g.view.state.AppState;
import g1t.hbv401g.view.util.Formats;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class UserProfileView {

    private final VBox root = new VBox();

    public UserProfileView() {
        root.setFillWidth(true);
        render();
        AppState.get().subscribe(this::render);
    }

    public Node getRoot() {
        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return scroll;
    }

    private void render() {
        root.getChildren().clear();
        if (!AppState.get().isLoggedIn()) {
            root.getChildren().add(buildLoggedOut());
        } else {
            root.getChildren().add(buildLoggedIn());
        }
    }

    private VBox buildLoggedIn() {
        VBox page = new VBox(40, buildHero(), buildPanels());
        page.setPadding(new Insets(50, 28, 120, 28));
        page.setMaxWidth(1240);

        StackPane wrap = new StackPane(page);
        wrap.setAlignment(Pos.TOP_CENTER);
        return new VBox(wrap);
    }

    private HBox buildHero() {
        User user = AppState.get().getCurrentUser();
        String username = user.getUsername() == null ? "" : user.getUsername();

        VBox center = buildWelcome(username);
        HBox.setHgrow(center, Priority.ALWAYS);

        HBox hero = new HBox(32, center);
        hero.getStyleClass().add("border-bottom");
        hero.setAlignment(Pos.CENTER_LEFT);
        hero.setPadding(new Insets(0, 0, 40, 0));
        return hero;
    }

    private VBox buildWelcome(String username) {
        Label welcome = new Label("Welcome back,");
        welcome.getStyleClass().addAll("display", "fs-56", "welcome-line");

        Label name = new Label(username + ".");
        name.getStyleClass().addAll("display", "fs-56", "welcome-line-alt");

        return new VBox(10, welcome, name);
    }

    private GridPane buildPanels() {
        GridPane grid = new GridPane();
        grid.setHgap(32);
        ColumnConstraints c1 = new ColumnConstraints(); c1.setPercentWidth(42);
        ColumnConstraints c2 = new ColumnConstraints(); c2.setPercentWidth(58);
        grid.getColumnConstraints().addAll(c1, c2);

        grid.add(buildAccountPanel(), 0, 0);
        grid.add(buildBookingsPanel(), 1, 0);
        return grid;
    }

    private VBox buildAccountPanel() {
        AppState s = AppState.get();
        User user = s.getCurrentUser();

        VBox panel = new VBox(18);
        panel.getStyleClass().add("panel");

        panel.getChildren().setAll(
                buildAccountHead(),
                InfoRow.text("USERNAME", user.getUsername(), "EDIT",
                        v -> { if (!v.isEmpty()) { user.setUsername(v); s.notifyListeners(); } }),
                InfoRow.text("EMAIL", user.getEmail(), "EDIT",
                        v -> { if (!v.isEmpty()) { user.setEmail(v); s.notifyListeners(); } }),
                InfoRow.password("PASSWORD", "CHANGE",
                        v -> {
                            if (!v.isEmpty() && s.getCurrentUser() != null) {
                                s.getCurrentUser().setPassword(v);
                            }
                            s.notifyListeners();
                        }),
                buildDivider(),
                buildLogoutButton());
        return panel;
    }

    private HBox buildAccountHead() {
        Label title = new Label("Account");
        title.getStyleClass().add("panel-title");
        Label dash = new Label("01");
        dash.getStyleClass().add("eyebrow");
        HBox row = new HBox(12, title, dash);
        row.setAlignment(Pos.BASELINE_LEFT);
        return row;
    }

    private Region buildDivider() {
        Region d = new Region();
        d.getStyleClass().add("divider-h");
        VBox.setMargin(d, new Insets(12, 0, 12, 0));
        return d;
    }

    private Button buildLogoutButton() {
        Button logout = new Button("Log out");
        logout.getStyleClass().addAll("btn", "btn-ghost");
        logout.setMaxWidth(Double.MAX_VALUE);
        logout.setOnAction(e -> {
            AppState.get().logout();
            ViewRouter.get().goTo(ViewRouter.Page.HOME);
        });
        return logout;
    }

    private VBox buildBookingsPanel() {
        AppState s = AppState.get();
        VBox panel = new VBox();
        panel.getStyleClass().add("panel");

        Label title = new Label("Your bookings");
        title.getStyleClass().add("panel-title");
        Label dash = new Label(s.getBookings().size() + " TOTAL");
        dash.getStyleClass().add("eyebrow");
        HBox titleRow = new HBox(12, title, dash);
        titleRow.setAlignment(Pos.BASELINE_LEFT);

        VBox items = new VBox();
        VBox.setMargin(items, new Insets(20, 0, 0, 0));

        if (s.getBookings().isEmpty()) {
            Label empty = new Label("No bookings yet. Build a trip on the search page.");
            empty.getStyleClass().add("muted");
            items.setPadding(new Insets(20, 0, 0, 0));
            items.getChildren().setAll(empty);
        } else {
            for (Booking b : s.getBookings()) {
                items.getChildren().add(buildBookingRow(b));
            }
        }

        panel.getChildren().setAll(titleRow, items);
        return panel;
    }

    private HBox buildBookingRow(Booking b) {
        Trip trip = b.getTrip();

        VBox info = new VBox(6,
                trip == null ? new Label("") : TripTitleEditor.create(trip, "booking-title"),
                buildBookingSummary(b),
                buildBookingBadges(b));
        HBox.setHgrow(info, Priority.ALWAYS);

        VBox right = buildBookingTotal(b);

        HBox row = new HBox(22, info, right);
        row.getStyleClass().add("border-bottom");
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(20, 0, 20, 0));
        return row;
    }

    private Label buildBookingSummary(Booking b) {
        Label summary = new Label(describeComponents(b));
        summary.getStyleClass().addAll("mono", "fs-11");
        return summary;
    }

    private HBox buildBookingBadges(Booking b) {
        Trip trip = b.getTrip();
        Label status = new Label("Booked");
        status.getStyleClass().addAll("tag", "status-upcoming");
        Label items = new Label((trip == null ? 0 : trip.getComponentCount()) + " items");
        items.getStyleClass().add("tag");
        return new HBox(6, status, items);
    }

    private VBox buildBookingTotal(Booking b) {
        Label total = new Label(Formats.moneyInt(b.getTotal()));
        total.getStyleClass().add("booking-total");
        Label lbl = new Label("TOTAL");
        lbl.getStyleClass().add("eyebrow");
        VBox right = new VBox(4, total, lbl);
        right.setAlignment(Pos.CENTER_RIGHT);
        return right;
    }

    private String describeComponents(Booking b) {
        Trip t = b.getTrip();
        if (t == null) return "";
        StringBuilder sb = new StringBuilder();
        int flightIdx = 0;
        for (Flight f : t.getFlights()) {
            if (sb.length() > 0) sb.append("  \u00B7  ");
            sb.append("Flight ")
              .append(f.getDepartureA().getCode())
              .append("\u2192")
              .append(f.getArrivalA().getCode());
            if (flightIdx < b.getFlightBookings().size()) {
                sb.append("  ref ").append(b.getFlightBookings().get(flightIdx).getBookingReference());
            }
            flightIdx++;
        }
        for (DayTrip dt : t.getDayTrips()) {
            if (sb.length() > 0) sb.append("  \u00B7  ");
            sb.append("Day-trip: ").append(dt.getName());
        }
        return sb.toString();
    }

    private StackPane buildLoggedOut() {
        boolean[] registerMode = { false };

        Label eyebrow = new Label("SIGN IN");
        eyebrow.getStyleClass().add("eyebrow");

        Label title = new Label("Welcome back.");
        title.getStyleClass().addAll("display", "fs-40");

        Label sub = new Label("Log in to see your bookings and saved trips.");
        sub.getStyleClass().add("muted");

        TextField username = new TextField();
        username.setPromptText("Username (optional)");
        username.getStyleClass().add("login-input");
        username.setManaged(false);
        username.setVisible(false);

        TextField email = new TextField();
        email.setPromptText("Email");
        email.getStyleClass().add("login-input");

        PasswordField pw = new PasswordField();
        pw.setPromptText("Password");
        pw.getStyleClass().add("login-input");

        Label err = new Label();
        err.getStyleClass().addAll("muted", "login-error");

        Button primary = new Button("Log in  \u2192");
        primary.getStyleClass().addAll("btn", "btn-primary");
        primary.setMaxWidth(Double.MAX_VALUE);

        HBox switchRow = buildSwitchRow(registerMode, eyebrow, title, sub, username, primary);

        Runnable submit = () -> {
            try {
                err.setText("");
                if (registerMode[0]) {
                    AppState.get().register(email.getText(), pw.getText(), username.getText().trim());
                } else {
                    AppState.get().login(email.getText(), pw.getText());
                }
            } catch (Exception ex) {
                err.setText(ex.getMessage());
            }
        };
        primary.setOnAction(e -> submit.run());
        pw.setOnAction(e -> submit.run());
        email.setOnAction(e -> submit.run());
        username.setOnAction(e -> submit.run());

        VBox card = new VBox(18, eyebrow, title, sub, username, email, pw, err, primary, switchRow);
        card.getStyleClass().add("login-card");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setMaxWidth(420);
        card.setMinWidth(360);

        StackPane wrap = new StackPane(card);
        wrap.setPadding(new Insets(120, 28, 120, 28));
        return wrap;
    }

    private HBox buildSwitchRow(boolean[] registerMode, Label eyebrow, Label title, Label sub,
                                TextField username, Button primary) {
        Label switchPrompt = new Label("New here?");
        switchPrompt.getStyleClass().add("muted");
        Button switchBtn = new Button("Create an account");
        switchBtn.getStyleClass().add("edit-link");

        switchBtn.setOnAction(e -> {
            registerMode[0] = !registerMode[0];
            if (registerMode[0]) {
                eyebrow.setText("SIGN UP");
                title.setText("Create account.");
                sub.setText("Set up an account to save trips and book them.");
                username.setManaged(true);
                username.setVisible(true);
                primary.setText("Create account  \u2192");
                switchPrompt.setText("Already have an account?");
                switchBtn.setText("Log in");
            } else {
                eyebrow.setText("SIGN IN");
                title.setText("Welcome back.");
                sub.setText("Log in to see your bookings and saved trips.");
                username.setManaged(false);
                username.setVisible(false);
                primary.setText("Log in  \u2192");
                switchPrompt.setText("New here?");
                switchBtn.setText("Create an account");
            }
        });

        HBox row = new HBox(8, switchPrompt, switchBtn);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

}
