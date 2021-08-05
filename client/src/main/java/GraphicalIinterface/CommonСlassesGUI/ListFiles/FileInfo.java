package GraphicalIinterface.CommonСlassesGUI.ListFiles;

import Commands.Commands;
import FileManager.FileManager;
import Format.ByteFormat;
import GraphicalIinterface.CommonСlassesGUI.Img.ImgFiles;
import GraphicalIinterface.CommonСlassesGUI.Menu.MenuOptionFile;
import GraphicalIinterface.CommonСlassesGUI.Menu.MenuShowFiles;
import GraphicalIinterface.MainUserInterface.ManagerBox;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class FileInfo extends HBox {

    private File file;
    private String pathServer;
    private String name;
    private String type;
    private long size;
    private long dateModified;
    private boolean server;
    private boolean directory;
    private boolean searchResults;
    private Label labelName;
    private TextField textFieldName;


    public FileInfo(boolean dir){

        name = "Текстовый файл.txt";

        if (dir) name = "Новая папка";

        this.textFieldName = new TextField(name);
        this.textFieldName.setMinWidth(300);

        getChildren().add(textFieldName);

        setSpacing(15);

        HBox.setHgrow(this, Priority.ALWAYS);
        VBox.setVgrow(this, Priority.NEVER);

        HBox.setHgrow(this, Priority.ALWAYS);
        VBox.setVgrow(this, Priority.NEVER);
    }

    public FileInfo (File file, boolean searchResults) {

        this.file = file;
        this.server = false;

        if (file.isDirectory()){

            this.name = file.getName();
            this.type = "DIR";
            this.directory = true;

        } else {
            this.name = getFileNameNotExtension(file);
            this.type = getFileExtension(file);
            this.size = file.length();
            this.directory = false;

        }

        this.dateModified = file.lastModified();
        this.searchResults = searchResults;
        initInteraction();
    }

    public FileInfo (File file, String pathServer, boolean searchResults) {

        if (file.isDirectory()){
            this.name = file.getName();
            this.type = "DIR";
        } else {
            this.name = new FileManager().getFileNameNotExtension(file);
            this.type = new FileManager().getFileExtension(file);
            this.size = file.length();
        }
        this.dateModified = file.lastModified();
        this.pathServer = pathServer + File.separator + file.getName();
        this.searchResults = searchResults;

    }

    public FileInfo (String msg) {

        String[] info = msg.split(Commands.DELIMITER[0]);

        this.pathServer = info[0];
        this.name = info[1];
        this.type = info[2];
        this.size = Long.parseLong(info[3]);
        this.server = true;

        if (type.equals("DIR")){
            this.file = new File(pathServer + File.separator +  name);
            this.directory = true;
        } else {
            this.file = new File(pathServer + File.separator + name + "." + type);
            this.directory = false;
        }

        this.dateModified = Long.parseLong(info[4]);
        this.searchResults = Boolean.parseBoolean(info[5]);
        initInteraction();
    }


    public void initInteraction(){

        this.labelName = new Label(name);
        this.labelName.setMinWidth(370);
        this.labelName.setMaxWidth(370);

        this.textFieldName = new TextField(getFullName());
        this.textFieldName.setManaged(false);
        this.textFieldName.setVisible(false);
        this.textFieldName.setMinWidth(370);
        this.textFieldName.setMaxWidth(370);

        HBox informationFieldName = new HBox();
        informationFieldName.setSpacing(5);
        informationFieldName.setAlignment(Pos.CENTER_LEFT);
        informationFieldName.getChildren().addAll(new ImgFiles(type,20,20),
                                                  labelName, textFieldName);

        InformationField informationFieldDate = new InformationField(getDate());

        InformationField informationFieldType = new InformationField(this.type);

        String size = ByteFormat.getStr(this.size);

        if (type.equals("DIR")) size = "";

        InformationField informationFieldSize = new InformationField(size);

        setSpacing(2.5);

        getChildren().addAll(informationFieldName, informationFieldDate,
                             informationFieldType, informationFieldSize);

        VBox.setVgrow(labelName, Priority.NEVER);
        HBox.setHgrow(labelName, Priority.ALWAYS);

        VBox.setVgrow(textFieldName, Priority.NEVER);
        HBox.setHgrow(textFieldName, Priority.ALWAYS);

        VBox.setVgrow(this, Priority.NEVER);
        HBox.setHgrow(this, Priority.ALWAYS);


        setOnMouseEntered(event -> {
            ListFiles.fileInfoMouseEntered = true;
        });

        setOnMouseExited(event -> {
            ListFiles.fileInfoMouseEntered = false;
        });



    };

    public Label getLabelQuestionDelete(){
        Label labelName = new Label(getFullName());
        labelName.setMaxWidth(300);
        return labelName;
    }

    public boolean isSearchResults () {
        return searchResults;
    }

    public String getName () {
        return name;
    }

    public String getType () {
        return type;
    }

    public long getSize () {
        return size;
    }

    private String getFileNameNotExtension(File file){
        return new FileManager().getFileNameNotExtension(file);
    }

    private String getFileExtension(File file) {
        return new FileManager().getFileExtension(file);
    }

    public File getFile () {
        return file;
    }

    public String getFullName () {
        return file.getName();
    }

    public void renameFile(){

        labelName.setManaged(false);
        labelName.setVisible(false);
        textFieldName.setManaged(true);
        textFieldName.setVisible(true);
        textFieldName.setFocusTraversable(true);

    }

    public String getPathServer(){
        return pathServer;
    }

    public TextField getTextField(){
        return textFieldName;
    }

//    yyyy.MM.dd HH:mm:ss
    private String getDate(){
        return new SimpleDateFormat("yyyy.MM.dd").format(new Date(dateModified));
    }

    public boolean isArchive() {
        return ("ZIP".equalsIgnoreCase(type) ||  "RAR".equalsIgnoreCase(type));
    }

    public boolean isConnectsServer () {
        return server;
    }

    public boolean isDirectory(){
        return directory;
    }

    public void showMenu(ManagerBox managerBox){
        new MenuOptionFile(managerBox, this);
    }

    public void showFileMenu(ManagerBox managerBox){
        new MenuShowFiles(managerBox, this);
    }


    public long getDateModified () {
        return dateModified;
    }

    public static String getPackDelete(List<FileInfo> fileInfoList){
        String[] str = new String[fileInfoList.size()];
        for (int i = 0; i < str.length; i++) {
            str[i] = fileInfoList.get(i).getPathServer();
        }
        return Commands.BUILD(0, str);
    }

    public String getPackRename (String name) {
        return Commands.BUILD(0, pathServer, name);
    }


    private class InformationField extends HBox{

        public InformationField (String name) {

            Label labelField = new Label(name);

            getChildren().add(labelField);

            labelField.setAlignment(Pos.CENTER_RIGHT);

            setAlignment(Pos.CENTER_RIGHT);

            setMinWidth(100);

            VBox.setVgrow(this, Priority.NEVER);
            HBox.setHgrow(this, Priority.ALWAYS);

        }

    }

}





