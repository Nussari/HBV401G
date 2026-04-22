package g1t.hbv401g.view.search;

import g1t.teamF.model.Flight;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.function.Function;


public final class FlightRow {

    private FlightRow() {}

    public static Node create(Flight f, boolean outbound, Function<Flight, Boolean> onToggle) {
        HBox row = new HBox(22);
        row.getStyleClass().add("result-item");
        row.setAlignment(Pos.CENTER_LEFT);

        row.getChildren().setAll(
                checkBadge(outbound),
                airlineBlock(f),
                time(f.getDepartureTime().toString()),
                arrow(),
                time(f.getArrivalTime().toString()),
                routeBlock(f),
                grow(),
                price(f));

        row.setOnMouseClicked(e -> {
            boolean nowSelected = onToggle.apply(f);

            Node parent = row.getParent();
            if (parent instanceof javafx.scene.layout.Pane p) {
                for (Node sibling : p.getChildren()) {
                    if (sibling instanceof HBox) sibling.getStyleClass().remove("selected");
                }
            }
            if (nowSelected) row.getStyleClass().add("selected");
        });

        Tooltip.install(row, new Tooltip(
                f.getDepartureAirport().getPlace() + " \u2192 " + f.getArrivalAirport().getPlace()));
        return row;
    }

    private static Label checkBadge(boolean outbound) {
        Label l = new Label(outbound ? "OUT" : "RET");
        l.getStyleClass().add("check-circle");
        return l;
    }

    private static VBox airlineBlock(Flight f) {
        Label name = new Label(f.getFlightNumber());
        name.getStyleClass().addAll("fs-13", "bold", "text-base");
        Label id = new Label(""+f.getFlightID());
        id.getStyleClass().addAll("mono", "fs-11");
        return new VBox(2, name, id);
    }

    private static Label time(String text) {
        Label l = new Label(text);
        l.getStyleClass().add("flight-time");
        return l;
    }

    private static Label arrow() {
        Label a = new Label("\u2192");
        a.getStyleClass().add("muted");
        return a;
    }

    private static VBox routeBlock(Flight f) {
        Label route = new Label(f.getDepartureAirport().getCode() + "  \u00B7  " + f.getArrivalAirport().getCode());
        route.getStyleClass().addAll("mono", "fs-11");
        Label dur = new Label(f.getDuration().toString());
        dur.getStyleClass().addAll("mono", "fs-11");
        return new VBox(2, route, dur);
    }

    private static Label price(Flight f) {
        Label p = new Label("$" + String.format("%,d", (int) f.getPrice()));
        p.getStyleClass().add("price-tag");
        return p;
    }

    private static Region grow() {
        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        return r;
    }
}
