package Server;

import Config.Property;
import DataBase.MySqlServer;
import Logger.Log;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class CloudStorageServer {

    public CloudStorageServer () {

        MySqlServer.getConnection();

        EventLoopGroup auth = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();

        try {

            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(auth, worker)

                    .channel(NioServerSocketChannel.class)

                    .childHandler(new CloudStorageChannel());

            ChannelFuture future = bootstrap.bind(
                    Integer.parseInt(Property.get("server.conf", "PORT"))
            ).sync();


            Log.info(" --- SERVER STARTED --- ");

            future.channel().closeFuture().sync();
            Log.info(" --- SERVER FINISHED --- ");

        } catch (Exception e) {
            e.printStackTrace();
            Log.error(" --- SERVER IS RUNNING ---", e);

        } finally {
            MySqlServer.disconnect();
            auth.shutdownGracefully();
            worker.shutdownGracefully();

        }

    }


}
