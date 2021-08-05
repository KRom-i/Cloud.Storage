package GraphicalIinterface.MainUserInterface;
import Files.FileBuffer;
import GraphicalIinterface.CommonСlassesGUI.ListFiles.FileInfo;
import GraphicalIinterface.CommonСlassesGUI.ListFiles.ListFiles;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;

public class ManagerBox extends VBox {

    public void currentDirectoryUp(String path){};

    public void currentDirectoryHome(){};

    public void currentDirectoryDown(){};

    public void setCurrentDirectory(String dir){};

    public String getCurrentDirectory(){
        return null;
    }

    public String getCurrentAbsolutePath() {
       return null;
    }

    public void searchFile(String cmd){};

    public void createFile(String path){};

    public void createDirectory(String path){};

    public void addNewBox(FileInfo fileInfo){};

    public void renameFile(FileInfo fileInfo, String name){};

    public void delete(List<FileInfo> bufListFileInfo){};

    public void copyFile(ManagerBox managerBox, List<FileInfo> bufListFileInfo, boolean delete) {};

    public boolean isConnectsServer () {
        return false;
    }

    public boolean isCurrentDirectory(){
        return  false;
    };

    public void showTextFile(FileInfo fileInfo){ }

    public void showImgFile(FileInfo fileInfo){};

    public void showMediaFile(FileInfo fileInfo){};

    public void update(){};

    public ListFiles getListFiles(){
        return null;
    }

    public void setFileManagerDownload (ManagerBox managerBoxDownload) {

    }

    public void setCurRoot(int i){
    }

    public String getCurRoot(){
        return  null;
    };

    public String[] getRoots(){
        return  null;
    };

    public void zip(List<FileInfo> fileInfoList){

    };

    public void unZip(List<FileInfo> fileInfoList){

    };

    public String infoToStr(){
        return  null;
    }

    public void addTab (Tab tab) {
    }

    public void closeBox(){
    }

    public TextField getNode(){
        return null;
    }


}

