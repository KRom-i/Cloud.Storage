package GraphicalIinterface.CommonСlassesGUI.Menu;

import GraphicalIinterface.CommonСlassesGUI.ShowFiles.ShowText;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import java.awt.*;

public class MenuCharset  {

    public MenuCharset (ShowText showText, TextField textField, String[] charset) {

        ContextMenu contextMenu = new ContextMenu();

        MenuItem[] item = new MenuItem[charset.length];

        for (int i = 0; i < item.length; i++) {

            final int FINAL_I = i;
            item[i] = new MenuItem(charset[i]);
            item[i].setOnAction(event -> {
                showText.read(charset[FINAL_I]);
            });

        }

        contextMenu.getItems().addAll(item);
        textField.setContextMenu(contextMenu);

        double x = MouseInfo.getPointerInfo().getLocation().getX();
        double y = MouseInfo.getPointerInfo().getLocation().getY();

        contextMenu.show (textField, x, y);
    }
}
