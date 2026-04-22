package g1t.hbv401g.view;

import g1t.hbv401g.controller.BookingController;
import g1t.hbv401g.model.Booking;
import g1t.teamF.model.Flight;
import g1t.hbv401g.model.HotelSelection;
import g1t.hbv401g.model.Trip;
import g1t.hbv401g.model.User;
import g1t.teamD.model.DayTrip;
import g1t.hbv401g.view.components.ComponentRow;
import g1t.hbv401g.view.components.InfoRow;
import g1t.hbv401g.view.components.TripTitleEditor;
import g1t.hbv401g.view.state.AppState;
import g1t.hbv401g.view.util.Formats;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
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

        VBox accountPanel = buildAccountPanel();
        GridPane.setValignment(accountPanel, VPos.TOP);
        GridPane.setFillHeight(accountPanel, false);

        grid.add(accountPanel, 0, 0);
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
                        v -> { if (!v.isEmpty()) s.updateUsername(v); }),
                InfoRow.text("EMAIL", user.getEmail(), "EDIT",
                        v -> { if (!v.isEmpty()) s.updateEmail(v); }),
                buildDivider(),
                buildLogoutButton(),
                buildChangePasswordButton());
        return panel;
    }

    private Button buildChangePasswordButton() {
        Button change = new Button("Change password");
        change.getStyleClass().addAll("btn", "btn-danger");
        change.setMaxWidth(Double.MAX_VALUE);
        change.setOnAction(e -> promptChangePassword());
        return change;
    }

    private void promptChangePassword() {
        AppState s = AppState.get();
        if (s.getCurrentUser() == null) return;

        PasswordField pw = new PasswordField();
        pw.setPromptText("New password");

        javafx.scene.control.Dialog<String> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Change password");
        dialog.setHeaderText(null);
        dialog.getDialogPane().setContent(pw);
        dialog.getDialogPane().getButtonTypes().addAll(
                javafx.scene.control.ButtonType.OK,
                javafx.scene.control.ButtonType.CANCEL);
        dialog.setResultConverter(bt ->
                bt == javafx.scene.control.ButtonType.OK ? pw.getText() : null);
        javafx.application.Platform.runLater(pw::requestFocus);

        dialog.showAndWait().ifPresent(v -> {
            if (v != null && !v.isEmpty()) s.changePassword(v);
        });
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

        VBox items = new VBox(18);
        VBox.setMargin(items, new Insets(20, 0, 0, 0));

        if (s.getBookings().isEmpty()) {
            Label empty = new Label("No bookings yet. Build a trip on the search page.");
            empty.getStyleClass().add("muted");
            items.setPadding(new Insets(20, 0, 0, 0));
            items.getChildren().setAll(empty);
        } else {
            int i = 1;
            int total = s.getBookings().size();
            for (Booking b : s.getBookings()) {
                items.getChildren().add(buildBookingCard(b, i, total));
                i++;
            }
        }

        panel.getChildren().setAll(titleRow, items);
        return panel;
    }

    private VBox buildBookingCard(Booking b, int index, int total) {
        Trip trip = b.getTrip();
        VBox card = new VBox(12);
        card.getStyleClass().add("trip-card");
        card.setPadding(new Insets(16, 16, 14, 16));

        card.getChildren().setAll(
                buildBookingEyebrowRow(b, index, total),
                trip == null ? new Label("") : TripTitleEditor.create(trip, "trip-card-title"),
                buildBookingBadges(b),
                buildIncludesLabel(),
                buildBookingComponentList(b),
                buildBookingFooterRow(b));
        return card;
    }

    private HBox buildBookingEyebrowRow(Booking b, int index, int total) {
        Label tag = new Label(String.format("BOOKING %02d / %02d", index, total));
        tag.getStyleClass().addAll("eyebrow", "trip-card-tag");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Label bookingTotal = new Label(Formats.money(b.getTotal()));
        bookingTotal.getStyleClass().add("cart-trip-total");

        HBox row = new HBox(10, tag, sp, bookingTotal);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private HBox buildBookingBadges(Booking b) {
        Trip trip = b.getTrip();
        Label status = new Label("Booked");
        status.getStyleClass().addAll("tag", "status-upcoming");
        Label items = new Label((trip == null ? 0 : trip.getComponentCount()) + " items");
        items.getStyleClass().add("tag");
        return new HBox(6, status, items);
    }

    private Label buildIncludesLabel() {
        Label l = new Label("INCLUDES");
        l.getStyleClass().add("cart-includes");
        return l;
    }

    private VBox buildBookingComponentList(Booking b) {
        VBox components = new VBox(6);
        components.getStyleClass().add("trip-card-components");
        components.setPadding(new Insets(10, 12, 10, 12));

        Trip trip = b.getTrip();
        if (trip != null) {
            for (Flight f : trip.getFlights()) {
                components.getChildren().add(ComponentRow.cancellable(
                        "FLI", Formats.flightShort(f), f.getPrice(),
                        () -> cancelBookingComponent(b, f)));
            }
            HotelSelection hotel = trip.getHotel();
            if (hotel != null) {
                components.getChildren().add(ComponentRow.cancellable(
                        "HTL", Formats.hotelShort(hotel), hotel.getTotalCost(),
                        () -> cancelBookingComponent(b, hotel)));
            }
            for (DayTrip dt : trip.getDayTrips()) {
                components.getChildren().add(ComponentRow.cancellable(
                        "DAY", Formats.dayTripShort(dt), dt.getPrice(),
                        () -> cancelBookingComponent(b, dt)));
            }
        }
        if (components.getChildren().isEmpty()) {
            Label none = new Label("No items");
            none.getStyleClass().add("muted");
            components.getChildren().add(none);
        }
        return components;
    }

    private HBox buildBookingFooterRow(Booking b) {
        Button cancel = new Button("Cancel booking");
        cancel.getStyleClass().addAll("edit-link", "fs-11");
        cancel.setOnAction(e -> cancelBooking(b));

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        HBox footer = new HBox(sp, cancel);
        return footer;
    }

    private void cancelBooking(Booking b) {
        handleCancellationResult(AppState.get().cancelBooking(b));
    }

    private void cancelBookingComponent(Booking b, Object component) {
        handleCancellationResult(AppState.get().cancelBookingComponent(b, component));
    }

    private void handleCancellationResult(BookingController.CancellationResult result) {
        switch (result) {
            case HOTEL_REQUIRES_PHONE -> showInfo("Hotel cancellation",
                    "Please call the hotel directly to cancel your reservation.");
            case FAILED -> showInfo("Cancellation failed",
                    "We couldn't cancel that. Please try again.");
            case CANCELLED -> { /* listeners refresh the view */ }
        }
    }

    private void showInfo(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
