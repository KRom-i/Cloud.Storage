package GraphicalIinterface.GUIBoxes.CloudBox;

import Commands.Commands;
import Connector.Connector;
import DataTransferTools.BytePack;
import DataTransferTools.RefreshPack;
import DataTransferTools.Serializer;;
import Files.ManagerTransportPackages;
import Format.ByteFormat;
import GraphicalIinterface.CommonСlassesGUI.Img.CustomImg;
import GraphicalIinterface.CommonСlassesGUI.ListFiles.FileInfo;
import GraphicalIinterface.MainUserInterface.*;
import GraphicalIinterface.GUIBoxes.CloudBox.AuthBox.AuthBox;
import GraphicalIinterface.CommonСlassesGUI.ShowFiles.ShowImg;
import GraphicalIinterface.CommonСlassesGUI.ShowFiles.ShowMedia;
import GraphicalIinterface.CommonСlassesGUI.ShowFiles.ShowText;
import GraphicalIinterface.MainUserInterface.ManagerBox;
import GraphicalIinterface.CommonСlassesGUI.ListFiles.ListFiles;
import GraphicalIinterface.CommonСlassesGUI.Navigation.Navigation;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static Address.Address.*;


public class Cloud  extends ManagerBox {

    private String nickName;
    private String currentDirectory = "";

    private Connector connector;

    private final VBox mainCloudBox;
    private final AuthBox authBox;
    private final Navigation navigation;
    private final ListFiles listFiles;
    private ShowText showText;
    private ShowImg showImg;
    private ShowMedia showMedia;
    private final InfoCloud infoCloud;
    private Tab tab;

    private boolean auth;

    private long currentMaxSize = 0;
    private long currentSize = 0;
    private long[] maxSizes = {0};

    private ManagerTransportPackages managerTransportPackages;

    private HBox hBoxErrAndInfo;
    private final Label labelErrAndInfo;

    private ExecutorService executorService;

    public Cloud () {

        this.authBox = new AuthBox(this);
        this.navigation = new Navigation(this);
        this.listFiles = new ListFiles(this);
        this.infoCloud = new InfoCloud(this);

        Button buttonCloseInfo = new Button();
        buttonCloseInfo.setPadding(new Insets(0,0,0,0));
        new CustomImg("close", 20,20).addButton(buttonCloseInfo);

        this.labelErrAndInfo = new Label();
        labelErrAndInfo.getStyleClass().add("label-info");

        hBoxErrAndInfo = new HBox(buttonCloseInfo, labelErrAndInfo);
        buttonCloseInfo.setOnAction(event -> nodesVis(false, hBoxErrAndInfo));

        hBoxErrAndInfo.setMaxHeight(20);
        hBoxErrAndInfo.setSpacing(5);
        hBoxErrAndInfo.getStyleClass().add("box-list-files");
        nodesVis(false, hBoxErrAndInfo);

        this.mainCloudBox = new VBox();
        this.mainCloudBox.setSpacing(5);
        this.mainCloudBox.getChildren().addAll(navigation, listFiles, infoCloud, hBoxErrAndInfo);

        HBox.setHgrow(hBoxErrAndInfo, Priority.ALWAYS);

        HBox.setHgrow(mainCloudBox, Priority.ALWAYS);
        VBox.setVgrow(mainCloudBox, Priority.ALWAYS);

        HBox.setHgrow(this, Priority.ALWAYS);
        VBox.setVgrow(this, Priority.ALWAYS);

        getChildren().addAll(mainCloudBox, authBox);
        getStyleClass().add("box-manager");

        setPadding(new Insets(2,2,2,2));
        setAlignment(Pos.CENTER);

        setAuth(false, null);
    }


    @Override
    public void setCurrentDirectory (String dir) {
        connector.writeBytes(new BytePack(FILE, DIR_SET, dir, null));
        update();
    }


    @Override
    public void  update(){
        if (connector != null  && auth) { ;
            connector.writeBytes(new BytePack(FILE, REFRESH, null,null));
        };
    }

    public void update (List<FileInfo> fileInfoList) {

        listFiles.update(fileInfoList);

        navigation.update();

        infoCloud.update();

        updateTab();
    }

   @Override
    public String getCurrentDirectory(){
        return currentDirectory;
    }

    @Override
    public boolean isCurrentDirectory(){
        return currentDirectory.length() > 0;
    }

    @Override
    public void addNewBox(FileInfo fileInfo){
        listFiles.add(fileInfo);
    }

    @Override
    public boolean isConnectsServer () {
        return true;
    }

    private void nodesVis(boolean value, Node... nodes){

        for (Node n: nodes
        ) {

            n.setVisible(value);

            n.setManaged(value);
        }
    }

    public void setAuth(boolean value, String nickName){

        this.auth = value;
        if (nickName != null) this.nickName = nickName;

        if (auth){
            MainBox.addCloud(this);
            update();
        } else {
            MainBox.removeCloud(this);
        }

        updateTab();
        nodesVis(auth, mainCloudBox);
        nodesVis(!auth, authBox);
    }


    public void downloadFile (List<FileInfo> bufListFileInfo, boolean delete, String pathDownload) {

        managerTransportPackages.getFiles(bufListFileInfo, delete, pathDownload, this);

    }

    @Override
    public void currentDirectoryHome(){
        connector.writeBytes(new BytePack(FILE, DIR_HOME, null, null));
        update();
    }

    @Override
    public void currentDirectoryUp (String fullName) {
        connector.writeBytes(new BytePack(FILE, DIR_UP, fullName, null));
        update();
    }

    @Override
    public void currentDirectoryDown(){
        connector.writeBytes(new BytePack(FILE, DIR_DOWN,  null, null));
        update();
    }

    @Override
    public void createDirectory (String path) {
        connector.writeBytes(new BytePack(FILE, CREATE_DIR, path, null));
        MainBox.updateClouds();

    }

    @Override
    public void createFile (String path) {
        connector.writeBytes(new BytePack(FILE, CREATE_FILE, path, null));
        MainBox.updateClouds();

    }

    @Override
    public void delete (List<FileInfo> bufListFileInfo) {
        String msg = FileInfo.getPackDelete(bufListFileInfo);
        connector.writeBytes(new BytePack(FILE, DELETE, msg, null));
        MainBox.updateClouds();

    }

    @Override
    public void renameFile (FileInfo fileInfo, String name) {
        connector.writeBytes(new BytePack(FILE, RENAME, fileInfo.getPackRename(name), null));
        MainBox.updateClouds();

    }

    @Override
    public void copyFile (ManagerBox cloud, List<FileInfo> bufListFileInfo, boolean delete) {

            for (FileInfo fileInfo: bufListFileInfo) {

                String msg = Commands.BUILD(0,
                                            fileInfo.getPathServer(),
                                            cloud.getCurrentDirectory(),
                                            String.valueOf(delete));

                connector.writeBytes(new BytePack(FILE, COPY, msg, null));
            }

        MainBox.updateClouds();

    }

    @Override
    public void zip(List<FileInfo> fileInfoList){
        for(FileInfo file: fileInfoList) {

            String msg = Commands.BUILD(0,
                                        file.getPathServer(),
                                        getCurrentDirectory(),
                                        file.getFullName(),
                                        String.valueOf(false));

                    connector.writeBytes(new BytePack(FILE, ZIP, msg, null));
        }
        MainBox.updateClouds();

    }

    @Override
    public void unZip(List<FileInfo> fileInfoList){

        for (FileInfo file: fileInfoList
             ) {
            String msg = Commands.BUILD(0, Commands.FILE, Commands.UNZIP,
                                   file.getPathServer(), null, null, String.valueOf(false));

            connector.writeBytes(new BytePack(FILE, UNZIP, msg, null));
        }

        MainBox.updateClouds();
    }


    //    Передача файла с клиента на сервет
    public void uploadFile (String pathUpload, List<FileInfo> bufListFileInfo) {
        this.managerTransportPackages.sendFiles(pathUpload, bufListFileInfo);
    }



    public void auth (String log, int pas) {

        this.executorService = Executors.newFixedThreadPool(3);

        executorService.execute(()->{

            String msg = Commands.BUILD(0, log, String.valueOf(pas));

            BytePack packStart = new BytePack(AUTH, LOG, msg, null);

            connector = new Connector(this, packStart);

            this.managerTransportPackages = new ManagerTransportPackages(executorService, connector);

            connector.run();

        });


    }




    public void reg(String nick, String log, int pas){

//        String msg = Commands.BUILD(0, Commands.AUTH, Commands.ADD, Commands.USER, nick, log, String.valueOf(pas));
//
//        if (this.connector == null) {
////            this.connector = new Connector(this, msg);
//            connect();
//
//        } else {
////            sendCmd(2, msg);
//        }

    }

    public void connect(){
//        this.executorService = Executors.newFixedThreadPool(2);
//        this.executorService.execute(connector);
//        this.managerTransportPackages = new ManagerTransportPackages(this, mainBox);
    }


    @Override
    public void showTextFile (FileInfo fileInfo) {
//        listTransportPackage.getFile(fileInfo, this, 1);
    }

    @Override
    public void showImgFile (FileInfo fileInfo) {
//        listTransportPackage.getFile(fileInfo, this, 2);

    }
    @Override
    public void showMediaFile (FileInfo fileInfo) {
//        listTransportPackage.getFile(fileInfo, this, 3);

    }

    public void openBoxShowTextFile (File file) {

        closeShowBoxes();

        showText = new ShowText(file, null, true, true);

        add(showText);
    }


    public void openBoxShowImgFile (File file) {

        closeShowBoxes();

        showImg = new ShowImg(file, true);

        add(showImg);
    }


    public void openBoxShowMediaFile (File file) {

        closeShowBoxes();

        showMedia = new ShowMedia(file, true);

        add(showMedia);
    }

    private void closeShowBoxes(){

        if (showText != null){

            showText.close();
        }
        if (showImg != null){

            showImg.close();
        }
        if (showMedia != null){

            showMedia.close();
        }
    }

    private void add(Node node){
        Platform.runLater(()-> getChildren().add(node));
    }

    public String getNickName () {
        return nickName;
    }

    public void closeConnector(){

        if (connector != null) {
            connector.writeBytes(new BytePack(AUTH, EXIT, null, null));
        }
    }

    @Override
    public void closeBox(){
        closeConnector();
        MainBox.removeCloud(this);
    }


    @Override
    public String getCurRoot(){
        return  "";
    }

    @Override
    public void setCurRoot(int i){
//        sendCmd(2, Commands.BUILD(0, Commands.CURRENT_DIRECTORY, Commands.ROOT, Commands.SET, String.valueOf(i)));
    }


    public String sizeToString(){
        return ByteFormat.getStr(currentSize) + " / " + ByteFormat.getStr(currentMaxSize);
    }


    public long[] getMaxSizes(){
        return this.maxSizes;
    }

    @Override
    public void searchFile(String msg){
        connector.writeBytes(new BytePack(FILE, SEARCH, msg, null));
    }


    @Override
    public String infoToStr(){

        if (!auth) return "Аутентификация";
        return nickName;
    }

    @Override
    public void addTab (Tab tab) {
        this.tab = tab;
    }

    private void updateTab(){
        Platform.runLater(()->{

        if (tab != null){
            tab.setText(infoToStr());
        } });
    }

    @Override
    public TextField getNode(){
        return navigation.getNode();
    }

    public void addText(String text){
        Platform.runLater(()-> {
            nodesVis(true, hBoxErrAndInfo);
            labelErrAndInfo.setText(text);
        });
    }


    @Override
    public ListFiles getListFiles(){
        return listFiles;
    }


    @Override
    public String getCurrentAbsolutePath(){
        return getCurRoot() + File.separator + getCurrentDirectory();
    }

    public double getSizePercent () {
        return (double) currentSize / currentMaxSize;
    }

    public void refresh (byte[] data) {

        RefreshPack refreshPack = (RefreshPack) Serializer.deserialize(data);

        currentDirectory = refreshPack.getCurrentDirectory();

        currentSize = refreshPack.getCurrentSizeStorage();

        currentMaxSize = refreshPack.getCurrentMaxSize();

        maxSizes = refreshPack.getSizesStorage();

        List<FileInfo> fileInfoList = new ArrayList<>();

        for (String file: refreshPack.getArrayFileInfo()
             ) {
            fileInfoList.add(new FileInfo(file));
        }

        update(fileInfoList);
    }


    public void route(BytePack packIn){

        switch (packIn.getAddress1()){

            case AUTH:

                switch (packIn.getAddress2()){

                    case LOG:
                        setAuth(Boolean.parseBoolean(packIn.getMsg()[0]), packIn.getMsg()[1]);
                        break;

                    case EXIT:
                        connector.close();
                        executorService.shutdown();
                        break;
                }

                break;

            case FILE:

                switch (packIn.getAddress2()){

                    case REFRESH:
                        refresh(packIn.getData());
                        break;
                }
                break;

            case LOAD:
                managerTransportPackages.setPack(packIn);
                break;
        }

    }

}
