package g1t.hbv401g.view.search;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;


public final class SearchFilters {

    private static final double PRICE_FLOOR = 0;
    private static final double PRICE_CEIL  = 5000;

    private final Runnable onChange;

    private final Slider priceMin = new Slider(PRICE_FLOOR, PRICE_CEIL, PRICE_FLOOR);
    private final Slider priceMax = new Slider(PRICE_FLOOR, PRICE_CEIL, PRICE_CEIL);
    private final Label priceMinLbl = new Label("$0");
    private final Label priceMaxLbl = new Label("$5,000");

    private boolean includeFlights = true;
    private boolean includeHotels  = true;
    private boolean includeEvents  = true;

    private final VBox root;

    public SearchFilters(Runnable onChange) {
        this.onChange = onChange;
        this.root = buildRoot();
    }

    public Node getRoot() { return root; }

    // ---------- getters ----------

    public double priceMin() { return priceMin.getValue(); }
    public double priceMax() { return priceMax.getValue(); }
    public boolean includeFlights() { return includeFlights; }
    public boolean includeHotels()  { return includeHotels; }
    public boolean includeEvents()  { return includeEvents; }
    public boolean nothingIncluded() {
        return !includeFlights && !includeHotels && !includeEvents;
    }

    // ---------- build ----------

    private VBox buildRoot() {
        GridPane row = new GridPane();
        row.getStyleClass().add("filters-row");
        row.setHgap(32);
        ColumnConstraints a = new ColumnConstraints(); a.setPercentWidth(58);
        ColumnConstraints b = new ColumnConstraints(); b.setPercentWidth(42);
        row.getColumnConstraints().addAll(a, b);

        row.add(buildPriceBlock(),   0, 0);
        row.add(buildToggleBlock(),  1, 0);

        return new VBox(row);
    }

    // ---------- price range ----------

    private VBox buildPriceBlock() {
        VBox block = new VBox(10);

        Label label = new Label("PRICE RANGE  \u00B7  PER PERSON");
        label.getStyleClass().add("field-label");

        StackPane sliderStack = buildRangeSlider();

        HBox readout = new HBox(priceMinLbl, grow(), priceMaxLbl);
        readout.setAlignment(Pos.CENTER);
        priceMinLbl.getStyleClass().add("mono");
        priceMaxLbl.getStyleClass().add("mono");

        block.getChildren().setAll(label, sliderStack, readout);
        return block;
    }

    private StackPane buildRangeSlider() {
        priceMin.getStyleClass().add("price-slider");
        priceMax.getStyleClass().add("price-slider");
        priceMin.setMaxWidth(Double.MAX_VALUE);
        priceMax.setMaxWidth(Double.MAX_VALUE);

        Region track = new Region();
        track.getStyleClass().add("price-range-track");
        track.setMaxHeight(3); track.setPrefHeight(3); track.setMinHeight(3);
        track.setMaxWidth(Double.MAX_VALUE);

        Region fill = new Region();
        fill.setMaxHeight(3); fill.setPrefHeight(3); fill.setMinHeight(3);
        fill.getStyleClass().add("price-range-fill");
        fill.setManaged(false);

        StackPane stack = new StackPane(track, fill, priceMin, priceMax);
        stack.setMinHeight(22);
        stack.setPrefHeight(22);
        StackPane.setAlignment(track, Pos.CENTER);
        StackPane.setAlignment(priceMin, Pos.CENTER);
        StackPane.setAlignment(priceMax, Pos.CENTER);

        // tveir sliderar á sama stað
        // default mouse handling disablað svo hægt sé að routa click á sá sem er nær 
        priceMin.setMouseTransparent(true);
        priceMax.setMouseTransparent(true);
        Consumer<MouseEvent> dispatch = e -> {
            double w = stack.getWidth();
            if (w <= 0) return;
            double range = priceMin.getMax() - priceMin.getMin();
            double x = Math.max(0, Math.min(w, e.getX()));
            double v = priceMin.getMin() + x / w * range;
            boolean pickLow = Math.abs(v - priceMin.getValue()) <= Math.abs(v - priceMax.getValue());
            if (pickLow) priceMin.setValue(Math.min(v, priceMax.getValue()));
            else priceMax.setValue(Math.max(v, priceMin.getValue()));
        };
        stack.setOnMousePressed(dispatch::accept);
        stack.setOnMouseDragged(dispatch::accept);

        Runnable layoutFill = () -> {
            double w = stack.getWidth();
            if (w <= 0) return;
            double lo = priceMin.getValue(), hi = priceMax.getValue();
            double range = priceMin.getMax() - priceMin.getMin();
            double x1 = (lo - priceMin.getMin()) / range * w;
            double x2 = (hi - priceMin.getMin()) / range * w;
            fill.resizeRelocate(x1, (stack.getHeight() - 3) / 2, Math.max(0, x2 - x1), 3);
        };
        stack.widthProperty().addListener((o, ov, nv) -> layoutFill.run());
        stack.heightProperty().addListener((o, ov, nv) -> layoutFill.run());
        priceMin.valueProperty().addListener((o, ov, nv) -> layoutFill.run());
        priceMax.valueProperty().addListener((o, ov, nv) -> layoutFill.run());

        priceMin.valueProperty().addListener((o, ov, nv) -> {
            if (nv.doubleValue() > priceMax.getValue()) priceMin.setValue(priceMax.getValue());
            priceMinLbl.setText("$" + String.format("%,d", (int) priceMin.getValue()));
            onChange.run();
        });
        priceMax.valueProperty().addListener((o, ov, nv) -> {
            if (nv.doubleValue() < priceMin.getValue()) priceMax.setValue(priceMin.getValue());
            priceMaxLbl.setText("$" + String.format("%,d", (int) priceMax.getValue()));
            onChange.run();
        });

        return stack;
    }

    // ---------- include toggles ----------

    private VBox buildToggleBlock() {
        VBox block = new VBox(10);
        Label label = new Label("INCLUDE");
        label.getStyleClass().add("field-label");

        HBox flights = togglePill("Flights",   true, v -> { includeFlights = v; onChange.run(); });
        HBox hotels  = togglePill("Hotels",    true, v -> { includeHotels  = v; onChange.run(); });
        HBox events  = togglePill("Day-trips", true, v -> { includeEvents  = v; onChange.run(); });

        TilePane toggles = new TilePane(8, 8);
        toggles.getChildren().setAll(flights, hotels, events);

        block.getChildren().setAll(label, toggles);
        return block;
    }

    private HBox togglePill(String label, boolean initial, Consumer<Boolean> onToggle) {
        HBox pill = new HBox(10);
        pill.getStyleClass().add("toggle-pill");
        pill.setAlignment(Pos.CENTER_LEFT);

        Region dot = new Region();
        dot.getStyleClass().add("toggle-dot");

        Label txt = new Label(label);
        pill.getChildren().setAll(dot, txt);

        boolean[] state = { initial };
        if (state[0]) pill.getStyleClass().add("selected");

        pill.setOnMouseClicked(e -> {
            state[0] = !state[0];
            if (state[0]) pill.getStyleClass().add("selected");
            else pill.getStyleClass().remove("selected");
            onToggle.accept(state[0]);
        });
        return pill;
    }

    private static Region grow() {
        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        return r;
    }
}
