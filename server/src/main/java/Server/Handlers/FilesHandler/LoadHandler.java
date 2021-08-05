package Server.Handlers.FilesHandler;

import Commands.Commands;
import Config.Property;
import DataTransferTools.BytePack;
import DataTransferTools.BytesPacker;
import DataTransferTools.Serializer;
import Files.FileStorage;
import Server.Handlers.Handler;
import TransportPackage.*;
import io.netty.channel.ChannelHandlerContext;
import static Address.Address.*;
import java.io.File;

public class LoadHandler extends Handler {

    private FileStorage fileStorage;
    private TransportPackageMap transportPackageMap;
    private TransportedFileRead transportedFileRead;
    private TransportedFileWrite transportedFileWrite;

    public LoadHandler (FileStorage fileStorage) {
        this.fileStorage = fileStorage;
        this.transportPackageMap = new TransportPackageMap();
        this.transportedFileRead = new TransportedFileRead(
                Integer.parseInt(Property.get("server.conf", "BUFFER_SIZE")
                ));
        this.transportedFileWrite = new TransportedFileWrite();
    }

    @Override
    protected void channelRead0 (ChannelHandlerContext channel, BytePack bytePack) throws Exception {

        switch (bytePack.getAddress2()){

            case PACK_UP:
                channel.writeAndFlush(checkTransportPackage(bytePack.getData()));
                break;

            case PACK_DOWN:
                channel.writeAndFlush(getTranPackDownload(bytePack.getData()));
                break;

            case DATA_UP:
                channel.writeAndFlush(setPackUpload(bytePack));
                break;

            case DATA_DOWN:
                channel.writeAndFlush(getPackDownloadTransportedFile());
                break;

        }

    }



    private BytePack setPackUpload (BytePack bytePack) {

        TransportedFile transportedFile = transportPackageMap.get(bytePack.getMsg());

        if (transportedFile == null) {
            String msg = Commands.BUILD(1, String.valueOf(transportedFile.getIdPack()),
                                        String.valueOf(transportedFile.getId()));
            return new BytePack(LOAD, NOT_FOUND, msg, null);

        } else{
            transportedFileWrite.write(transportedFile, bytePack.getData());

            return new BytePack(LOAD, DATA_UP, null, null);
        }
    }

    private BytePack checkTransportPackage (byte[] data) {

        TransportPackage transportPackage = (TransportPackage) Serializer.deserialize(data);

        if (!fileStorage.getCheckSize(transportPackage.getSize()))
            return new BytePack(LOAD, SIZE, String.valueOf(transportPackage.getId()), null);

        transportPackageMap.add(transportPackage, fileStorage.getAbsolutePathCurrentDirectory());

        return new BytePack(LOAD, DATA_UP,  String.valueOf(transportPackage.getId()), null);

    }

    private BytePack getTranPackDownload (byte[] data) throws CloneNotSupportedException {

        TransportPackage transportPackage = (TransportPackage) Serializer.deserialize(data);

        File fileDown = new File(fileStorage.getRoot() + File.separator + transportPackage.getPathServer());
        int idPack = transportPackage.getId();

        TransportPackage transportPackageDown = new TransportPackage(fileDown, false, idPack);

        transportPackageMap.add(transportPackageDown);

        return BytesPacker.getPackDownload(transportPackageDown);
    }

    private BytePack getPackDownloadTransportedFile  () {

        TransportedFile transportedFile = transportPackageMap.getNotRead();

        if (transportedFile == null) return new BytePack(LOAD, DOWN_END, null, null);

        byte[] data = transportedFileRead.getDataFile(transportedFile);

        String msg = transportedFileRead.getMsg();

        return new BytePack(LOAD, DATA_DOWN, msg, data);
    }




    @Override
    public int getNum () {
        return LOAD;
    }
}
