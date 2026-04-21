package g1t.hbv401g.view;

import g1t.hbv401g.model.Cart;
import g1t.teamD.model.DayTrip;
import g1t.hbv401g.model.Flight;
import g1t.hbv401g.model.Trip;
import g1t.hbv401g.view.components.ComponentRow;
import g1t.hbv401g.view.components.TripTitleEditor;
import g1t.hbv401g.view.state.AppState;
import g1t.hbv401g.view.util.Formats;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;


public class CartDrawerView {

    private static final double DRAWER_WIDTH = 440;

    private final StackPane root = new StackPane();
    private final Pane scrim = new Pane();
    private final VBox drawer = new VBox();
    private final VBox itemsBox = new VBox();
    private final Label grandTotal = new Label("$0");
    private final Button checkoutBtn = new Button("Proceed to checkout");

    private boolean open = false;

    public CartDrawerView() {
        root.setPickOnBounds(false);
        root.setVisible(false);
        root.setManaged(false);

        configureScrim();
        configureDrawer();

        StackPane.setAlignment(drawer, Pos.CENTER_RIGHT);
        root.getChildren().setAll(scrim, drawer);

        buildDrawerChrome();
        AppState.get().subscribe(this::refresh);
        refresh();
    }

    public StackPane getRoot() { return root; }


    private void configureScrim() {
        scrim.getStyleClass().add("tx-cart-scrim");
        scrim.setOpacity(0);
        scrim.setOnMouseClicked(e -> close());
    }

    private void configureDrawer() {
        drawer.getStyleClass().add("tx-cart");
        drawer.setPrefWidth(DRAWER_WIDTH);
        drawer.setMaxWidth(DRAWER_WIDTH);
        drawer.setTranslateX(DRAWER_WIDTH);
    }

    private void buildDrawerChrome() {
        HBox head = buildHead();

        itemsBox.setPadding(new Insets(16, 18, 16, 18));
        itemsBox.setSpacing(18);

        ScrollPane scroll = new ScrollPane(itemsBox);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        VBox foot = buildFoot();

        drawer.getChildren().setAll(head, scroll, foot);
    }

    private HBox buildHead() {
        Label title = new Label("Your cart");
        title.getStyleClass().add("tx-cart-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button close = new Button("\u2715");
        close.getStyleClass().add("tx-cart-close");
        close.setOnAction(e -> close());

        HBox head = new HBox(title, spacer, close);
        head.getStyleClass().add("tx-cart-head");
        head.setAlignment(Pos.CENTER_LEFT);
        return head;
    }

    private VBox buildFoot() {
        Label totalLabel = new Label("Total");
        totalLabel.getStyleClass().add("eyebrow");

        Region ts = new Region();
        HBox.setHgrow(ts, Priority.ALWAYS);

        grandTotal.getStyleClass().add("tx-cart-grand");

        HBox totalRow = new HBox(totalLabel, ts, grandTotal);
        totalRow.setAlignment(Pos.BASELINE_LEFT);

        checkoutBtn.getStyleClass().addAll("btn", "btn-primary");
        checkoutBtn.setMaxWidth(Double.MAX_VALUE);
        checkoutBtn.setOnAction(e -> {
            close();
            ViewRouter.get().goTo(ViewRouter.Page.CHECKOUT);
        });

        VBox foot = new VBox(14, totalRow, checkoutBtn);
        foot.getStyleClass().add("tx-cart-foot");
        return foot;
    }

    private void refresh() {
        Cart cart = AppState.get().getCart();
        itemsBox.getChildren().clear();

        if (cart.isEmpty()) {
            itemsBox.getChildren().add(buildEmptyState());
            checkoutBtn.setDisable(true);
        } else {
            int i = 1;
            int total = cart.size();
            for (Trip trip : cart.getTrips()) {
                itemsBox.getChildren().add(buildTripCard(trip, i, total));
                i++;
            }
            checkoutBtn.setDisable(false);
        }
        grandTotal.setText(Formats.money(cart.getSubtotal()));
    }

    private VBox buildEmptyState() {
        Label glyph = new Label("\u25CB");
        glyph.getStyleClass().add("cart-empty-glyph");

        Label txt = new Label("Nothing in your cart yet.");
        txt.getStyleClass().add("muted");

        Label sub = new Label("Head to search to build a trip.");
        sub.getStyleClass().add("muted");

        VBox empty = new VBox(10, glyph, txt, sub);
        empty.setAlignment(Pos.CENTER);
        empty.setPadding(new Insets(60, 20, 60, 20));
        return empty;
    }

    private VBox buildTripCard(Trip trip, int index, int total) {
        VBox card = new VBox(12);
        card.getStyleClass().add("trip-card");
        card.setPadding(new Insets(16, 16, 14, 16));

        card.getChildren().setAll(
                buildEyebrowRow(trip, index, total),
                TripTitleEditor.create(trip, "trip-card-title"),
                buildIncludesLabel(),
                buildComponentList(trip),
                buildFooterRow(trip));
        return card;
    }

    private HBox buildEyebrowRow(Trip trip, int index, int total) {
        Label tag = new Label(String.format("TRIP %02d / %02d", index, total));
        tag.getStyleClass().addAll("eyebrow", "trip-card-tag");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Label tripTotal = new Label(Formats.money(trip.getTotalCost()));
        tripTotal.getStyleClass().add("cart-trip-total");

        HBox row = new HBox(10, tag, sp, tripTotal);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private Label buildIncludesLabel() {
        Label l = new Label("INCLUDES");
        l.getStyleClass().add("cart-includes");
        return l;
    }

    private VBox buildComponentList(Trip trip) {
        VBox components = new VBox(6);
        components.getStyleClass().add("trip-card-components");
        components.setPadding(new Insets(10, 12, 10, 12));

        for (Flight f : trip.getFlights()) {
            components.getChildren().add(
                    ComponentRow.removable("FLI", Formats.flightShort(f), f.getPrice(), trip, f));
        }
        for (DayTrip dt : trip.getDayTrips()) {
            components.getChildren().add(
                    ComponentRow.removable("DAY", Formats.dayTripShort(dt), dt.getPrice(), trip, dt));
        }
        if (components.getChildren().isEmpty()) {
            Label none = new Label("No items");
            none.getStyleClass().add("muted");
            components.getChildren().add(none);
        }
        return components;
    }

    private HBox buildFooterRow(Trip trip) {
        Button remove = new Button("Remove trip");
        remove.getStyleClass().addAll("edit-link", "fs-11");
        remove.setOnAction(e -> AppState.get().removeFromCart(trip));

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        HBox footer = new HBox(sp, remove);
        return footer;
    }

    public void open() {
        if (open) return;
        open = true;
        root.setVisible(true);
        root.setManaged(true);

        FadeTransition fade = new FadeTransition(Duration.millis(240), scrim);
        fade.setToValue(1);
        fade.play();

        TranslateTransition slide = new TranslateTransition(Duration.millis(320), drawer);
        slide.setToX(0);
        slide.play();
    }

    public void close() {
        if (!open) return;
        open = false;

        FadeTransition fade = new FadeTransition(Duration.millis(200), scrim);
        fade.setToValue(0);

        TranslateTransition slide = new TranslateTransition(Duration.millis(280), drawer);
        slide.setToX(DRAWER_WIDTH);
        slide.setOnFinished(e -> {
            root.setVisible(false);
            root.setManaged(false);
        });

        fade.play();
        slide.play();
    }
}
