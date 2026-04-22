package g1t.hbv401g.view.search;

import g1t.hbv401g.model.HotelSelection;
import g1t.hbv401g.view.util.Formats;
import is.hi.H1.model.Room;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.function.Function;


public final class HotelCard {

    private HotelCard() {}

    public static Node create(HotelSelection sel, boolean initiallySelected,
                              Function<HotelSelection, Boolean> onToggle) {
        VBox card = new VBox(10);
        card.getStyleClass().add("event-card");
        card.setPrefWidth(280);
        card.setMinWidth(260);
        card.setMaxWidth(360);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        card.getChildren().setAll(
                topRow(sel),
                title(sel),
                spacer,
                meta(sel),
                priceRow(sel));

        if (initiallySelected) card.getStyleClass().add("selected");

        card.setOnMouseClicked(e -> {
            boolean nowSelected = onToggle.apply(sel);
            if (nowSelected) {
                if (!card.getStyleClass().contains("selected")) card.getStyleClass().add("selected");
            } else {
                card.getStyleClass().remove("selected");
            }
        });
        return card;
    }

    private static HBox topRow(HotelSelection sel) {
        Label check = new Label("HTL");
        check.getStyleClass().addAll("check-circle", "check-circle-sm");

        Label rating = new Label(String.format("%.1f\u2605", sel.getHotel().getAvgRating()));
        rating.getStyleClass().add("eyebrow");

        HBox row = new HBox(10, check, grow(), rating);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private static Label title(HotelSelection sel) {
        Label title = new Label(sel.getHotel().getName());
        title.getStyleClass().add("event-card-title");
        return title;
    }

    private static Label meta(HotelSelection sel) {
        Room[] rooms = sel.getRooms();
        String roomLabel = rooms.length + (rooms.length == 1 ? " room" : " rooms");
        Label meta = new Label(roomLabel
                + "  \u00B7  " + sel.getGuestCapacity() + " guests"
                + "  \u00B7  " + sel.getNights() + (sel.getNights() == 1 ? " night" : " nights"));
        meta.getStyleClass().addAll("mono", "fs-11");
        return meta;
    }

    private static HBox priceRow(HotelSelection sel) {
        Label label = new Label("STAY TOTAL");
        label.getStyleClass().add("eyebrow");

        Label price = new Label(Formats.moneyInt(sel.getTotalCost()));
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
