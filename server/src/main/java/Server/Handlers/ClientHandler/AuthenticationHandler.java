package Server.Handlers.ClientHandler;

import DataBase.Client;
import DataBase.DataClients;
import Files.FileStorage;
import Logger.Log;
import Server.Handlers.FilesHandler.FileCommandHandler;
import Server.Handlers.FilesHandler.LoadHandler;
import Server.Handlers.Handler;
import DataTransferTools.BytePack;
import Server.Handlers.Router;
import io.netty.channel.ChannelHandlerContext;
import static Address.Address.*;

public class AuthenticationHandler extends Handler {

    public AuthenticationHandler () {
        Router.handlerList.add(this);
    }

    @Override
    protected void channelRead0 (ChannelHandlerContext channel, BytePack pack) throws Exception {

        String[] msg = pack.getMsg();

        switch (pack.getAddress2()){

            case LOG:

                Client client = new DataClients().get(msg[0], msg[1]);

                if (client != null) {

                    FileStorage fileStorage = client.getFileStorage();

                    FileCommandHandler fileCommandHandler = new FileCommandHandler(fileStorage);
                    LoadHandler loadHandler = new LoadHandler(fileStorage);

                    Router.handlerList.add(fileCommandHandler);
                    Router.handlerList.add(loadHandler);

                    channel.pipeline().addLast(fileCommandHandler, loadHandler);

                    channel.writeAndFlush(
                            new BytePack(AUTH, LOG, client.getPackAuth(), null)
                    );
                }

               break;

            case EXIT:
                channel.writeAndFlush(new BytePack(AUTH, EXIT, null, null));
                channel.close();
                break;
        }


    }

    @Override
    public int getNum () {
        return AUTH;
    }


    @Override
    public void channelActive (ChannelHandlerContext ctx) throws Exception {
        Log.info(" --- Client connected --- " + ctx);
    }

    @Override
    public void channelInactive (ChannelHandlerContext ctx) throws Exception {
        Log.info(" --- Client disconnected --- " + ctx);
    }
}
