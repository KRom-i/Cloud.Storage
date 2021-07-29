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

    private final List<User> userList;
    private final List<Cloud> cloudList;
    private final MouseManager mouseManager;
    private final FileBuffer fileBuffer;

    public MainBox ()  {

        this.userList = new ArrayList<>();
        this.cloudList = new ArrayList<>();
        this.fileBuffer = new FileBuffer(userList, cloudList);
        this.mouseManager = new MouseManager(fileBuffer);

        TabPaneManager paneLeft = new TabPaneManager(this);
        TabPaneManager paneRight = new TabPaneManager(this);


        Cloud cloud = new Cloud(this);

        User user = new User(this);

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

    public FileBuffer getFileBuffer () {
        return fileBuffer;
    }

    public MouseManager getMouseManager () {
        return this.mouseManager;
    }

    public void addUser(User user){
        userList.add(user);
    }

    public void removeUser(User user){
        userList.remove(user);
    }

    public void addCloud(Cloud cloud){
        cloudList.add(cloud);
    }

    public void removeCloud(Cloud cloud){
        cloudList.remove(cloud);
    }

    public void closeClouds(){

        if (!cloudList.isEmpty()){

            for(Cloud c: cloudList
            ) {
                c.closeConnector();
            }
        }
    }

    public boolean checkUpload(){
        return !cloudList.isEmpty();
    }

    public boolean checkDownload(){
        return !userList.isEmpty();
    }

    public void updateUser (String... paths) {

        if (!userList.isEmpty()){

            for(User u: userList) {

                for(String path: paths) {

                    if(path.equals(u.getCurrentAbsolutePath())){
                        u.update();
                        break;
                    }
                }
            }

        }

    }

    public void updateClouds (Cloud cloud) {

        if (!cloudList.isEmpty()){

            for(Cloud c: cloudList) {

              if (c.getNickName().equals(cloud.getNickName())){
                  c.update();
              }
            }

        }
    }




}
