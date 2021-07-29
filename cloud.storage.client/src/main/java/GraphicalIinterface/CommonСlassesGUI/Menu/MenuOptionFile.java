package GraphicalIinterface.CommonСlassesGUI.Menu;

import Files.FileBuffer;
import GraphicalIinterface.CommonСlassesGUI.ListFiles.FileInfo;
import GraphicalIinterface.CommonСlassesGUI.DialogStage.QuestionDelete;
import GraphicalIinterface.GUIBoxes.CloudBox.Cloud;
import GraphicalIinterface.MainUserInterface.ManagerBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.awt.*;

public class MenuOptionFile extends ContextMenu {

    private double x;
    private double y;
    private FileBuffer fileBuffer;

    public MenuOptionFile (ManagerBox managerBox, FileInfo fileInfo) {

        this.fileBuffer = managerBox.getFileBuffer();

        if (fileInfo != null) {

            if (fileInfo.isDirectory()){

                MenuItem itemOpenDir = new MenuItem("Открыть");

                itemOpenDir.setOnAction((event) -> {

                    managerBox.currentDirectoryUp(fileInfo.getFullName ());
                });
                getItems().add(itemOpenDir);

            } else {

                MenuItem itemReadText = new MenuItem("Открыть как текст");

                itemReadText.setOnAction((event) -> {

                    managerBox.showTextFile(fileInfo);
                });

                MenuItem itemReadImg = new MenuItem("Открыть как изображение");

                itemReadImg.setOnAction((event) -> {

                    managerBox.showImgFile(fileInfo);
                });

                MenuItem itemMedia = new MenuItem("Открыть как видео");

                itemMedia.setOnAction((event) -> {

                    managerBox.showMediaFile(fileInfo);
                });

                getItems().addAll(itemReadText, itemReadImg, itemMedia);
            }

        }

        if (managerBox.isCurrentDirectory()){

            MenuItem itemBack = new MenuItem("Назад");

            itemBack.setOnAction((event) -> {

                managerBox.currentDirectoryDown();
            });

            getItems().add(itemBack);

        }


        if (fileBuffer.checkBuf()) {

            if(fileBuffer.checkBuf()) {

                MenuItem itemUpload = new MenuItem("Оправить в Cloud storage");

                itemUpload.setOnAction((event) -> {
                    fileBuffer.upload(null);
                });


                MenuItem itemDownload = new MenuItem("Скачать файл");

                itemDownload.setOnAction((event) -> {

                    fileBuffer.download((Cloud) managerBox, null);
                });

                MenuItem itemOutFileUser = new MenuItem("Оправить пользователю");

                itemOutFileUser.setOnAction((event) -> {
                    fileBuffer.outFileUser(managerBox);
                });

                if(fileInfo.isServer()) {

                    itemDownload.setVisible(managerBox.checkDownload());

                    itemUpload.setVisible(false);

                    itemOutFileUser.setVisible(true);

                } else {

                    itemUpload.setVisible(managerBox.checkUpload());

                    itemOutFileUser.setVisible(managerBox.checkUpload());

                    itemDownload.setVisible(false);
                }


                MenuItem itemRename = new MenuItem("Переименовать");

                itemRename.setOnAction((event) -> {

                    fileInfo.renameFile();

                    fileInfo.getTextField().setOnAction((a) -> {
                        managerBox.renameFile(fileInfo, fileInfo.getTextField().getText());
                    });
                });

                MenuItem itemDelete = new MenuItem("Удалить");

                itemDelete.setOnAction((event) -> {
                    new QuestionDelete(managerBox, fileBuffer.getBufListFileInfo(), x, y);
                });

                MenuItem itemCopy = new MenuItem("Копировать");
                itemCopy.setOnAction((event) -> {
                    fileBuffer.updateBufferList(false);
                });

                MenuItem itemCut = new MenuItem("Вырезать");
                itemCut.setOnAction((event) -> {
                    fileBuffer.updateBufferList(true);
                });

                getItems().addAll(itemUpload, itemDownload, itemOutFileUser, itemRename, itemDelete, itemCopy, itemCut);
            }
        }

        if (fileInfo != null) {

                MenuItem itemInsert = new MenuItem("Вставить");

                itemInsert.setOnAction((event) -> {
                    fileBuffer.copyBuf(managerBox);

                });

                getItems().add(itemInsert);
        }

        if (fileInfo != null){

            MenuItem itemZip = new MenuItem();

            if (fileInfo.isArchive()){

                itemZip.setText("Разархивировать");

                itemZip.setOnAction((event)->{

                    managerBox.unZip(fileBuffer.getBufListFileInfo());
                });
            } else {

                itemZip.setText("Архивировать");

                itemZip.setOnAction((event)->{

                    managerBox.zip(fileBuffer.getBufListFileInfo());
                });

            }

            getItems().add(itemZip);
        }

        MenuItem itemCreateDir = new MenuItem("Создать папку");

        itemCreateDir.setOnAction((event)->{

            FileInfo newFileInfo = new FileInfo(true);

            newFileInfo.getTextField().setOnAction((a) ->{

                managerBox.createDirectory(newFileInfo.getTextField().getText());
            });

            managerBox.getListFiles().getFileInfoListView().getItems().add(newFileInfo);
        });

        MenuItem itemCreateFile = new MenuItem("Создать файл");

        itemCreateFile.setOnAction((event)->{

            FileInfo newFileInfo = new FileInfo(false);

            newFileInfo.getTextField().setOnAction((a) ->{

                managerBox.createFile(newFileInfo.getTextField().getText());
            });

            managerBox.getListFiles().getFileInfoListView().getItems().add(newFileInfo);
        });

        getItems().addAll(itemCreateDir, itemCreateFile);

        x = MouseInfo.getPointerInfo().getLocation().getX();
        y = MouseInfo.getPointerInfo().getLocation().getY();

        managerBox.getNode().setContextMenu(this);
        show (managerBox.getNode(), x,y);
    }
}
