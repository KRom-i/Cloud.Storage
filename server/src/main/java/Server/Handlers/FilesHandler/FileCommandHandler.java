package Server.Handlers.FilesHandler;

import Files.FileStorage;
import Server.Handlers.Handler;
import DataTransferTools.BytePack;
import io.netty.channel.ChannelHandlerContext;
import static Address.Address.*;

public class FileCommandHandler extends Handler {

    private FileStorage fileStorage;
    private byte[] data;
    private BytePack packOut;

    public FileCommandHandler (FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    @Override
    protected void channelRead0 (ChannelHandlerContext channel, BytePack pack) throws Exception {

        String[] msg = pack.getMsg();

        switch (pack.getAddress2()) {

            case DIR_UP:
                fileStorage.currentDirectoryUp(msg);
                break;

            case DIR_DOWN:
                fileStorage.currentDirectoryDown();
                break;

            case DIR_HOME:
                fileStorage.setCurrentDirectory(null);
                break;

            case DIR_SET:
                fileStorage.setCurrentDirectory(msg);
                break;

            case CREATE_FILE:
                fileStorage.createFile(msg);
                break;

            case CREATE_DIR:
                fileStorage.createDirectory(msg);
                break;

            case DELETE:
                fileStorage.delete(msg);
                break;

            case RENAME:
                fileStorage.renameFile(msg);
                break;

            case COPY:
                fileStorage.copy(msg);
                break;

            case ZIP:
                fileStorage.zip(msg);
                break;

            case UNZIP:
                fileStorage.unzip(msg);
                break;

            case REFRESH:
                data = fileStorage.getPackRefresh();
                packOut = new BytePack(FILE, REFRESH, null, data);

                channel.writeAndFlush(packOut);
                break;

            case SEARCH:
                data = fileStorage.getSearchList(msg);
                packOut = new BytePack(FILE, SEARCH, null, data);

                channel.writeAndFlush(packOut);
                break;


        }
    }

    @Override
    public int getNum(){
        return FILE;
    }



}
