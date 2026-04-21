package g1t.hbv401g.view;

import g1t.hbv401g.view.util.Animations;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class HomeView {

    private final VBox root;

    public HomeView() {
        root = new VBox();
        root.setFillWidth(true);
        root.getChildren().setAll(buildHero(), buildRecommended());
    }

    public Node getRoot() {
        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return scroll;
    }

    private VBox buildHero() {
        VBox hero = new VBox(40);
        hero.setAlignment(Pos.CENTER);
        hero.setPadding(new Insets(140, 28, 120, 28));

        Label headline = new Label("Your journey awaits");
        headline.getStyleClass().addAll("display", "display-xl", "fs-80");

        Button cta = new Button("Search for trips  \u2192");
        cta.getStyleClass().add("hero-cta");
        cta.setOnAction(e -> ViewRouter.get().goTo(ViewRouter.Page.SEARCH));

        Animations.fadeInUp(headline, 0);
        Animations.fadeInUp(cta, 120);

        hero.getChildren().setAll(headline, cta);
        return hero;
    }

    private VBox buildRecommended() {
        VBox section = new VBox(32);
        section.setPadding(new Insets(20, 28, 140, 28));
        section.setMaxWidth(1240);
        VBox.setVgrow(section, Priority.NEVER);

        Label h2 = new Label("Recommended");
        h2.getStyleClass().addAll("display", "display-md", "fs-44");

        Label empty = new Label("Nothing to recommend yet. Come back soon.");
        empty.getStyleClass().add("muted");

        VBox wrap = new VBox(16, h2, empty);
        wrap.setMaxWidth(1240);
        section.getChildren().setAll(wrap);

        StackPane center = new StackPane(section);
        center.setAlignment(Pos.TOP_CENTER);
        return new VBox(center);
    }
}
