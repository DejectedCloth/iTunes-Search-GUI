package cs1302.gallery;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.layout.TilePane;
import java.net.URLEncoder;
import java.net.URL;
import java.io.InputStreamReader;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * this class represents a {@code UpdateImages} object which
 * is an extension of the {@code VBox} class.
 */
public class UpdateImages extends VBox {

    MenuBar menuBar;
    final Menu file;
    final Menu help;
    MenuItem exit;
    MenuItem about;
    HBox menu;
    HBox updateBar;
    Button pauseButton;
    Button updateButton;
    TextField searchField;
    Text text;
    TilePane tile;
    LoadImages[] image;
    String[] links;
    boolean active;
    boolean paused;
    ProgressBar progressBar;
    int progress;

    /**
     * creates a {@code UpdateImages} object.
     */
    public UpdateImages() {
        super();
        file = new Menu("File");
        help = new Menu("Help");
        exit = new MenuItem("Exit");
        exit.setOnAction(e -> Platform.exit());
        about = new MenuItem("About");
        about.setOnAction(e -> aboutMe());
        menuBar = new MenuBar();
        file.getItems().add(exit);
        help.getItems().add(about);
        menuBar.getMenus().addAll(file, help);
        updateBar = new HBox(10);
        pauseButton = new Button("Pause");
        updateButton = new Button("Update Images");
        searchField = new TextField("music");
        text = new Text("Search Query: ");
        updateBar.getChildren().setAll(pauseButton, text, searchField, updateButton);
        progressBar = new ProgressBar();
        tile = new TilePane();
        tile.setPrefColumns(5);
        image = new LoadImages[20];
        for (int i = 0; i < 20; i++) {
            image[i] = new LoadImages();
            tile.getChildren().add(image[i]);
        } //for
        this.getChildren().addAll(menuBar, updateBar, tile, progressBar);
        updateButton.setOnAction(e -> updateImages());
        pauseButton.setOnAction(a -> replaceImages());
        updateButton.fire();
        active = false;
        pauseButton.fire();
    } //UpdateImages

    /**
     * updates all images being dislayed to the user once the user
     * inputs a search and clicks the update button.
     */
    private void updateImages() {
        Runnable z = () -> {
            try {
                if (active == true) {
                    pauseButton.fire();
                    paused = true;
                } //if
                String searched = URLEncoder.encode(searchField.getText(), "UTF-8");
                String sURL = "https://itunes.apple.com/search?term=" + searched + "&limit=200&media=music";
                URL url = new URL(sURL);
                InputStreamReader reader = new InputStreamReader(url.openStream());
                JsonElement je = JsonParser.parseReader(reader);
                JsonObject root = je.getAsJsonObject();
                JsonArray results = root.getAsJsonArray("results");
                links = new String[results.size()];
                int numResults = results.size();
                int position = 0;
                setProgress(0);
                for (int i = 0; i < results.size(); i++) {
                    JsonObject result = results.get(i).getAsJsonObject();
                    JsonElement artworkUrl100 = result.get("artworkUrl100");
                    setProgress(1.0 * i / results.size());
                    if (!isPresent(artworkUrl100.getAsString())) {
                        links[position] = artworkUrl100.getAsString();
                        position++;
                    } //if
                    Thread.sleep(1);
                } //for
                setProgress(1);
                if (numLinks() < 21) {
                    throw new Exception();
                }
                for (int i = 0; i < 20; i++) {
                    Platform.runLater(createRunnable(i, i));
                } //for
                if (active == false && paused == true) {
                    pauseButton.fire();
                    paused = false;
                } //if
            } catch (Exception e) {
                System.out.println("error");
                Platform.runLater(() -> informationAlert());
            } //try-catch
        };
        Thread b = new Thread(z);
        b.setDaemon(true);
        b.start();
    } //updateImages

    /**
     * replaces a random displayed image with another images stored
     * in the links String array instance variable every two seconds.
     */
    public void replaceImages() {
        if (active) {
            active = false;
        } else {
            active = true;
        } //if
        Runnable r = () -> {
            int i = 20;
            while (active) {
                int random = (int)(Math.random() * 20);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    active = false;
                }
                if (links.length > 21 && (links[20] == null || i == links.length)) {
                    i = 0;
                } //if
                if (links.length > 21) {
                    Platform.runLater(createRunnable(random, i));
                    i++;
                } //if
            } //while
        };
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.start();
    } //replaceImages

    /**
     * determines if the link provided as the perameter is already
     * pressent in the links String array instance variable.
     *
     * @param checkingLink link to be checked if already present in
     * links String array instance variable
     * @return true if link is present in the links array already and
     * false otherwise.
     */
    private boolean isPresent (String checkingLink) {
        for (String link : links) {
            if (link == null ) {
                return false;
            } else if (link.equalsIgnoreCase(checkingLink)) {
                return true;
            } //if
        } //for
        return false;
    } //isPresent

    /**
     * counts the number of links present in the links String
     * array instance variable.
     *
     * @return the number of links present in the links Stirng array
     */
    private int numLinks() {
        int numLinks = 0;
        for (String link : links) {
            if (link != null) {
                numLinks++;
            } else {
                return numLinks;
            } //if
        } //for
        return numLinks;
    } //numLinks

    /**
     * updates the images that are being displayed to the user that
     * is being downloaded from a seperate thread from the main method.
     *
     * @param i the index of the loadimages object in the image array
     * that will be updated from the image array
     * @param j the index of the link in the links array  that will be used to
     * update the LoadImages object
     * @return Runnable object to use with runLater method in the
     * {@code Platform} class
     */
    private Runnable createRunnable(int i, int j) {
        Runnable run = () -> image[i].loadImage(links[j]);
        return run;
    } //createRunnable

    /**
     * updates the progress of the progress bar that is running in
     * a seperate thread from main method.
     *
     * @param progress the progress of the download of the images
     * from the user search query. Must be a double between 0 and 1
     */
    private void setProgress(final double progress) {
        Platform.runLater(() -> progressBar.setProgress(progress));
    } // setProgress

    /**
     * creates and displays an alert the user that there is less than 21 unique
     * results to showcase from the search they inputted.
     */
    public void informationAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Oh No! :(");
        alert.setContentText("Less than 21 unique results to showcase.");
        alert.setHeaderText("Try another search.");
        alert.setResizable(true);
        alert.show();
    } //informationAlert

    /**
     * creates and displays an alert that cointains a photo
     * of me, my email, and the version number.
     */
    public void aboutMe() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Christopher");
        alert.setHeaderText("That's me!! --->");
        LoadImages image = new LoadImages();
        image.loadImage("file:resources/Tea-Time-3.jpg");
        alert.setGraphic(image);
        alert.setContentText("Name: Christopher Young\nEmail:Christopher.Young@uga.edu\n" +
            "Version: 1.2");
        alert.setResizable(true);
        alert.show();
    } //aboutMe

} //UpdateImages
