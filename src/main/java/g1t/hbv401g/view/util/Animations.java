package g1t.hbv401g.view.util;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

// wowie kul animations B)
public final class Animations {

    private static final Duration DEFAULT = Duration.millis(500);
    private static final int STAGGER_MS = 60;

    private Animations() {}

    public static void fadeInUp(Node node, int delayMs) {
        node.setOpacity(0);
        node.setTranslateY(10);

        FadeTransition fade = new FadeTransition(DEFAULT, node);
        fade.setDelay(Duration.millis(delayMs));
        fade.setToValue(1);

        TranslateTransition rise = new TranslateTransition(DEFAULT, node);
        rise.setDelay(Duration.millis(delayMs));
        rise.setToY(0);

        fade.play();
        rise.play();
    }

    public static void staggerIn(Pane parent) {
        int i = 0;
        for (Node child : parent.getChildren()) {
            fadeInUp(child, STAGGER_MS * i);
            i++;
        }
    }
}
