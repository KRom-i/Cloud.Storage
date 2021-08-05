package Files;

import Config.Property;
import Connector.Connector;
import DataTransferTools.BytePack;
import DataTransferTools.BytesPacker;
import DataTransferTools.Serializer;
import TransportPackage.TransportPackage;
import TransportPackage.*;
import GraphicalIinterface.CommonСlassesGUI.ListFiles.FileInfo;
import GraphicalIinterface.GUIBoxes.CloudBox.Cloud;
import GraphicalIinterface.MainUserInterface.MainBox;
import java.util.List;
import java.util.concurrent.ExecutorService;
import static Address.Address.*;

public class ManagerTransportPackages {


    private ExecutorService service;
    private Connector connector;
    private TransportPackageMap transportPackageMap;
    private TransportedFileRead transportedFileRead;
    private TransportedFileWrite transportedFileWrite;

    private int sizeBuf;

    public boolean upload;
    public boolean download;

    public ManagerTransportPackages (ExecutorService service, Connector connector) {
        this.service = service;
        this.connector = connector;
        this.sizeBuf = Integer.parseInt(Property.get("client.conf", "BUFFER_SIZE"));
        this.transportPackageMap = new TransportPackageMap();
        this.transportedFileRead = new TransportedFileRead(sizeBuf);
        this.transportedFileWrite = new TransportedFileWrite();
    }

    public void getFiles (List<FileInfo> bufListFileInfo, boolean delete, String path, Cloud cloud) {

        try {

        for(FileInfo file: bufListFileInfo) {

            int id = getIdTranPack();

            TransportPackage transportPackage = new TransportPackage(file.getFullName(), false, file.getPathServer(), path, id);

            add(transportPackage);

            BytePack pack = BytesPacker.getPackDownload(transportPackage);

            connector.writeBytes(pack);
        }

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }


    }

    public void sendFiles (String path, List<FileInfo> fileInfoList) {

        try {

        TransportPackage[] transportPackages = new TransportPackage[fileInfoList.size()];

        for (int i = 0; i < transportPackages.length; i++) {

            int id = getIdTranPack();

            transportPackages[i] = new TransportPackage(fileInfoList.get(i).getFile(), true, id);

            add(transportPackages[i]);

            BytePack pack = BytesPacker.getPackUpload(transportPackages[i]);

            connector.writeBytes(pack);

        }

        } catch (Exception e){
            e.printStackTrace();
        }


    }

    private int getIdTranPack () {
        return  transportPackageMap.getSize() + 1;
    }

    public void add(TransportPackage transportPackage){
        transportPackageMap.add(transportPackage);
    }


    public void setPack (BytePack pack) {

        switch (pack.getAddress2()){

            case DATA_UP:
                service.execute(()-> connector.writeBytes(getPackUploadTranFile()));
                break;
                
            case DATA_DOWN:
                service.execute(()-> connector.writeBytes(setPackDownloadTranFile(pack)));
                break;

            case PACK_DOWN:
                connector.writeBytes(replacePack(pack.getData()));
                break;

        }

    }

    private BytePack setPackDownloadTranFile (BytePack pack) {

        TransportedFile transportedFile = transportPackageMap.get(pack.getMsg());

        transportedFileWrite.write(transportedFile, pack.getData());

        if (transportedFile.getSize() == transportedFile.getByteRead()){
            MainBox.updateUser();
            MainBox.updateClouds();
        }

        return new BytePack(LOAD, DATA_DOWN, null, null);
    }

    private BytePack replacePack (byte[] data) {
        TransportPackage transportPackage = (TransportPackage) Serializer.deserialize(data);

        transportPackageMap.replace(transportPackage);

        return new BytePack(LOAD, DATA_DOWN, null ,null);
    }

    private BytePack getPackUploadTranFile () {

        TransportedFile transportedFile = transportPackageMap.getNotRead();

        if (transportedFile == null) return new BytePack(LOAD, UP_END, null, null);

        byte[] data = transportedFileRead.getDataFile(transportedFile);

        String msg = transportedFileRead.getMsg();

        if (transportedFile.getSize() == transportedFile.getByteRead()){
            MainBox.updateUser();
            MainBox.updateClouds();
        }

        return new BytePack(LOAD, DATA_UP, msg, data);
    }
}