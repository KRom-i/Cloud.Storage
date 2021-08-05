package GraphicalIinterface.CommonСlassesGUI.Navigation;

import Commands.Commands;
import GraphicalIinterface.CommonСlassesGUI.Img.CustomImg;
import GraphicalIinterface.GUIBoxes.CloudBox.Cloud;
import GraphicalIinterface.MainUserInterface.ManagerBox;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.awt.*;


public class Navigation extends HBox {

    private HBox hBoxNavigation;
    private Button butHome;
    private Button butBack;
    private Button butRoot;
    private TextField textCurrentDir;

    private HBox hBoxSearch;
    private TextField textSearch;
    private Button butSearch;

    private ManagerBox managerBox;

    public Navigation (ManagerBox managerBox) {

        this.managerBox = managerBox;

        butHome = new Button();
        new CustomImg("home", 20,20).addButton(butHome);
        butHome.setOnAction(event -> managerBox.currentDirectoryHome());

        butBack = new Button();
        new CustomImg("back2", 20,20).addButton(butBack);
        butBack.setOnAction(event -> managerBox.currentDirectoryDown());

        butRoot = new Button();
        butRoot.setText(managerBox.getCurRoot());
        butRoot.setOnAction(event -> new MenuRoot(butRoot));

        textCurrentDir = new TextField(managerBox.getCurrentDirectory());
        textCurrentDir.setOnAction(event -> {

            String cmd = textCurrentDir.getText();
            if (cmd.length() == 0) {
                managerBox.currentDirectoryHome();

            }  else {
                managerBox.setCurrentDirectory(cmd);

            }

        });


        hBoxNavigation = new HBox(butHome, butBack, butRoot, textCurrentDir);
        hBoxNavigation.setSpacing(2.5);
        hBoxNavigation.setAlignment(Pos.CENTER_RIGHT);

        if (managerBox.isConnectsServer()) nodesVis(false, butRoot);


        textSearch = new TextField();
        textSearch.setPromptText("Ввод");
        textSearch.setMinWidth(250);

        butSearch = new Button();
        new CustomImg("search", 20,20).addButton(butSearch);
        butSearch.setOnAction(event -> {
            String cmd = textSearch.getText();
            if (cmd.length() > 0) managerBox.searchFile(cmd);

        });

        hBoxSearch = new HBox(textSearch, butSearch);
        hBoxSearch.setSpacing(15);
        hBoxSearch.setAlignment(Pos.CENTER_RIGHT);

        HBox.setHgrow(textCurrentDir, Priority.ALWAYS);
        HBox.setHgrow(hBoxNavigation, Priority.ALWAYS);
        HBox.setHgrow(hBoxSearch, Priority.ALWAYS);
        VBox.setVgrow(this, Priority.NEVER);

        setSpacing(5);
        setAlignment(Pos.CENTER_RIGHT);
        setPadding(new Insets(5, 5, 5, 5));
        getChildren().addAll(new MenuNavigation(), hBoxSearch, hBoxNavigation);

        nodesVis(false, hBoxSearch);

    }


   public void update(){
       Platform.runLater(()->{
       textCurrentDir.setText(managerBox.getCurrentDirectory());
       butRoot.setText(managerBox.getCurRoot());
       });
   }

    public TextField getNode () {
        return textCurrentDir;
    }


    private void nodesVis(boolean value, Node... nodes){

        for (Node n: nodes
        ) {

            n.setVisible(value);

            n.setManaged(value);
        }
    }


    private class MenuNavigation  extends MenuBar {

        public MenuNavigation () {

            Menu mainMenu = new Menu("Меню");

            MenuItem itemNavigation  = new MenuItem("Навигация");
            itemNavigation.setOnAction(event -> {
                nodesVis(false, hBoxSearch);
                nodesVis(true, hBoxNavigation);
            });

            MenuItem itemSearch  = new MenuItem("Поиск");
            itemSearch.setOnAction(event -> {
                nodesVis(true, hBoxSearch);
                nodesVis(false, hBoxNavigation);
            });

            MenuItem itemUpdate  = new MenuItem("Обновить");
            itemUpdate.setOnAction(event -> managerBox.update());

            mainMenu.getItems().addAll(itemNavigation, itemSearch, itemUpdate);

            getMenus().add(mainMenu);

            getStyleClass().add("button");

        }



    }

    private class MenuRoot extends ContextMenu{

       public MenuRoot (Button button) {

           String[] roots = managerBox.getRoots();

           MenuItem items[] = new MenuItem[roots.length];

           for (int i = 0; i < items.length; i++) {
               final int I = i;
               items[i] = new MenuItem(roots[i]);
               items[i].setOnAction(event -> {
                   managerBox.setCurRoot(I);
                   managerBox.currentDirectoryHome();
               });
           }

           getItems().addAll(items);

           double x = MouseInfo.getPointerInfo().getLocation().getX();
           double y = MouseInfo.getPointerInfo().getLocation().getY();

           button.setContextMenu(this);
           show (button, x,y);
       }
   }
}
