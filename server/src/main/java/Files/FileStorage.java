package Files;

import Archive.ArhUnZipFile;
import Archive.ArhZipFile;
import Config.Property;
import DataTransferTools.RefreshPack;
import DataTransferTools.Serializer;
import FileManager.FileManager;
import TransportPackage.TransportPackage;

import java.io.File;
import java.util.List;

public class FileStorage {

    private String currentDirectory;
    private String rootDirectory;

    private long currentMaxSize;
    private long[] maxSizesStorage;

    private FileManager fileManager;

    public FileStorage (String clientNickName) {

        this.currentDirectory = "";
        this.rootDirectory = clientNickName;
        this.fileManager = new FileManager();

        createRootDirectory();
        initMaxSizesStorage ();
    }

    public void createRootDirectory(){

        File dirClient = new File(getRoot());
        if (!dirClient.exists()) dirClient.mkdirs();

    }

    public void initMaxSizesStorage(){

        this.maxSizesStorage = new long[Integer.parseInt(Property.get("server.conf", "CLOUD_SIZES"))];

        long bytes = 1_073_741_824L;

        for (int i = 0; i < this.maxSizesStorage.length; i++) {

            this.maxSizesStorage[i] = bytes * (i + 3);
        }

        this.currentMaxSize = this.maxSizesStorage[0];

    }

    public void setCurrentDirectory (String[] msg) {

        if (msg == null) {

            this.currentDirectory = "";

        } else if (fileManager.checkDirectory(getRoot() + File.separator + msg[0])) {

            this.currentDirectory = msg[0];
        }

    }

    public void currentDirectoryUp (String[] msg) {

        String path = msg[0];

        if (fileManager.checkDirectory(getAbsolutePathCurrentDirectory() + File.separator + path)){
            currentDirectory = currentDirectory + File.separator + path;

        }
    }

    public void currentDirectoryDown () {
        currentDirectory = fileManager.getParentCurrentDirectory(currentDirectory);
    }

    public String getAbsolutePathCurrentDirectory () {
        return new File(getRoot () + File.separator +  currentDirectory).toString();
    }

    public String getRoot (){
        return Property.get("server.conf", "ROOT_SERVER_DIR") + File.separator + rootDirectory;
    }

    public long getCurrentSize(){
        return fileManager.getSizeFile(getRoot());
    }

    public void setCurrentMaxSize (String msg) {

        int i = Integer.parseInt(msg);

        if (i > - 1 && i < maxSizesStorage.length){
            currentMaxSize = maxSizesStorage[i];
        }
    }

    public boolean getCheckSize (long sizePack) {
        return  (getCurrentSize() + sizePack) < currentMaxSize;
    }

    public void createFile (String[] msg) {

        fileManager.createFile(getAbsolutePathCurrentDirectory() + File.separator + msg[0]);
    }

    public void createDirectory (String[] msg) {

        fileManager.createDir(getAbsolutePathCurrentDirectory() + File.separator + msg[0]);

    }

    public void delete(String... pathMsg){

        for (String path: pathMsg
             ) {
            fileManager.deleteFiles(new File(getRoot() + File.separator + path));
        }

    }

    public void renameFile (String[] msg) {

        File file = new File(getRoot() + File.separator + msg[0]);

        fileManager.rename(file, msg[1]);
    }

    public void copy (String[] msg) {

        String pathResource = getRoot() + File.separator + msg[0];

        String pathTarget = getRoot() + File.separator + msg[1];

        boolean delete = Boolean.parseBoolean(msg[2]);

        File fileResource = new File(pathResource);

        if (!delete) {

            long sizeFile = fileResource.length();

            if (!getCheckSize(sizeFile)){

//                sendMsg(Commands.BUILD(0, Commands.ERROR, "Превышен максимальный размер"));

                return;
            }

        }

        fileManager.copyFiles(pathTarget, new File(pathResource));

        if (delete) fileManager.deleteFiles(fileResource);
    }

    public void zip (String[] msg) {
        File file = new File(getRoot() + File.separator + msg[0]);
        new ArhZipFile().getZipFiles(file);
    }

    public void unzip (String[] msg) {

        File file = new File(getRoot() + File.separator + msg[0]);
        String dir = file.getParent();
        new ArhUnZipFile().getUnzipFiles(dir, file);

    }

    public byte[] getPackRefresh(){

        RefreshPack refreshPack = new RefreshPack(
                currentDirectory,
                getCurrentSize(),
                currentMaxSize,
                maxSizesStorage,
                getArrayFileInfo()

        );

        return Serializer.serialize(refreshPack);

    }

    public String[] getArrayFileInfo(){

        File file = new File(getAbsolutePathCurrentDirectory());

        while (!file.exists()) {
            currentDirectoryDown();
            file = new File(getAbsolutePathCurrentDirectory());
        }

        File[] list = file.listFiles();

        int length = list.length;

        String[] files = new String[length];

        for(int i = 0; i < length; i++) {
            files[i] = new FileInfo(list[i], currentDirectory, false).getInfoPack();
        }

        return files;
    }

    public byte[] getSearchList (String[] msg) {

        List<FileInfo> fileInfoList = new SearchFiles().searchFile(getRoot(), msg[0]);

        if (fileInfoList  == null ) return null;

        int numFiles = fileInfoList.size();

        String[] files = new String[numFiles];

        for (int i = 0; i < fileInfoList.size(); i++) {
            files[i] = fileInfoList.get(i).getInfoPack();
        }

        RefreshPack refreshPack = new RefreshPack(
                currentDirectory,
                getCurrentSize(),
                currentMaxSize,
                maxSizesStorage,
                files

        );


        return Serializer.serialize(refreshPack);
    }


}
