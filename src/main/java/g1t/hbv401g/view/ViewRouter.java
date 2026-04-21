package g1t.hbv401g.view;

import g1t.hbv401g.view.search.SearchView;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;


public final class ViewRouter {

    public enum Page { HOME, SEARCH, USER, CHECKOUT }

    private static ViewRouter INSTANCE;
    public static ViewRouter get() { return INSTANCE; }
    public static void install(StackPane content, CartDrawerView cartDrawer) {
        INSTANCE = new ViewRouter(content, cartDrawer);
    }

    private final StackPane content;
    private final CartDrawerView cartDrawer;

    private ViewRouter(StackPane content, CartDrawerView cartDrawer) {
        this.content = content;
        this.cartDrawer = cartDrawer;
    }

    public void goTo(Page page) {
        Node node = switch (page) {
            case HOME     -> new HomeView().getRoot();
            case SEARCH   -> new SearchView().getRoot();
            case USER     -> new UserProfileView().getRoot();
            case CHECKOUT -> new CheckoutView().getRoot();
        };

        content.getChildren().setAll(node);

        FadeTransition ft = new FadeTransition(Duration.millis(280), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    public void openCart() { cartDrawer.open(); }
    public void closeCart() { cartDrawer.close(); }
}
