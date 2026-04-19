package g1t.hbv401g.view;

import g1t.hbv401g.view.state.AppState;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;


public class HeaderView {

    private final HBox root;
    private final StackPane cartButtonStack = new StackPane();
    private final Label cartBadge = new Label();
    private final HBox userSlot = new HBox();

    public HeaderView() {
        root = new HBox(16);
        root.getStyleClass().add("tx-header");
        root.setAlignment(Pos.CENTER_LEFT);

        HBox brand = buildBrand();
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        Button cartBtn = buildCartButton();

        userSlot.setAlignment(Pos.CENTER_RIGHT);
        userSlot.setSpacing(6);

        root.getChildren().setAll(brand, spacer, cartBtn, userSlot);

        AppState.get().subscribe(this::refresh);
        refresh();
    }

    public HBox getRoot() { return root; }

    private HBox buildBrand() {
        Label brandText = new Label("Tripster");
        brandText.getStyleClass().add("tx-brand");

        HBox brand = new HBox(10, brandText);
        brand.getStyleClass().add("cursor-hand");
        brand.setAlignment(Pos.CENTER_LEFT);
        brand.setOnMouseClicked(e -> ViewRouter.get().goTo(ViewRouter.Page.HOME));
        return brand;
    }

    private Button buildCartButton() {
        cartBadge.getStyleClass().add("tx-badge");
        StackPane.setAlignment(cartBadge, Pos.TOP_RIGHT);
        cartBadge.setTranslateX(8);
        cartBadge.setTranslateY(-6);

        Label cartGlyph = new Label("\uD83D\uDED2");
        cartGlyph.getStyleClass().add("fs-16");

        cartButtonStack.getChildren().setAll(cartGlyph, cartBadge);

        Button btn = new Button();
        btn.setGraphic(cartButtonStack);
        btn.getStyleClass().add("tx-icon-btn");
        btn.setOnAction(e -> ViewRouter.get().openCart());
        return btn;
    }

    private void refresh() {
        AppState state = AppState.get();

        int cartSize = state.getCart().size();
        cartBadge.setText(String.valueOf(cartSize));
        cartBadge.setVisible(cartSize > 0);
        cartBadge.setManaged(cartSize > 0);

        userSlot.getChildren().clear();
        if (state.isLoggedIn()) {
            HBox chip = new HBox(10);
            chip.getStyleClass().add("tx-user-chip");
            chip.setAlignment(Pos.CENTER_LEFT);

            String username = state.getCurrentUser().getUsername();
            if (username == null) username = "";

            Label name = new Label(username);
            name.getStyleClass().addAll("tx-brand", "fs-13", "fw-normal", "text-base");

            chip.getChildren().setAll(name);
            chip.setOnMouseClicked(e -> ViewRouter.get().goTo(ViewRouter.Page.USER));
            userSlot.getChildren().add(chip);
        } else {
            Button login = new Button("Log in");
            login.getStyleClass().add("tx-login-btn");
            login.setOnAction(e -> ViewRouter.get().goTo(ViewRouter.Page.USER));
            userSlot.getChildren().add(login);
        }
    }
}
