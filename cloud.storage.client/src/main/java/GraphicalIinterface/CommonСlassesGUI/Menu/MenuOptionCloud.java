package GraphicalIinterface.CommonСlassesGUI.Menu;

import Format.ByteFormat;
import GraphicalIinterface.GUIBoxes.CloudBox.Cloud;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.awt.*;

public class MenuOptionCloud extends ContextMenu {

    public MenuOptionCloud (Cloud cloud, Button button) {

        long[] size = cloud.getMaxSizes();

        MenuItem[] items = new MenuItem[size.length];

        for (int i = 0; i < items.length; i++) {
            final int I = i;
            items[i] = new MenuItem( "Максимальный размер " + ByteFormat.getStr(size[i]));
            items[i].setOnAction(event -> {
                cloud.outMaxSize(I);
            });
        }

        getItems().addAll(items);

        double x = MouseInfo.getPointerInfo().getLocation().getX();
        double y = MouseInfo.getPointerInfo().getLocation().getY();

        button.setContextMenu(this);
        show (button, x,y);
    }

}
