package GraphicalIinterface.GUIBoxes.CloudBox;

import Commands.Commands;
import Connector.Connector;
import Files.FileBuffer;
import Files.FileTransfer.ListTransportedFile;
import Format.ByteFormat;
import GraphicalIinterface.CommonСlassesGUI.Img.CustomImg;
import GraphicalIinterface.CommonСlassesGUI.ListFiles.ButtonsSort;
import GraphicalIinterface.CommonСlassesGUI.ListFiles.FileInfo;
import Files.IncomigFiles.IncomingFile;
import Files.IncomigFiles.ListIncomingFile;
import GraphicalIinterface.CommonСlassesGUI.DialogStage.DialogOutFile;
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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Cloud  extends ManagerBox {

    private String nickName;
    private String[] roots;
    private String root;
    private String currentDirectory = "";

    private int rootInt;
    private int idCloudClient;

    private Connector connector;

    private final MainBox mainBox;
    private final FileBuffer fileBuffer;

    private final VBox mainCloudBox;
    private final AuthBox authBox;
    private final Navigation navigation;
    private final ListFiles listFiles;
    private ShowText showText;
    private ShowImg showImg;
    private ShowMedia showMedia;
    private DialogOutFile dialogOutFile;
    private ListIncomingFile listIncomingFile;
    private final InfoCloud infoCloud;
    private Tab tab;

    private boolean auth;
    private final boolean server;

    private long currentMaxSize = 0;
    private long currentSize = 0;
    private long[] maxSizes = {0};

    private ListTransportedFile listTransportedFile;

    private HBox hBoxErrAndInfo;
    private final Label labelErrAndInfo;

    private ExecutorService executorService;

    public Cloud (MainBox mainBox) {

        this.mainBox = mainBox;

        this.fileBuffer = mainBox.getFileBuffer();

        this.root = "";

        this.authBox = new AuthBox(this);

        this.navigation = new Navigation(this);

        this.listFiles = new ListFiles(this, mainBox.getMouseManager());

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

        this.server = true;

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

        setAuth(false);
    }



    public ExecutorService getExecutorService () {
        return executorService;
    }


    public void currentDirectory (String dir) {

        if (dir != null ) {
            this.currentDirectory = dir;

        } else {
            this.currentDirectory = ""; }

    }

    @Override
    public void setCurrentDirectory (String dir) {

        sendCmd(Commands.BUILD(0, Commands.CURRENT_DIRECTORY, Commands.SET, dir));

        update();
    }


    @Override
    public void  update(){
        if (connector != null ) if (auth) sendCmd(Commands.BUILD(0, Commands.FILE, Commands.LIST, Commands.GET));
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
    public boolean isServer () {
        return server;
    }

    private void nodesVis(boolean value, Node... nodes){

        for (Node n: nodes
        ) {

            n.setVisible(value);

            n.setManaged(value);
        }
    }

    public void setAuth(boolean value){

        this.auth = value;

        if (value){ this.mainBox.addCloud(this);

        } else {

            this.mainBox.removeCloud(this);

        }

        updateTab();

        nodesVis(value, mainCloudBox);

        nodesVis(!value, authBox);
    }


    public void downloadFile (List<FileInfo> bufListFileInfo, boolean delete, String pathDownload) {

        listTransportedFile.getFiles(bufListFileInfo, delete, pathDownload, this);

    }

    @Override
    public void currentDirectoryHome(){

        sendCmd(Commands.BUILD(0, Commands.CURRENT_DIRECTORY, Commands.DIRECTORY_HOME));

        update();
    }

    @Override
    public void currentDirectoryUp (String fullName) {

        sendCmd(Commands.BUILD(0, Commands.CURRENT_DIRECTORY, Commands.UP, fullName));

        update();
    }

    @Override
    public void currentDirectoryDown(){

        sendCmd(Commands.BUILD(0, Commands.CURRENT_DIRECTORY, Commands.DOWN));

        update();
    }

    @Override
    public void createDirectory (String path) {

        sendCmd(Commands.BUILD(0, Commands.FILE, Commands.CREATE_DIR, path));

       mainBox.updateClouds(this);
    }

    @Override
    public void createFile (String path) {

        sendCmd(Commands.BUILD(0, Commands.FILE, Commands.CREATE_FILE, path));

        mainBox.updateClouds(this);
    }

    @Override
    public void delete (List<FileInfo> bufListFileInfo) {

        for(FileInfo fileDelete: bufListFileInfo) {

            sendCmd(Commands.BUILD(0, Commands.FILE, Commands.DELETE, fileDelete.getPathServer()));
        }

        mainBox.updateClouds(this);

    }

    @Override
    public void renameFile (FileInfo fileInfo, String name) {

        sendCmd(Commands.BUILD(0, Commands.FILE, Commands.RENAME, fileInfo.getPathServer(), name));

        mainBox.updateClouds(this);
    }

    @Override
    public void copyFile (ManagerBox cloud, List<FileInfo> bufListFileInfo) {


        long maxSize = currentMaxSize - currentSize;

        long copySize = 0;

        for (FileInfo fileInfo: bufListFileInfo
        ) {
            copySize += fileInfo.getFile().length();
        }

        if (copySize < maxSize){

            for (FileInfo fileInfo: bufListFileInfo
            ) {
                sendCmd(Commands.BUILD(0, Commands.FILE, Commands.COPY,
                                       fileInfo.getPathServer(), cloud.getCurrentDirectory()));
            }

            mainBox.updateClouds(this);

        } else {
            addText("Недостаточно места на облачном хранении");
        }

    }

    @Override
    public void zip(List<FileInfo> fileInfoList){
        for(FileInfo file: fileInfoList
            ) {
                    sendCmd(Commands.BUILD(0, Commands.FILE, Commands.ZIP,
                                           file.getPathServer(), getCurrentDirectory(), file.getFullName(), String.valueOf(false)));
        }
        mainBox.updateClouds(this);
    }

    @Override
    public void unZip(List<FileInfo> fileInfoList){

        for (FileInfo file: fileInfoList
             ) {
            sendCmd(Commands.BUILD(0, Commands.FILE, Commands.UNZIP,
                                   file.getPathServer(), null, null, String.valueOf(false)));
        }

        mainBox.updateClouds(this);
    }


    //    Передача файла с клиента на сервет
    public void uploadFile (String pathUpload, List<FileInfo> bufListFileInfo) {

        long maxSize = currentMaxSize - currentSize;
        listTransportedFile.outFiles(pathUpload, bufListFileInfo , maxSize);
    }



    public void auth (String log, int pas) {
        String msg = Commands.BUILD(0, Commands.AUTH, Commands.CHECK, Commands.USER, log, String.valueOf(pas));

        if (this.connector == null) {
            this.connector = new Connector(this, msg);
            connect();

        } else {
            sendCmd(msg);
        }

    }

    public void reg(String nick, String log, int pas){
        String msg = Commands.BUILD(0, Commands.AUTH, Commands.ADD, Commands.USER, nick, log, String.valueOf(pas));

        if (this.connector == null) {
            this.connector = new Connector(this, msg);
            connect();

        } else {
            sendCmd(msg);
        }

    }

    public void connect(){
        this.executorService = Executors.newFixedThreadPool(2);
        this.executorService.execute(connector);
        this.listTransportedFile = new ListTransportedFile(this, mainBox);
    }


    @Override
    public void showTextFile (FileInfo fileInfo) {
        listTransportedFile.getFile(fileInfo, this, 1);
    }

    @Override
    public void showImgFile (FileInfo fileInfo) {
        listTransportedFile.getFile(fileInfo, this, 2);

    }
    @Override
    public void showMediaFile (FileInfo fileInfo) {
        listTransportedFile.getFile(fileInfo, this, 3);

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

    public void setNickName (String nickName) {
        this.nickName = nickName;
    }


    public void closeConnector(){

        if (connector != null) {
            sendCmd(Commands.BUILD(0, Commands.AUTH, Commands.EXIT));

        }
    }

    @Override
    public void closeBox(){

        closeConnector();

        mainBox.removeCloud(this);

    }

    public void sendCmd(String cmd){
        connector.sendMsg(cmd);
    }


    @Override
    public String getCurRoot(){
        return  this.root;
    }

    @Override
    public void setCurRoot(int i){
        sendCmd(Commands.BUILD(0, Commands.CURRENT_DIRECTORY, Commands.ROOT, Commands.SET, String.valueOf(i)));
    }

    @Override
    public String[] getRoots(){
        return  this.roots;
    }


    public void setRoots(String msg){

        String[] strings = msg.split(Commands.DELIMITER[1]);

        this.roots = new String[strings.length];

        this.roots = strings;
    }

    public void setRoot(String msg){

        this.rootInt = Integer.parseInt(msg);

        this.root = roots[rootInt];
    }


    public void outFile(List<FileInfo> bufListFileInfo){
        dialogOutFile = new DialogOutFile(this, bufListFileInfo);
    }

    public void outNickName (String nick) {
        sendCmd(Commands.BUILD(0, Commands.AUTH, Commands.CHECK,
                               Commands.NICK, nick));
    }

    public void checkNick(String nickName, boolean check){

        if (dialogOutFile != null){

            if (!check){

                dialogOutFile.errorNick();

            } else if (!dialogOutFile.checkNick(nickName)){

                dialogOutFile.errorOut();

            } else {
                dialogOutFile.fileSent();

                sentFileByNickname(dialogOutFile.getFileInfoList(), nickName);
            }

        }
    }

    private void sentFileByNickname (List<FileInfo> fileInfoList, String nickName) {

        for (FileInfo fileInfo: fileInfoList
             ) {
            if (fileInfo.isServer()){
                sendCmd(Commands.BUILD(0, Commands.FILE, Commands.NICK, Commands.COPY,
                                       fileInfo.getPathServer(), nickName));
            } else {
//            uploadFile(fileInfo, null, nickName);
            }
        }

    }


    public void showInFilesStage(List<IncomingFile> incomingFiles){
        Platform.runLater(()->listIncomingFile = new ListIncomingFile(incomingFiles, navigation, this));
    }


    public void getListIncomingFiles() {
        sendCmd(Commands.BUILD(0, Commands.FILE, Commands.INCOMING, Commands.LIST));
    }

    public void downloadInFiles (List<IncomingFile> incomingFiles) {

        for(int i = 0; i < incomingFiles.size(); i++) {
            String msg = incomingFiles.get(i).getCmd();
            sendCmd(msg);
        }

        mainBox.updateClouds(this);
    }

    public void setCurrentMaxSize (String msg) {
        this.currentMaxSize = Long.parseLong(msg);
    }

    public void setCurrentSize (String msg) {
        this.currentSize = Long.parseLong(msg);
    }

    public String sizeToString(){
        return ByteFormat.getStr(currentSize) + " / " + ByteFormat.getStr(currentMaxSize);
    }

    public void setMaxSizes (String msg) {

        String[] str = msg.split(Commands.DELIMITER[1]);

        maxSizes = new long[str.length];

        for(int i = 0; i < str.length; i++) {

            maxSizes[i] = Long.parseLong(str[i]);
        }

    }

    public void outMaxSize (int i) {
        sendCmd(Commands.BUILD(0, Commands.CURRENT_DIRECTORY, Commands.SIZE, String.valueOf(i)));
        update();
    }

    public long[] getMaxSizes(){
        return this.maxSizes;
    }

    public void setNumInFile (String msg) {
        int numInFile = Integer.parseInt(msg);
        this.infoCloud.setNumInFiles(numInFile);
    }

    @Override
    public void searchFile(String msg){
        sendCmd(Commands.BUILD(0,Commands.FILE, Commands.LIST, Commands.SEARCH, msg));
    }

    @Override
    public FileBuffer getFileBuffer(){
        return this.fileBuffer;
    }

    public boolean checkUpload(){
        return mainBox.checkUpload();
    }

    public boolean checkDownload(){
        return mainBox.checkDownload();
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

    public int getCurRootInt () {
        return rootInt;
    }

    public void setConnectInfo (String msg) {
        this.idCloudClient = Integer.parseInt(msg);
    }

    @Override
    public ListFiles getListFiles(){
        return listFiles;
    }

    public String getUploadMsg () {
        return Commands.BUILD(0, Commands.LOAD, Commands.UP, String.valueOf(idCloudClient));
    }

    public int getIdCloudClient () {
        return idCloudClient;
    }

    public String getDownloadMsg () {
        return Commands.BUILD(0, Commands.LOAD, Commands.DOWN, String.valueOf(idCloudClient));
    }

    public void setResultAuthBox (String msg) {

        if (authBox.isVisible()){
            int check = Integer.parseInt(msg);
            authBox.setInfo(check);
        }

    }


    public void authAdmin (String msg) {
        sendCmd(Commands.BUILD(0,Commands.AUTH, Commands.CHECK, Commands.ADMIN, msg));
    }

    @Override
    public String getCurrentAbsolutePath(){
        return getCurRoot() + File.separator + getCurrentDirectory();
    }

    public double getSizePercent () {
        return (double) currentSize / currentMaxSize;
    }
}
