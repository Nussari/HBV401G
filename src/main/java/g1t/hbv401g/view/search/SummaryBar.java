package g1t.hbv401g.view.search;

import g1t.hbv401g.view.util.Formats;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

// pillan niðrí hægra horni í search
public final class SummaryBar {

    private final Label count = new Label("0 ITEMS SELECTED");
    private final Label total = new Label("$0");
    private final HBox root;

    public SummaryBar(Runnable onAddToCart) {
        count.getStyleClass().add("eyebrow");

        Label dot = new Label(" \u00B7 ");
        dot.getStyleClass().add("muted");

        total.getStyleClass().add("summary-total");

        Button addBtn = new Button("Add to cart  \u2192");
        addBtn.getStyleClass().addAll("btn", "btn-primary");
        addBtn.setOnAction(e -> onAddToCart.run());

        root = new HBox(14, count, dot, total, addBtn);
        root.getStyleClass().add("summary-bar");
        root.setAlignment(Pos.CENTER_LEFT);
        root.setMaxWidth(Region.USE_PREF_SIZE);
        root.setMaxHeight(Region.USE_PREF_SIZE);

        StackPane.setAlignment(root, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(root, new Insets(0, 24, 24, 24));
    }

    public HBox getRoot() { return root; }

    public void refresh(int selectedCount, double selectedTotal) {
        count.setText(selectedCount + (selectedCount == 1 ? " ITEM" : " ITEMS") + " SELECTED");
        total.setText(Formats.moneyInt(selectedTotal));
    }
}
