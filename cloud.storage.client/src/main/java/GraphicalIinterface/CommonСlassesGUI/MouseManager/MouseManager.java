package GraphicalIinterface.CommonСlassesGUI.MouseManager;

import Files.FileBuffer;
import GraphicalIinterface.CommonСlassesGUI.Img.ImgFiles;
import GraphicalIinterface.MainUserInterface.ManagerBox;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.*;

public class MouseManager {

    private FileBuffer fileBuffer;

    private MoveFile moveFile;

    private ManagerBox bufBox;
    private ManagerBox boxMouseEnt;

    private boolean showMenu;

    private double xCloseMenu;
    private double yCloseMenu;

    public MouseManager (FileBuffer fileBuffer) {
        this.fileBuffer = fileBuffer;
        this.showMenu = false;
    }

    public void boxMouseEnt(ManagerBox boxMouseEnt){

        double x = MouseInfo.getPointerInfo().getLocation().getX();
        double y = MouseInfo.getPointerInfo().getLocation().getY();

        if (!boxMouseEnt.equals(this.bufBox) && x == this.xCloseMenu && y == this.yCloseMenu){
            fileBuffer.copyCell(boxMouseEnt);
        }

        this.boxMouseEnt = boxMouseEnt;

    }


    public void showMenu(boolean showMenu){

            if (showMenu) {

                if (!this.showMenu) {
                    this.bufBox = this.boxMouseEnt;
                    this.moveFile = new MoveFile(bufBox);
                    this.showMenu = showMenu;
                }

            this.moveFile.update();

        } else if (moveFile != null) {


                this.showMenu = showMenu;
            this.moveFile.close();
            this.xCloseMenu = MouseInfo.getPointerInfo().getLocation().getX();
            this.yCloseMenu = MouseInfo.getPointerInfo().getLocation().getY();

        }

    }



    private class MoveFile  extends ContextMenu {

        private MoveFile (Node node) {

            CustomMenuItem customMenuItem = new CustomMenuItem();

            VBox vBoxContent = new VBox(
                    new ImgFiles("file", 80,80),
                    new Label(String.valueOf(fileBuffer.getBufListFileInfo().size())));

            vBoxContent.setAlignment(Pos.CENTER);

            customMenuItem.setContent(vBoxContent);
            getItems().addAll(customMenuItem);

            double x = MouseInfo.getPointerInfo().getLocation().getX() - 30;
            double y = MouseInfo.getPointerInfo().getLocation().getY() - 30;

            show(node, x, y);

        }


        private void update () {
            double x = MouseInfo.getPointerInfo().getLocation().getX() - 30;
            double y = MouseInfo.getPointerInfo().getLocation().getY() - 30;
            setX(x);
            setY(y);
        }


        private void close () {
            hide();
        }

    }


}
