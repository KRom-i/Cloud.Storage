package GraphicalIinterface.MainUserInterface;

import Files.FileBuffer;
import GraphicalIinterface.CommonСlassesGUI.MouseManager.MouseManager;
import GraphicalIinterface.GUIBoxes.CloudBox.Cloud;
import GraphicalIinterface.GUIBoxes.UserBox.User;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

public class MainBox extends HBox {

     public static List<User> userList;
     public static List<Cloud> cloudList;
    public static MouseManager mouseManager;
    public static FileBuffer fileBuffer;

    public MainBox ()  {

        userList = new ArrayList<>();
        cloudList = new ArrayList<>();
        fileBuffer = new FileBuffer();
        mouseManager = new MouseManager();

        TabPaneManager paneLeft = new TabPaneManager(this);
        TabPaneManager paneRight = new TabPaneManager(this);


        Cloud cloud = new Cloud();

        User user = new User();

        paneLeft.addManager(cloud);

        paneRight.addManager(user);

        SplitPane splitPane = new SplitPane(paneLeft, paneRight);

        getChildren().addAll(splitPane);

        HBox.setHgrow(paneLeft, Priority.ALWAYS);
        VBox.setVgrow(paneLeft, Priority.ALWAYS);

        HBox.setHgrow(paneRight, Priority.ALWAYS);
        VBox.setVgrow(paneRight, Priority.ALWAYS);

        HBox.setHgrow(splitPane, Priority.ALWAYS);
        VBox.setVgrow(splitPane, Priority.ALWAYS);

        HBox.setHgrow(this, Priority.ALWAYS);
        VBox.setVgrow(this, Priority.ALWAYS);

        getStylesheets().add("style.css");
        getStyleClass().add("box-main");

    }

    public static void addUser(User user){
        userList.add(user);
    }

    public static void removeUser(User user){
        userList.remove(user);
    }

    public static void addCloud(Cloud cloud){
        cloudList.add(cloud);
    }

    public static void removeCloud(Cloud cloud){
        cloudList.remove(cloud);
    }

    public static void closeClouds(){

        if (!cloudList.isEmpty()){

            for(Cloud c: cloudList
            ) {
                c.closeConnector();
            }
        }
    }

    public static boolean checkUpload(){
        return !cloudList.isEmpty();
    }

    public static boolean checkDownload(){
        return !userList.isEmpty();
    }

    public static void updateUser () {

        if (!userList.isEmpty()){

            for(User u: userList) {

                        u.update();
                }

        }

    }

    public static void updateClouds () {

        if (!cloudList.isEmpty()){

            for(Cloud c: cloudList) {

                    c.update();
              }
            }


    }




}
