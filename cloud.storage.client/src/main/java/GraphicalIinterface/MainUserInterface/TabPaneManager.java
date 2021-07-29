package GraphicalIinterface.MainUserInterface;

import GraphicalIinterface.GUIBoxes.CloudBox.Cloud;
import GraphicalIinterface.GUIBoxes.UserBox.User;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.awt.*;


public class TabPaneManager extends TabPane {


    private MainBox mainBox;
    private MenuAddBox menuAddBox;

    public TabPaneManager (MainBox mainBox) {

        this.mainBox = mainBox;

        HBox.setHgrow(this, Priority.ALWAYS);
        VBox.setVgrow(this, Priority.ALWAYS);

        setOnMouseClicked(event -> {

            double yWindow = getScene().getWindow().getY();
            double yMouse = MouseInfo.getPointerInfo().getLocation().getY();

                if ((yWindow - yMouse) > - 80 && event.getButton().toString().equals("SECONDARY")) {
                    if (menuAddBox != null) menuAddBox.getItems().clear();
                    menuAddBox = new MenuAddBox(this);
                }

        });


    }

    public void addManager (ManagerBox managerBox) {

        Tab tab = new Tab(managerBox.infoToStr());

        tab.setContent(managerBox);

        managerBox.addTab(tab);

        tab.setOnClosed(event -> {
            if (menuAddBox != null) menuAddBox.getItems().clear();
            managerBox.closeBox() ;
        });

        tab.setOnSelectionChanged(event -> {
            managerBox.update();
            if (menuAddBox != null) menuAddBox.getItems().clear();
        } );

        getTabs().add(tab);

    }


    private class MenuAddBox extends ContextMenu {

        public MenuAddBox (TabPaneManager tabPaneManager) {

            MenuItem menuAddCloud = new MenuItem("Добавить вкладку облачного хранения");
            menuAddCloud.setOnAction(event -> addManager(new Cloud(mainBox)));

            MenuItem menuAddUser = new MenuItem("Добавить вкладку файлов на ПК");
            menuAddUser.setOnAction(event -> addManager(new User(mainBox)));

            getItems().addAll(menuAddCloud, menuAddUser);

            double x = MouseInfo.getPointerInfo().getLocation().getX();
            double y = MouseInfo.getPointerInfo().getLocation().getY();

            show (tabPaneManager, x,y);
        }

    }

}
