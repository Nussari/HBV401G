package g1t.hbv401g.view.components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;


public final class InfoRow {

    private InfoRow() {}

    public static VBox text(String label, String value, String editLabel, Consumer<String> onSave) {
        return build(label, value, editLabel, false, onSave);
    }

    public static VBox password(String label, String editLabel, Consumer<String> onSave) {
        return build(label, "\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022",
                     editLabel, true, onSave);
    }

    private static VBox build(String label, String displayValue, String editLabel,
                              boolean secret, Consumer<String> onSave) {
        VBox wrap = new VBox(8);

        Label heading = new Label(label);
        heading.getStyleClass().add("eyebrow");

        StackPane host = new StackPane();
        host.getChildren().setAll(displayRow(displayValue, editLabel, secret, host, onSave));

        wrap.getChildren().setAll(heading, host);
        return wrap;
    }

    private static HBox displayRow(String value, String editLabel, boolean secret,
                                   StackPane host, Consumer<String> onSave) {
        HBox row = new HBox(10);
        row.getStyleClass().add("info-value");
        row.setAlignment(Pos.CENTER_LEFT);

        Label text = new Label(value == null || value.isEmpty() ? "(not set)" : value);
        if (secret) text.getStyleClass().add("masked-password");

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Button edit = new Button(editLabel);
        edit.getStyleClass().add("edit-link");
        edit.setOnAction(e -> host.getChildren().setAll(editorRow(value, secret, host, onSave)));

        row.getChildren().setAll(text, sp, edit);
        return row;
    }

    private static HBox editorRow(String value, boolean secret, StackPane host, Consumer<String> onSave) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);

        TextField input = secret ? new PasswordField() : new TextField(value == null ? "" : value);
        input.getStyleClass().add("info-input");
        if (secret) input.setPromptText("Enter new password");
        HBox.setHgrow(input, Priority.ALWAYS);
        input.setMaxWidth(Double.MAX_VALUE);

        Runnable save = () -> {
            String raw = input.getText() == null ? "" : input.getText();
            onSave.accept(secret ? raw : raw.trim());
        };
        input.setOnAction(e -> save.run());
        input.focusedProperty().addListener((o, was, isNow) -> { if (!isNow) save.run(); });

        row.getChildren().setAll(input);
        javafx.application.Platform.runLater(() -> {
            input.requestFocus();
            if (!secret) ((TextField) input).selectAll();
        });
        return row;
    }
}
