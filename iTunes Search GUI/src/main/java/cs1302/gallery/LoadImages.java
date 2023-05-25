package cs1302.gallery;

import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import com.google.gson.JsonElement;

/**
 * this class represents a {@code LoadImage} object and
 * is an extension of the {@code VBox} class.
 */
public class LoadImages extends VBox {

    ImageView imgView;
    Image img;

    /**
     * creats a {@code LoadImages} object.
     */
    public LoadImages() {
        super();
        imgView = new ImageView();
        imgView.setPreserveRatio(false);
        String defURL = "https://i.pinimg.com/736x/02/6f/38/026f38534e419b425926189f39fa0d01.jpg";
        img = new Image(defURL);
        this.getChildren().add(imgView);
        imgView.setImage(img);
        imgView.setFitHeight(95);
        imgView.setFitWidth(120);
        this.setMaxHeight(10);
        this.setMaxWidth(10);
    } //LoadImages

    /**
     * updates the image being stored in the {@code ImageView} object.
     *
     * @param image the string for the link or file location of the image that will
     * be stored in the {@code ImageView} object
     */
    public void loadImage(String image) {
        Image i = new Image(image);
        this.imgView.setImage(i);
    } //loadImage

} //LoadImages
