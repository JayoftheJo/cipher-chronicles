package RoomCompass;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class Compass extends Pane {

    @FXML
    private Polygon up;

    @FXML
    private Polygon down;

    @FXML
    private Polygon left;

    @FXML
    private Polygon right;

    @FXML
    private Rectangle background;

    @FXML
    private Rectangle center;

    private final RoomCompass model;

    public Compass(RoomCompass model) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("compass.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.model = model;

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        update();
    }

    public void update() {
        model.update();

        up.setFill(Color.BLACK);
        up.setStroke(Color.BLACK);
        down.setFill(Color.BLACK);
        down.setStroke(Color.BLACK);
        left.setFill(Color.BLACK);
        left.setStroke(Color.BLACK);
        right.setFill(Color.BLACK);
        right.setStroke(Color.BLACK);

        if (model.getUp()) {
            up.setFill(Color.WHITE);
            up.setStroke(Color.WHITE);
        }
        if (model.getDown()) {
            down.setFill(Color.WHITE);
            down.setStroke(Color.WHITE);
        }
        if (model.getRight()) {
            right.setFill(Color.WHITE);
            right.setStroke(Color.WHITE);
        }
        if (model.getLeft()) {
            left.setFill(Color.WHITE);
            left.setStroke(Color.WHITE);
        }
    }

}
