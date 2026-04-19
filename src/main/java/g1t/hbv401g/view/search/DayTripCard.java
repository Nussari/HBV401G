package g1t.hbv401g.view.search;

import g1t.hbv401g.model.DayTrip;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.function.Function;


public final class DayTripCard {

    private DayTripCard() {}

    public static Node create(DayTrip t, boolean initiallySelected, Function<DayTrip, Boolean> onToggle) {
        VBox card = new VBox(10);
        card.getStyleClass().add("event-card");
        card.setPrefWidth(260);
        card.setMinWidth(240);
        card.setMaxWidth(320);

        card.getChildren().setAll(
                topRow(t),
                title(t),
                meta(t),
                divider(),
                priceRow(t));

        if (initiallySelected) card.getStyleClass().add("selected");

        card.setOnMouseClicked(e -> {
            boolean nowSelected = onToggle.apply(t);
            if (nowSelected) {
                if (!card.getStyleClass().contains("selected")) card.getStyleClass().add("selected");
            } else {
                card.getStyleClass().remove("selected");
            }
        });
        return card;
    }

    private static HBox topRow(DayTrip t) {
        Label check = new Label("DAY");
        check.getStyleClass().addAll("check-circle", "check-circle-sm");

        Label spaces = new Label((t.getTotalSpace() - t.getBookedSpaces()) + " LEFT");
        spaces.getStyleClass().add("eyebrow");

        HBox row = new HBox(10, check, grow(), spaces);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private static Label title(DayTrip t) {
        Label title = new Label(t.getName());
        title.getStyleClass().add("event-card-title");
        return title;
    }

    private static Label meta(DayTrip t) {
        Label meta = new Label(t.getCategory() + "  \u00B7  " + t.getDuration() + "h  \u00B7  " + t.getDate());
        meta.getStyleClass().addAll("mono", "fs-11");
        return meta;
    }

    private static Region divider() {
        Region sep = new Region();
        sep.getStyleClass().add("divider-h");
        VBox.setMargin(sep, new Insets(4, 0, 4, 0));
        return sep;
    }

    private static HBox priceRow(DayTrip t) {
        Label label = new Label("FROM");
        label.getStyleClass().add("eyebrow");

        Label price = new Label("$" + String.format("%,d", t.getPrice()));
        price.getStyleClass().addAll("price-tag", "fs-20");

        HBox row = new HBox(label, grow(), price);
        row.setAlignment(Pos.BASELINE_LEFT);
        return row;
    }

    private static Region grow() {
        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        return r;
    }
}
