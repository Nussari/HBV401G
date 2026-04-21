package g1t.hbv401g.view.search;

import g1t.hbv401g.controller.SearchController;
import g1t.teamD.model.DayTrip;
import g1t.hbv401g.model.Flight;
import g1t.hbv401g.view.ViewRouter;
import g1t.hbv401g.view.state.AppState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public final class SearchView {

    private final SearchController controller = new SearchController();

    private final SearchForm form;
    private final SearchFilters filters;
    private final SearchResults results;
    private final SummaryBar summary;

    private final VBox root = new VBox();

    public SearchView() {
        form     = new SearchForm(controller, this::runSearch);
        filters  = new SearchFilters(this::runSearch);
        results  = new SearchResults(controller, this::refreshSummary);
        summary  = new SummaryBar(this::addSelectionToCart);

        root.setFillWidth(true);
        build();
        runSearch();
    }

    public Node getRoot() {
        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        StackPane overlay = new StackPane(scroll, summary.getRoot());
        overlay.setPickOnBounds(false);
        return overlay;
    }

    // ---------- layout ----------

    private void build() {
        Label title = new Label("Plan your trip");
        title.getStyleClass().addAll("display", "display-lg", "fs-56");

        VBox head = new VBox(36, title, form.getRoot(), filters.getRoot());
        head.setPadding(new Insets(50, 28, 24, 28));
        head.setMaxWidth(1240);

        StackPane headCenter = new StackPane(head);
        headCenter.setAlignment(Pos.TOP_CENTER);

        VBox resultsWrap = new VBox(results.getRoot());
        resultsWrap.setPadding(new Insets(40, 28, 100, 28));
        resultsWrap.setMaxWidth(1240);
        StackPane resultsCenter = new StackPane(resultsWrap);
        resultsCenter.setAlignment(Pos.TOP_CENTER);

        root.getChildren().setAll(headCenter, resultsCenter);
    }

    // ---------- flow ----------

    private void runSearch() {
        results.render(form, filters);
    }

    private void refreshSummary() {
        int count = 0;
        double total = 0;
        if (results.selectedOut() != null) {
            count++;
            total += results.selectedOut().getPrice();
        }
        if (results.selectedReturn() != null) {
            count++;
            total += results.selectedReturn().getPrice();
        }
        for (DayTrip dt : results.selectedDayTrips()) {
            count++;
            total += dt.getPrice();
        }
        summary.refresh(count, total);
    }

    private void addSelectionToCart() {
        if (!results.hasAnySelection()) return;

        if (!AppState.get().isLoggedIn()) {
            ViewRouter.get().goTo(ViewRouter.Page.USER);
            return;
        }

        AppState.get().addSelectionToCart(results.selectedDayTrips());

        results.clearSelection();
        ViewRouter.get().openCart();
    }
}
