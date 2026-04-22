package g1t.hbv401g.view.components;

import g1t.hbv401g.model.Trip;
import g1t.hbv401g.view.state.AppState;
import g1t.hbv401g.view.util.Formats;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;


public final class ComponentRow {

    private ComponentRow() {}

    public static HBox readOnly(String kindCode, String label, double price) {
        return build(kindCode, label, price, null, 14, 42);
    }

    public static HBox removable(String kindCode, String label, double price,
                                 Trip trip, Object component) {
        return build(kindCode, label, price,
                () -> AppState.get().removeComponentFromTrip(trip, component), 12, 40);
    }

    public static HBox cancellable(String kindCode, String label, double price, Runnable onCancel) {
        return build(kindCode, label, price, onCancel, 12, 40);
    }

    private static HBox build(String kindCode, String label, double price,
                              Runnable onAction,
                              int spacing, int kindSize) {
        HBox row = new HBox(spacing);
        row.setAlignment(Pos.CENTER_LEFT);

        Label kind = new Label(kindCode);
        kind.getStyleClass().add("tx-cart-kind");
        if (kindSize != 40) {
            kind.setMinWidth(kindSize); kind.setMaxWidth(kindSize);
            kind.setMinHeight(kindSize); kind.setMaxHeight(kindSize);
        }

        Label lbl = new Label(label);
        lbl.getStyleClass().addAll("cart-component-label");
        HBox.setHgrow(lbl, Priority.ALWAYS);
        lbl.setMaxWidth(Double.MAX_VALUE);

        Label priceLbl = new Label(Formats.money(price));
        priceLbl.getStyleClass().addAll("mono", "fs-11");

        row.getChildren().setAll(kind, lbl, priceLbl);

        if (onAction != null) {
            Button drop = new Button("\u2715");
            drop.getStyleClass().addAll("tx-cart-close", "fs-11");
            drop.setOnAction(e -> onAction.run());
            row.getChildren().add(drop);
        }
        return row;
    }
}
