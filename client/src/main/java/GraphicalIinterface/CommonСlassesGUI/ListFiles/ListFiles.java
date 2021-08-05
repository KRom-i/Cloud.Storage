package GraphicalIinterface.CommonСlassesGUI.ListFiles;

import Files.FileBuffer;
import GraphicalIinterface.CommonСlassesGUI.Menu.MenuOptionFile;
import GraphicalIinterface.CommonСlassesGUI.MouseManager.MouseManager;
import GraphicalIinterface.MainUserInterface.MainBox;
import GraphicalIinterface.MainUserInterface.ManagerBox;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ListFiles extends VBox{

    private ManagerBox managerBox;
    private ListView<FileInfo> fileInfoListView;
    public static boolean fileInfoMouseEntered;


    public ListFiles (ManagerBox managerBox) {

        this.managerBox = managerBox;
        this.fileInfoListView = new ListView<>();

        getChildren().addAll(new ButtonsSort(this),fileInfoListView);

        HBox.setHgrow(fileInfoListView, Priority.ALWAYS);
        VBox.setVgrow(fileInfoListView, Priority.ALWAYS);

        HBox.setHgrow(this, Priority.ALWAYS);
        VBox.setVgrow(this, Priority.ALWAYS);


        fileInfoListView.setOnMouseClicked(event -> {
            if (fileInfoListView.getItems().isEmpty()) new MenuOptionFile(managerBox, null);
        });
        fileInfoListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ListChangeListener<FileInfo> multiSelection = new ListChangeListener<FileInfo>(){
            @Override
            public void onChanged(  Change<? extends FileInfo> changed){
                MainBox.fileBuffer.buffering(managerBox, changed);
            }
        };
        fileInfoListView.getSelectionModel().getSelectedItems().addListener(multiSelection);

        getStylesheets().add("style.css");

        setOnMouseEntered((event)-> MainBox.mouseManager.boxMouseEnt(managerBox));

        setSpacing(2.5);
        setPadding(new Insets(5, 5, 5, 5));

        getStyleClass().add("box-list-files");
    }

    public void update() {

        File file = new File(managerBox.getCurrentAbsolutePath());

        List<FileInfo> fileInfoList = new ArrayList<>();

        for (File f: file.listFiles()
        ) {
            fileInfoList.add(new FileInfo(f, false));
        }

        update(fileInfoList);
    };

    public void update (List<FileInfo> fileInfoList) {

        Platform.runLater(() -> {
            fileInfoListView.getItems().clear();

            for (FileInfo f: fileInfoList) {
                initInteraction(f);
            }

            fileInfoListView.getItems().addAll(fileInfoList);

        });

    }

    public ListView<FileInfo> getFileInfoListView () {
        return fileInfoListView;
    }

    public void initInteraction(FileInfo fileInfo){

        fileInfo.setOnMouseClicked(event -> {

            if (!event.getButton().toString().equals("SECONDARY")){

                if (event.getClickCount() == 2){

                    if (fileInfo.isDirectory()){

                        if (!fileInfo.isSearchResults()) {
                        managerBox.currentDirectoryUp(fileInfo.getFullName());

                        } else if (fileInfo.isConnectsServer()) {
                            managerBox.setCurrentDirectory(fileInfo.getPathServer());
                        }

                    } else {
                        fileInfo.showFileMenu(managerBox);
                    }
                }

            } else {
                fileInfo.showMenu(managerBox);
            }

        });

        fileInfo.setOnMouseReleased((event -> MainBox.mouseManager.showMenu(false)));
        fileInfo.setOnMouseDragged((event)-> MainBox.mouseManager.showMenu(true));

    }

    public void add (FileInfo fileInfo) {
        Platform.runLater(()->{
            getChildren().add(fileInfo);
        });
    }


    public void sortName(boolean value){
        if (!value){
            fileInfoListView.getItems().sort(Comparator.comparing(FileInfo::getFullName));
        }  else {
            fileInfoListView.getItems().sort(Comparator.comparing(FileInfo::getFullName).reversed());
        }
    }

    public void sortDate(boolean value){
        if (!value){
            fileInfoListView.getItems().sort(Comparator.comparing(FileInfo::getDateModified));
        }  else {
            fileInfoListView.getItems().sort(Comparator.comparing(FileInfo::getDateModified).reversed());
        }
    }


    public void sortType(boolean value){
        if (!value){
            fileInfoListView.getItems().sort(Comparator.comparing(FileInfo::getType));
        }  else {
            fileInfoListView.getItems().sort(Comparator.comparing(FileInfo::getType).reversed());
        }
    }


    public void sortSize(boolean value){
        if (!value){
            fileInfoListView.getItems().sort(Comparator.comparing(FileInfo::getSize));
        }  else {
            fileInfoListView.getItems().sort(Comparator.comparing(FileInfo::getSize).reversed());
        }
    }




}
