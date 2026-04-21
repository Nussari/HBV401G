package g1t.hbv401g.view;

import g1t.hbv401g.model.Booking;
import g1t.hbv401g.model.Cart;
import g1t.hbv401g.model.Flight;
import g1t.hbv401g.model.HotelSelection;
import g1t.hbv401g.model.Trip;
import g1t.teamD.model.DayTrip;
import g1t.hbv401g.view.components.ComponentRow;
import g1t.hbv401g.view.components.TripTitleEditor;
import g1t.hbv401g.view.state.AppState;
import g1t.hbv401g.view.util.Formats;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;


public class CheckoutView {

    private final VBox root = new VBox();

    private boolean justBooked = false;
    private int lastBookingCount = 0;
    private double lastBookingTotal = 0;

    public CheckoutView() {
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

        if (justBooked) {
            root.getChildren().add(buildConfirmation());
        } else if (AppState.get().getCart().isEmpty()) {
            root.getChildren().add(buildEmpty());
        } else {
            root.getChildren().add(buildCheckout());
        }
    }

    private VBox buildCheckout() {
        GridPane grid = new GridPane();
        grid.setHgap(60);
        ColumnConstraints a = new ColumnConstraints(); a.setPercentWidth(47);
        ColumnConstraints b = new ColumnConstraints(); b.setPercentWidth(53);
        grid.getColumnConstraints().addAll(a, b);

        VBox right = buildTotalsPanel();
        right.setMaxHeight(Region.USE_PREF_SIZE);

        VBox left = new VBox(24, buildItemsPanel());

        grid.add(right, 0, 0);
        grid.add(left,  1, 0);
        GridPane.setValignment(right, VPos.TOP);

        VBox page = new VBox(grid);
        page.setPadding(new Insets(80, 28, 140, 28));
        page.setMaxWidth(1180);

        StackPane wrap = new StackPane(page);
        wrap.setAlignment(Pos.TOP_CENTER);
        return new VBox(wrap);
    }

    private VBox buildItemsPanel() {
        Cart cart = AppState.get().getCart();

        VBox panel = new VBox();
        panel.getStyleClass().add("panel");
        panel.setPadding(Insets.EMPTY);

        HBox head = buildItemsHead(cart);

        VBox trips = new VBox(16);
        trips.setPadding(new Insets(18, 26, 22, 26));
        List<Trip> list = cart.getTrips();
        for (int i = 0; i < list.size(); i++) {
            trips.getChildren().add(buildTripBlock(list.get(i), i + 1, list.size()));
        }

        panel.getChildren().setAll(head, trips);
        return panel;
    }

    private HBox buildItemsHead(Cart cart) {
        Label title = new Label("Itinerary");
        title.getStyleClass().add("panel-title");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Label count = new Label(cart.size() + (cart.size() == 1 ? " TRIP" : " TRIPS"));
        count.getStyleClass().add("eyebrow");

        HBox head = new HBox(title, sp, count);
        head.getStyleClass().add("border-bottom");
        head.setAlignment(Pos.BASELINE_LEFT);
        head.setPadding(new Insets(22, 26, 22, 26));
        return head;
    }

    private VBox buildTripBlock(Trip trip, int index, int total) {
        VBox block = new VBox(12);
        block.getStyleClass().add("trip-card");
        block.setPadding(new Insets(18, 20, 18, 20));

        block.getChildren().setAll(
                buildTripEyebrow(trip, index, total),
                TripTitleEditor.create(trip, "trip-card-title"),
                buildIncludesLabel(),
                buildComponentList(trip));
        return block;
    }

    private HBox buildTripEyebrow(Trip trip, int index, int total) {
        Label tag = new Label(String.format("TRIP %02d / %02d", index, total));
        tag.getStyleClass().addAll("eyebrow", "trip-card-tag");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Label totalLbl = new Label(Formats.moneyInt(trip.getTotalCost()));
        totalLbl.getStyleClass().addAll("mono", "fs-16", "bold", "text-base");

        HBox row = new HBox(10, tag, sp, totalLbl);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private Label buildIncludesLabel() {
        Label l = new Label("INCLUDES");
        l.getStyleClass().add("cart-includes");
        return l;
    }

    private VBox buildComponentList(Trip trip) {
        VBox components = new VBox(8);
        components.getStyleClass().add("trip-card-components");
        components.setPadding(new Insets(12, 14, 12, 14));
        for (Flight f : trip.getFlights()) {
            components.getChildren().add(
                    ComponentRow.readOnly("FLI", Formats.flightLong(f), f.getPrice()));
        }
        HotelSelection hotel = trip.getHotel();
        if (hotel != null) {
            components.getChildren().add(
                    ComponentRow.readOnly("HTL", Formats.hotelLong(hotel), hotel.getTotalCost()));
        }
        for (DayTrip dt : trip.getDayTrips()) {
            components.getChildren().add(
                    ComponentRow.readOnly("DAY", Formats.dayTripLong(dt), dt.getPrice()));
        }
        return components;
    }

    private VBox buildTotalsPanel() {
        double total = AppState.get().cartSubtotal();

        VBox totals = new VBox(10);
        totals.getStyleClass().add("totals");

        Label intro = new Label("Review your trip and confirm.");
        intro.getStyleClass().add("muted");
        intro.setWrapText(true);
        VBox.setMargin(intro, new Insets(0, 0, 8, 0));

        HBox grand = buildGrandTotalRow(total);

        Button buy = new Button("Buy now  \u2192");
        buy.getStyleClass().add("buy-btn");
        buy.setMaxWidth(Double.MAX_VALUE);
        buy.setOnAction(e -> confirmPurchase());
        VBox.setMargin(buy, new Insets(18, 0, 0, 0));

        totals.getChildren().setAll(intro, grand, buy);
        return totals;
    }

    private HBox buildGrandTotalRow(double total) {
        Label label = new Label("TOTAL");
        label.getStyleClass().addAll("eyebrow", "fs-20");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Label value = new Label(Formats.moneyInt(total));
        value.getStyleClass().add("totals-grand");

        HBox row = new HBox(label, sp, value);
        row.setAlignment(Pos.BASELINE_LEFT);
        return row;
    }

    private void confirmPurchase() {
        if (!AppState.get().isLoggedIn()) {
            ViewRouter.get().goTo(ViewRouter.Page.USER);
            return;
        }
        double total = AppState.get().cartSubtotal();
        int count = AppState.get().getCart().size();
        List<Booking> created = AppState.get().checkout();
        justBooked = true;
        lastBookingCount = created.size() > 0 ? created.size() : count;
        lastBookingTotal = total;
        render();
    }

    private StackPane buildConfirmation() {
        Label eyebrow = new Label("BOOKED");
        eyebrow.getStyleClass().add("eyebrow");

        Label h = new Label("All set.");
        h.getStyleClass().addAll("display", "fs-64");

        Label sub = new Label(lastBookingCount + (lastBookingCount == 1 ? " trip" : " trips")
                + " confirmed  \u00B7  " + Formats.moneyInt(lastBookingTotal));
        sub.getStyleClass().add("muted");

        Button toProfile = new Button("See your bookings  \u2192");
        toProfile.getStyleClass().addAll("btn", "btn-primary", "btn-lg");
        toProfile.setOnAction(e -> {
            justBooked = false;
            ViewRouter.get().goTo(ViewRouter.Page.USER);
        });

        Button toHome = new Button("Back to home");
        toHome.getStyleClass().addAll("btn", "btn-ghost");
        toHome.setOnAction(e -> {
            justBooked = false;
            ViewRouter.get().goTo(ViewRouter.Page.HOME);
        });

        VBox box = new VBox(18, eyebrow, h, sub, toProfile, toHome);
        box.setAlignment(Pos.CENTER);

        StackPane wrap = new StackPane(box);
        wrap.setPadding(new Insets(140, 40, 140, 40));
        return wrap;
    }

    private StackPane buildEmpty() {
        Label h = new Label("Your cart is empty.");
        h.getStyleClass().addAll("display", "fs-56");

        Label sub = new Label("Head to search to build a trip, then come back here to check out.");
        sub.getStyleClass().add("muted");
        sub.setWrapText(true);
        sub.setMaxWidth(360);

        Button cta = new Button("Start a search  \u2192");
        cta.getStyleClass().addAll("btn", "btn-primary", "btn-lg");
        cta.setOnAction(e -> ViewRouter.get().goTo(ViewRouter.Page.SEARCH));

        VBox box = new VBox(16, h, sub, cta);
        box.setAlignment(Pos.CENTER);

        StackPane wrap = new StackPane(box);
        wrap.setPadding(new Insets(120, 40, 120, 40));
        return wrap;
    }
}
