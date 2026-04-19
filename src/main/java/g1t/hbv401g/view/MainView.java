package g1t.hbv401g.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainView extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/g1t/hbv401g/fxml/main-view.fxml"));
        StackPane rootStack = loader.load();
        BorderPane frame = (BorderPane) loader.getNamespace().get("frame");

        StackPane content = new StackPane();
        frame.setCenter(content);

        HeaderView header = new HeaderView();
        frame.setTop(header.getRoot());

        CartDrawerView cartDrawer = new CartDrawerView();
        rootStack.getChildren().add(cartDrawer.getRoot());

        ViewRouter.install(content, cartDrawer);
        ViewRouter.get().goTo(ViewRouter.Page.HOME);

        Scene scene = new Scene(rootStack, 1280, 820);
        scene.getStylesheets().add(getClass().getResource("/g1t/hbv401g/css/style.css").toExternalForm());

        stage.setTitle("Tripster");
        stage.setScene(scene);
        stage.setMinWidth(960);
        stage.setMinHeight(680);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
