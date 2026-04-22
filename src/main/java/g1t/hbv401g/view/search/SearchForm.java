package g1t.hbv401g.view.search;

import g1t.hbv401g.controller.SearchController;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;


public final class SearchForm {

    private static final int MIN_TRAVELLERS = 1;
    private static final int MAX_TRAVELLERS = 12;

    private final Runnable onChange;

    private final ComboBox<String> fromBox = new ComboBox<>();
    private final ComboBox<String> toBox = new ComboBox<>();
    private final DatePicker departDate = new DatePicker(LocalDate.now());
    private final DatePicker returnDate = new DatePicker(LocalDate.now().plusDays(7));
    private final Label travCount = new Label("1");
    private int travellers = 1;

    private final GridPane root;

    public SearchForm(SearchController controller, Runnable onChange) {
        this.onChange = onChange;
        this.root = buildRoot(controller);
    }

    public Node getRoot() { return root; }

    public String from() { return fromBox.getValue(); }
    public String to()   { return toBox.getValue(); }
    public LocalDate depart() { return departDate.getValue(); }
    public LocalDate returnOn() { return returnDate.getValue(); }
    public int travellers() { return Math.max(1, travellers); }

    // ---------- build ----------

    private GridPane buildRoot(SearchController controller) {
        GridPane form = new GridPane();
        form.getStyleClass().add("search-form");
        form.getColumnConstraints().addAll(
                col(24), col(24), col(18), col(18), col(16));

        wirePlaces(controller.getPlaces());
        wireDates();

        form.add(field("FROM", fromBox, false),       0, 0);
        form.add(field("TO", toBox, false),           1, 0);
        form.add(field("DEPART", departDate, false),  2, 0);
        form.add(field("RETURN", returnDate, false),  3, 0);
        form.add(field("TRAVELLERS", stepper(), true),4, 0);

        return form;
    }

    private void wirePlaces(List<String> places) {
        fromBox.getItems().addAll(places);
        toBox.getItems().addAll(places);
        if (places.size() >= 2) {
            fromBox.getSelectionModel().select(places.get(0));
            toBox.getSelectionModel().select(places.get(1));
        } else if (!places.isEmpty()) {
            fromBox.getSelectionModel().select(places.get(0));
            toBox.getSelectionModel().select(places.get(0));
        }
        fromBox.getStyleClass().add("field-combo");
        toBox.getStyleClass().add("field-combo");
        fromBox.setMaxWidth(Double.MAX_VALUE);
        toBox.setMaxWidth(Double.MAX_VALUE);
        fromBox.valueProperty().addListener((o, a, b) -> onChange.run());
        toBox.valueProperty().addListener((o, a, b) -> onChange.run());
    }

    private void wireDates() {
        departDate.getStyleClass().add("field-combo");
        returnDate.getStyleClass().add("field-combo");
        departDate.setPromptText("Depart");
        returnDate.setPromptText("Return");
        departDate.valueProperty().addListener((o, a, b) -> onChange.run());
        returnDate.valueProperty().addListener((o, a, b) -> onChange.run());
    }

    private HBox stepper() {
        Button minus = new Button("\u2212");
        Button plus  = new Button("+");
        minus.getStyleClass().add("stepper-btn");
        plus.getStyleClass().add("stepper-btn");
        travCount.getStyleClass().add("stepper-count");

        minus.setOnAction(e -> adjustTravellers(-1));
        plus.setOnAction(e -> adjustTravellers(+1));

        HBox box = new HBox(minus, travCount, plus);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private void adjustTravellers(int delta) {
        int next = travellers + delta;
        if (next < MIN_TRAVELLERS || next > MAX_TRAVELLERS) return;
        travellers = next;
        travCount.setText(String.valueOf(travellers));
        onChange.run();
    }

    private static VBox field(String labelText, Node control, boolean last) {
        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("field-label");
        VBox f = new VBox(4, lbl, control);
        f.getStyleClass().add("field");
        if (last) f.getStyleClass().add("field-last");
        return f;
    }

    private static ColumnConstraints col(double percent) {
        ColumnConstraints c = new ColumnConstraints();
        c.setPercentWidth(percent);
        return c;
    }
}
