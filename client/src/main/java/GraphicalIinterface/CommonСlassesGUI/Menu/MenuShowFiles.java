package GraphicalIinterface.CommonСlassesGUI.Menu;

import GraphicalIinterface.CommonСlassesGUI.ListFiles.FileInfo;
import GraphicalIinterface.MainUserInterface.ManagerBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

import java.awt.*;

public class MenuShowFiles extends ContextMenu {

    public MenuShowFiles (ManagerBox managerBox, FileInfo fileInfo) {


        MenuItem itemReadText = new MenuItem("Открыть как текст");
        itemReadText.setOnAction((event) -> {
            managerBox.showTextFile(fileInfo);
        });
        MenuItem itemReadImg = new MenuItem("Открыть как изображение");
        itemReadImg.setOnAction((event) -> {
            managerBox.showImgFile(fileInfo);
        });
        MenuItem itemMedia = new MenuItem("Открыть как видео");
        itemMedia.setOnAction((event) -> {
            managerBox.showMediaFile(fileInfo);
        });

        getItems().addAll(itemReadText, itemReadImg, itemMedia);



        double x = MouseInfo.getPointerInfo().getLocation().getX() + 15;
        double y = MouseInfo.getPointerInfo().getLocation().getY();

        TextField textField = fileInfo.getTextField();
        textField.setContextMenu(this);
        show (textField, x,y);
    }
}
