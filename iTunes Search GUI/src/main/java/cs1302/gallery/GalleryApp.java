package cs1302.gallery;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Represents an iTunes GalleryApp!.
 */
public class GalleryApp extends Application {

    /**
     * Creates and runs a iTunes GalleryApp application.
     *{@inheritdoc}
     */
    @Override
    public void start(Stage stage) {
        HBox pane = new HBox();
        pane.getChildren().add(new UpdateImages());
        Scene scene = new Scene(pane);
        stage.setMaxWidth(615);
        stage.setMaxHeight(480);
        stage.setMinWidth(602);
        stage.setMinHeight(480);
        stage.setTitle("GalleryApp!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    } // start

} // GalleryApp
