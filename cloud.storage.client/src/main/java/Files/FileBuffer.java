package Files;

import GraphicalIinterface.CommonСlassesGUI.ListFiles.FileInfo;
import GraphicalIinterface.CommonСlassesGUI.Menu.MenuDownloadFile;
import GraphicalIinterface.CommonСlassesGUI.Menu.MenuOutFileByUser;
import GraphicalIinterface.CommonСlassesGUI.Menu.MenuUploadFile;
import GraphicalIinterface.GUIBoxes.CloudBox.Cloud;
import GraphicalIinterface.MainUserInterface.ManagerBox;
import GraphicalIinterface.GUIBoxes.UserBox.User;
import javafx.collections.ListChangeListener;


import java.util.ArrayList;
import java.util.List;

public class FileBuffer {

    private List<User> userList;
    private List<Cloud> cloudList;
    private List<FileInfo> listCellFileInfo;
    private  List<FileInfo> bufFileInfo;
    private ManagerBox bufManagerBox;
    private boolean delete;


    public FileBuffer (List<User> userList, List<Cloud> cloudList) {
        this.userList = userList;
        this.cloudList = cloudList;
        this.bufFileInfo = new ArrayList<>();
    }

    public void upload (List<FileInfo> uploadListFileInfo) {

        if (uploadListFileInfo == null ) uploadListFileInfo = listCellFileInfo;

        if (cloudList.size() > 1){

            new MenuUploadFile(cloudList, uploadListFileInfo);

        } else {

            cloudList.get(0).uploadFile(cloudList.get(0).getCurrentDirectory(),  uploadListFileInfo);
        }

    }


    public void copyBuf(ManagerBox managerBox){

        if (!bufManagerBox.isServer() && !managerBox.isServer()){

            User user1 = (User) bufManagerBox;
            User user2 = (User) managerBox;

            user1.copyFile(user2, bufFileInfo);

            if (delete) managerBox.delete(bufFileInfo);

        } else if (!bufManagerBox.isServer() && managerBox.isServer()){

            Cloud cloud = (Cloud) managerBox;

            cloud.uploadFile(cloud.getCurrentDirectory(), bufFileInfo);

            if (delete) bufManagerBox.delete(bufFileInfo);

        } else if (bufManagerBox.isServer() && !managerBox.isServer()){

            Cloud cloud = (Cloud) bufManagerBox;

            cloud.downloadFile(bufFileInfo, delete, ((User) managerBox).getCurrentAbsolutePath());


        } else if (bufManagerBox.isServer() && managerBox.isServer()){

            Cloud cloudBuf  = (Cloud) bufManagerBox;

            Cloud cloud  = (Cloud) managerBox;

            cloudBuf.copyFile(cloud, bufFileInfo);

            if (delete) bufManagerBox.delete(bufFileInfo);
        }
    }

    public void copyCell(ManagerBox managerBox){

        if (!bufManagerBox.isServer() && !managerBox.isServer()){

            User user1 = (User) bufManagerBox;
            User user2 = (User) managerBox;

            user1.copyFile(user2, listCellFileInfo);

            if (delete) managerBox.delete(listCellFileInfo);

        } else if (!bufManagerBox.isServer() && managerBox.isServer()){

            Cloud cloud = (Cloud) managerBox;

            cloud.uploadFile(cloud.getCurrentDirectory(), listCellFileInfo);

            if (delete) bufManagerBox.delete(listCellFileInfo);

        } else if (bufManagerBox.isServer() && !managerBox.isServer()){

            Cloud cloud = (Cloud) bufManagerBox;

            cloud.downloadFile(listCellFileInfo, delete, ((User) managerBox).getCurrentAbsolutePath());


        } else if (bufManagerBox.isServer() && managerBox.isServer()){

            Cloud cloudBuf  = (Cloud) bufManagerBox;

            Cloud cloud  = (Cloud) managerBox;

            cloudBuf.copyFile(cloud, listCellFileInfo);
        }
    }


    public void download(Cloud cloud, List<FileInfo> fileInfoList){

        if (fileInfoList == null) fileInfoList = listCellFileInfo;

        if (userList.size() > 1){

            new MenuDownloadFile(cloud, fileInfoList, userList);

        } else {

            cloud.downloadFile(fileInfoList, false, userList.get(0).getCurrentAbsolutePath());
        }
    }

    public void outFileUser (ManagerBox managerBox) {

        if (!managerBox.isServer()){

            if (cloudList.size() > 1){

                new MenuOutFileByUser(listCellFileInfo, cloudList);

            } else {

                cloudList.get(0).outFile(listCellFileInfo);
            }

        } else {
            ((Cloud) managerBox).outFile(listCellFileInfo);
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
        this.bufFileInfo.addAll(listCellFileInfo);
        this.delete = value;
    }
}
