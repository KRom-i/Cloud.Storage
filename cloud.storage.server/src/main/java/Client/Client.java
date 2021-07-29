package Client;

import DataBase.DataClients;
import Archive.ArhUnZipFile;
import Archive.ArhZipFile;
import Files.IncomigFiles.IncomingFile;
import FileManager.*;
import Commands.*;
import Configuration.Config;
import Files.FileInfo;
import Files.FileTransfer.TransportedFile;
import Files.SearchFiles;
import Logger.Log;
import javafx.scene.layout.HBox;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Client extends HBox {

    private int idClient;
    private String name;
    private String currentDirectory;
    private String rootFull;

    private boolean auth;
    private boolean admin;

    private final String ROOT_SERVER_DIR = Config.ROOT_SERVER_DIR;
    private final String ROOT_PUBLIC_DIR = ROOT_SERVER_DIR +  File.separator + Config.ROOT_PUBLIC_DIR;
//    private final String ROOT_BY_USER_DIR = ROOT_SERVER_DIR + File.separator + Config.ROOT_BY_USER_DIR;

    private String client_dir;
    private String root_client_dir;

    private String[] roots;
    private String[] admin_roots;

    private final String[] ROOTS_NAME = {"ЛИЧ", "ОБЩ"};
    private final String[] ADMIN_ROOTS_NAME = {"ЛИЧ", "ОБЩ", "АДМ", "АДМ off"};
    private int root;

    private long currentMaxSize;
    private long[] maxSize;

    private ClientHandler connect;

    private FileManager fileManager;


    public Client (int idClient, String name) {

        this.name = name;

        this.idClient = idClient;

        this.currentDirectory = "";

        this.client_dir = name;

        this.root_client_dir =  ROOT_SERVER_DIR  + File.separator + client_dir;

        this.rootFull = root_client_dir;

        this.auth = true;

        this.fileManager = new FileManager();

        this.root = 0;

        this.roots = new String[]{root_client_dir , ROOT_PUBLIC_DIR};

        this.admin_roots = new String[]{root_client_dir, ROOT_PUBLIC_DIR, ROOT_SERVER_DIR};

        File dirClient = new File(getRootFullAbsolutePath());
        if (!dirClient.exists()) dirClient.mkdirs();

        initMaxSizes();
    }



    public void initMaxSizes(){

        this.maxSize = new long[Config.CLOUD_SIZES];

        long bytes = 1_073_741_824L;

        for (int i = 0; i < this.maxSize.length; i++) {

            this.maxSize[i] = bytes * (i + 3);
        }

        this.currentMaxSize = this.maxSize[0];

    }

    public int getIdClient(){
        return idClient;
    }


    public TransportedFile setUploadPath (TransportedFile transportedFile) {

        int i = transportedFile.getRoot();

        String rootPath = null;

        if (!admin){
            if (i > -1 && i < roots.length) {
                rootPath = roots[i];}

        } else {
            if (i > -1 && i < admin_roots.length) {
                rootPath = admin_roots[i];
            }
        }

        if (rootPath == null ) return  null;

        File file = new File(rootPath);

        if (!file.exists()) return null;

        transportedFile.setPath(file.getAbsolutePath() + File.separator + transportedFile.getPath());

        return transportedFile;
    }

    public void setCurrentDirectory (String currentDirectory) {

        if (currentDirectory == null) {

            this.currentDirectory = "";

        } else if (fileManager.checkDirectory(getRootFullAbsolutePath() + File.separator + currentDirectory)) {

            this.currentDirectory = currentDirectory;
        }

    }

    public void currentDirectoryUp (String path) {

        if (fileManager.checkDirectory(getCurrentPath() + File.separator + path)){

            currentDirectory = currentDirectory + File.separator + path;

        }
    }

    public void currentDirectoryDown () {

        currentDirectory = fileManager.getParentCurrentDirectory(currentDirectory);

    }

    public void createFile (String path) {

        fileManager.createFile(getCurrentPath() + File.separator + path);
    }

    public void createDirectory (String path) {

        fileManager.createDir(getCurrentPath() + File.separator + path);

    }

    public void delete(String pathMsg){

        fileManager.deleteFiles(new File(getRootFullAbsolutePath() + File.separator + pathMsg));
    }

    public void renameFile (String path, String name) {

        File file = new File(getRootFullAbsolutePath() + File.separator + path);

        fileManager.rename(file, name);
    }

    public void copy (String path1, String path2) {

        path1 = getRootFullAbsolutePath() + File.separator + path1;

        path2 = getRootFullAbsolutePath() + File.separator + path2;

        fileManager.copyFiles(path2, new File(path1));
    }

    public void zip (String filePath, String zipPath, String name, String delete) {
        File file = new File(getRootFullAbsolutePath() + File.separator + filePath);
        new ArhZipFile().getZipFiles(file);
    }

    public void unzip (String filePath, String zipPath, String name, String delete) {

        File file = new File(getRootFullAbsolutePath() + File.separator + filePath);
        String dir = file.getParent();
        new ArhUnZipFile().getUnzipFiles(dir, file);

    }

    public String getRoots(){

        if (!admin){

            return Commands.BUILD(0, Commands.ROOT, Commands.INFO,
                                  Commands.BUILD(1, ROOTS_NAME), String.valueOf(root));
        } else {

            return Commands.BUILD(0, Commands.ROOT, Commands.INFO,
                                  Commands.BUILD(1, ADMIN_ROOTS_NAME), String.valueOf(root));
        }
    }

    public void setRoot(String msg){

        int i = Integer.parseInt(msg);

        if (!admin){

            if (i > -1 && i < roots.length) {
                this.root = i;
                this.rootFull = roots[i];
            };

        } else {

            if (i > -1 && i < admin_roots.length) {
                this.root = i;
                this.rootFull = this.admin_roots[i];

            } else {

                this.root = 0;
                this.admin = false;
                this.rootFull  = this.root_client_dir;
            }
        }

        update();

    }

    public void checkAdmin (String msg){
        int pas = msg.hashCode();
        boolean checkAdmin = new DataClients().checkAdmin(idClient, pas);

        if (checkAdmin){
            admin = checkAdmin;
            update();
        }
    }

    public boolean isAdmin () {
        return admin;
    }

    public String getCurrentPath () {
        return new File(getRootFullAbsolutePath () + File.separator +  currentDirectory).toString();
    }

    public String getRootFullAbsolutePath (){
        return new File(rootFull).getAbsolutePath();
    }

    public String getCurrentDirectoryMsg(){

        if (currentDirectory.length() == 0){

            return Commands.BUILD(0,Commands.
                    CURRENT_DIRECTORY, Commands.DIRECTORY_HOME);

        } else {

            return Commands.BUILD(0,Commands.
                    CURRENT_DIRECTORY, new File(currentDirectory).toString()); }
    }

    public boolean isAuth () {
        return auth;
    }

    public String getName () {
        return name;
    }

    @Override
    public String toString () {
        return "Client{" +
                "idClient=" + idClient +
                ", name='" + name + '\'' +
                ", auth=" + auth +
                ", admin=" + admin +
                '}';
    }

    public String[] listFile(){

        File file = new File(getCurrentPath());

        while (!file.exists()) {
            currentDirectoryDown();
            file = new File(getCurrentPath());
        }

        File[] list = file.listFiles();

        int length = list.length;

        String[] msg = new String[length + 1];

        msg[0] = Commands.BUILD(0, Commands.FILE, Commands.LIST, String.valueOf(length));

        for(int i = 0; i < length; i++) {
            msg[i + 1] = new FileInfo(list[i], currentDirectory, false).toString();
        }

        return msg;
    }

    public void update () {
        sendMsg(getNotificationIncomingFiles(), getRoots(), sizeToString());
        sendMsg(listFile());
        sendMsg(getCurrentDirectoryMsg());
    }

    public long getCurrentSize(){
        return fileManager.getSizeFile(getRootFullAbsolutePath());
    }

    public String sizeToString(){

        String[] sizes = new String[maxSize.length];

        for(int i = 0; i < sizes.length; i++) {

            sizes[i] = String.valueOf(maxSize[i]);
        }

        return Commands.BUILD(0, Commands.SIZE, Commands.SET,
                              String.valueOf(getCurrentSize()),
                              String.valueOf(currentMaxSize),
                              Commands.BUILD(1, sizes));
    }


    public void setCurrentMaxSize (String msg) {

        int i = Integer.parseInt(msg);

        if (i > - 1 && i < maxSize.length){
            currentMaxSize = maxSize[i];
        }
    }


    public void sendMsg(String... msg){
        connect.sendMessage(msg);
    }

    public void close () {
        sendMsg(Commands.BUILD(0, Commands.AUTH, Commands.EXIT));
    }

    public void authorization (ClientHandler connect) {

        this.connect = connect;

        Log.info("CLIENT authorization: " + toString());

        sendMsg(Commands.BUILD(0, Commands.AUTH, Commands.CHECK, Commands.USER, String.valueOf(true)));

        sendMsg(Commands.BUILD(0, Commands.NICK, Commands.SET, name));

        sendConnectInfo();

        update();
    }

    private String[] getArrayMessage(){
        return getMessage().split(Commands.DELIMITER[0]);
    }

    private String getMessage(){
        return connect.getMessage();
    }

    public void sendUploadResponse (TransportedFile transportedFile) {
        sendMsg(transportedFile.getUploadResponse());
    }

    public void sendErrorUploadResponse (String msg) {
        sendMsg(Commands.BUILD(0, Commands.LOAD, Commands.ERROR, Commands.UP, msg));
    }

    public void sendConnectInfo () {
        sendMsg(Commands.BUILD(0, Commands.AUTH, Commands.INFO, String.valueOf(idClient)));
    }


    public List<TransportedFile> getTransportedFileList (TransportedFile transportedFile) {

        int intRoot = transportedFile.getRoot();

        String path = "";

        if (intRoot < admin_roots.length - 1){
            path += roots[intRoot];

        } else if (admin){
            path += roots[intRoot];
        }


       File file = new File(path += File.separator + transportedFile.getPath());

        List<TransportedFile> transportedFileList = getTransportedFileList(path ,file);

        return transportedFileList;
    }


        public List<TransportedFile> getTransportedFileList (String path, File... files) {

        List<WalkFile> walkFiles = new FileManager().getWalkFiles(files);

        List<TransportedFile> transportedFileList = new ArrayList<>();

            if(walkFiles.isEmpty()) return null;

            for(WalkFile wf : walkFiles) {
                if(!wf.isDirectory()) transportedFileList.add(new TransportedFile(wf, false));
            }


        return transportedFileList;
    }

    public void sendListIncomingFiles () {

        File dir = new File(Config.ROOT_SERVER_DIR + File.separator + Config.ROOT_BY_USER_DIR + File.separator + name);

        if (dir.exists()) {

            File[] files = dir.listFiles();

            String[] msg = new String[files.length + 1];

            msg[0] = Commands.BUILD(0, Commands.FILE, Commands.INCOMING,
                                    Commands.LIST, String.valueOf(files.length));

            for (int i = 0; i < files.length; i++) {

                msg[i + 1] = new IncomingFile(files[i]).getCmd();

            }

            if (msg.length > 1 ) sendMsg(msg);
        };

    }

    public String getNotificationIncomingFiles(){

        int numFiles = 0;

        File dir = new File(Config.ROOT_SERVER_DIR + File.separator + Config.ROOT_BY_USER_DIR + File.separator + name);

        if (dir.exists()) numFiles = dir.listFiles().length;

        return Commands.BUILD(0, Commands.FILE, Commands.INCOMING,
                               Commands.INFO, String.valueOf(numFiles));
    }

    public void getIncomingFile (String msg) {

        IncomingFile incomingFile = new IncomingFile(msg);

        File file = new File(Config.ROOT_SERVER_DIR + File.separator
                                     + Config.ROOT_BY_USER_DIR + File.separator + name
                                     + File.separator + incomingFile.getFullName());

        if (incomingFile.isDownload()) {

            Path target = Paths.get(getCurrentPath() + File.separator + incomingFile.getName());

            fileManager.copy(file.toPath(), target);
        }

        if (incomingFile.isDelete()) fileManager.deleteFiles(file);

    }

    public void getSearchList (String fileName) {

       List<FileInfo> fileInfoList = new SearchFiles().searchFile(getRootFullAbsolutePath(), fileName);

       if (fileInfoList  == null ) return;

       int numFiles = fileInfoList.size();

        String[] msg = new String[numFiles + 1];

        msg[0] = Commands.BUILD(0, Commands.FILE, Commands.LIST, String.valueOf(numFiles));

        for (int i = 0; i < fileInfoList.size(); i++) {
            msg[i + 1] = fileInfoList.get(i).toString();
        }

        sendMsg(msg);
    }
}
