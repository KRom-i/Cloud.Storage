package GraphicalIinterface.CommonСlassesGUI.Menu;

import GraphicalIinterface.CommonСlassesGUI.ListFiles.FileInfo;
import GraphicalIinterface.GUIBoxes.CloudBox.Cloud;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

import java.awt.*;
import java.util.List;

public class MenuOutFileByUser extends ContextMenu {

    public MenuOutFileByUser (List<FileInfo> bufListFileInfo, List<Cloud> cloudList) {

        MenuItem[] items = new MenuItem[cloudList.size()];

        for (int i = 0; i < items.length; i++) {
            final int I = i;
            items[i] = new MenuItem( "Отправить от " + cloudList.get(i).infoToStr());
            items[i].setOnAction(event -> {
                cloudList.get(I).outFile(bufListFileInfo);
            });
        }

        getItems().addAll(items);

        double x = MouseInfo.getPointerInfo().getLocation().getX();
        double y = MouseInfo.getPointerInfo().getLocation().getY();

        TextField textField = bufListFileInfo.get(0).getTextField();
        textField.setContextMenu(this);
        show (textField, x,y);
    }
}
