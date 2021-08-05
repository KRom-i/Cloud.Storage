package GraphicalIinterface.CommonСlassesGUI.Menu;

import GraphicalIinterface.CommonСlassesGUI.ListFiles.FileInfo;
import GraphicalIinterface.GUIBoxes.CloudBox.Cloud;
import GraphicalIinterface.GUIBoxes.UserBox.User;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

import java.awt.*;
import java.util.List;

public class MenuDownloadFile extends ContextMenu {

    public MenuDownloadFile (Cloud cloud, List<FileInfo> bufListFileInfo, List<User> userList) {

        MenuItem[] items = new MenuItem[userList.size()];

        for (int i = 0; i < items.length; i++) {
            final int I = i;
            items[i] = new MenuItem(userList.get(i).infoToStr());
            items[i].setOnAction(event -> {
                cloud.downloadFile(bufListFileInfo, false, userList.get(I).getCurrentAbsolutePath());
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