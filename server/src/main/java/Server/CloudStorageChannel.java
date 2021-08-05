package Server;


import Server.Handlers.ClientHandler.AuthenticationHandler;
import Server.Handlers.PacketHandler.InPackHandler;
import Server.Handlers.PacketHandler.OutPackHandler;
import Server.Handlers.Router;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;


public class CloudStorageChannel extends ChannelInitializer {

    @Override
    protected void initChannel (Channel channel) throws Exception {

        channel.pipeline().addLast(
                new InPackHandler(),
                new OutPackHandler(),
                new Router(),
                new AuthenticationHandler());

    }


}
