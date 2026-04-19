package g1t.hbv401g.view.components;

import g1t.hbv401g.model.Trip;
import g1t.hbv401g.view.state.AppState;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;


public final class TripTitleEditor {

    private TripTitleEditor() {}

    public static StackPane create(Trip trip, String titleStyleClass) {
        StackPane host = new StackPane();
        host.setAlignment(Pos.CENTER_LEFT);
        host.getChildren().setAll(display(trip, host, titleStyleClass));
        return host;
    }

    private static HBox display(Trip trip, StackPane host, String titleStyleClass) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label(trip.getName());
        title.getStyleClass().add(titleStyleClass);

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Button edit = new Button("Rename");
        edit.getStyleClass().addAll("edit-link", "fs-11");
        edit.setOnAction(e -> host.getChildren().setAll(editor(trip, host, titleStyleClass)));

        row.getChildren().setAll(title, sp, edit);
        return row;
    }

    private static HBox editor(Trip trip, StackPane host, String titleStyleClass) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);

        TextField input = new TextField(trip.getName());
        input.getStyleClass().add("info-input");
        input.setPromptText(trip.getDefaultName());
        HBox.setHgrow(input, Priority.ALWAYS);
        input.setMaxWidth(Double.MAX_VALUE);

        Runnable commit = () -> {
            String v = input.getText() == null ? "" : input.getText().trim();
            if (v.isEmpty() || v.equals(trip.getDefaultName())) {
                trip.setCustomName(null);
            } else {
                trip.setCustomName(v);
            }
            AppState.get().notifyListeners();
        };
        input.setOnAction(e -> commit.run());
        input.focusedProperty().addListener((o, was, isNow) -> { if (!isNow) commit.run(); });

        row.getChildren().setAll(input);
        Platform.runLater(() -> { input.requestFocus(); input.selectAll(); });
        return row;
    }
}
