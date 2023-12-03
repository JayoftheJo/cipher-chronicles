package views;

import RoomCompass.Compass;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CompassView {

    private Compass compass;

    public CompassView(Compass compass) {
        final Stage display = new Stage();
        Scene compassBox = new Scene(compass, 300, 300);
        display.setScene(compassBox);
        display.show();
    }

    public void update() {
        compass.update();
    }

}
