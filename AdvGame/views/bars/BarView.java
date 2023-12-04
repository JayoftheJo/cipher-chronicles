package views.bars;

import javafx.scene.layout.StackPane;

/**
 * BarView interface
 */
public interface BarView {

    public void change(int howMuch);
    public void initState();

    public StackPane get();

}
