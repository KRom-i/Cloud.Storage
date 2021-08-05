package Files;

import GraphicalIinterface.CommonСlassesGUI.ListFiles.FileInfo;
import GraphicalIinterface.CommonСlassesGUI.Menu.MenuDownloadFile;
import GraphicalIinterface.CommonСlassesGUI.Menu.MenuUploadFile;
import GraphicalIinterface.GUIBoxes.CloudBox.Cloud;
import GraphicalIinterface.MainUserInterface.MainBox;
import GraphicalIinterface.MainUserInterface.ManagerBox;
import GraphicalIinterface.GUIBoxes.UserBox.User;
import javafx.collections.ListChangeListener;


import java.util.ArrayList;
import java.util.List;

public class FileBuffer {

    private List<FileInfo> listCellFileInfo;
    private  List<FileInfo> bufFileInfo;
    private ManagerBox bufManagerBox;
    private boolean delete;

    public FileBuffer () {
        this.bufFileInfo = new ArrayList<>();
    }

    public void upload (List<FileInfo> uploadListFileInfo) {

        if (uploadListFileInfo == null ) uploadListFileInfo = listCellFileInfo;

        if (MainBox.cloudList.size() > 1){

            new MenuUploadFile(MainBox.cloudList, uploadListFileInfo);

        } else {

            MainBox.cloudList.get(0).uploadFile(MainBox.cloudList.get(0).getCurrentDirectory(),  uploadListFileInfo);
        }

    }


    public void copyBuf(ManagerBox managerBox){

        if (!bufManagerBox.isConnectsServer() && !managerBox.isConnectsServer()){

            User user1 = (User) bufManagerBox;
            User user2 = (User) managerBox;

            user1.copyFile(user2, bufFileInfo, delete);


        } else if (!bufManagerBox.isConnectsServer() && managerBox.isConnectsServer()){

            Cloud cloud = (Cloud) managerBox;

            cloud.uploadFile(cloud.getCurrentDirectory(), bufFileInfo);


        } else if (bufManagerBox.isConnectsServer() && !managerBox.isConnectsServer()){

            Cloud cloud = (Cloud) bufManagerBox;

            cloud.downloadFile(bufFileInfo, delete, ((User) managerBox).getCurrentAbsolutePath());


        } else if (bufManagerBox.isConnectsServer() && managerBox.isConnectsServer()){

            Cloud cloudBuf  = (Cloud) bufManagerBox;

            Cloud cloud  = (Cloud) managerBox;

            cloudBuf.copyFile(cloud, bufFileInfo, delete);

        }
    }

    public void copyCell(ManagerBox managerBox){

        if (!bufManagerBox.isConnectsServer() && !managerBox.isConnectsServer()){

            User user1 = (User) bufManagerBox;
            User user2 = (User) managerBox;

            user1.copyFile(user2, listCellFileInfo, delete);

            if (delete) managerBox.delete(listCellFileInfo);

        } else if (!bufManagerBox.isConnectsServer() && managerBox.isConnectsServer()){

            Cloud cloud = (Cloud) managerBox;

            cloud.uploadFile(cloud.getCurrentDirectory(), listCellFileInfo);

            if (delete) bufManagerBox.delete(listCellFileInfo);

        } else if (bufManagerBox.isConnectsServer() && !managerBox.isConnectsServer()){

            Cloud cloud = (Cloud) bufManagerBox;

            cloud.downloadFile(listCellFileInfo, delete, ((User) managerBox).getCurrentAbsolutePath());


        } else if (bufManagerBox.isConnectsServer() && managerBox.isConnectsServer()){

            Cloud cloudBuf  = (Cloud) bufManagerBox;

            Cloud cloud  = (Cloud) managerBox;

            cloudBuf.copyFile(cloud, listCellFileInfo, delete);
        }
    }


    public void download(Cloud cloud, List<FileInfo> fileInfoList){

        if (fileInfoList == null) fileInfoList = listCellFileInfo;

        if (MainBox.userList.size() > 1){

            new MenuDownloadFile(cloud, fileInfoList, MainBox.userList);

        } else {

            cloud.downloadFile(fileInfoList, false, MainBox.userList.get(0).getCurrentAbsolutePath());
        }
    }



    public void buffering(ManagerBox managerBox, ListChangeListener.Change<? extends FileInfo> changed){

        this.bufManagerBox = managerBox;

        if (this.listCellFileInfo == null) this.listCellFileInfo = new ArrayList<>();

        this.listCellFileInfo.clear();

        this.listCellFileInfo.addAll(changed.getList());

    }

    public boolean checkBuf(){

        if (this.listCellFileInfo == null) return false;

        if (this.listCellFileInfo.isEmpty()) return false;

        return true;
    }

    public List<FileInfo> getBufListFileInfo () {
        return listCellFileInfo;
    }


    public void updateBufferList (boolean value) {
        this.bufFileInfo.clear();
        this.bufFileInfo.addAll(listCellFileInfo);
        this.delete = value;
    }
}
