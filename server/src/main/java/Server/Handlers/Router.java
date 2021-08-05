package Server.Handlers;

import DataTransferTools.BytePack;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.ArrayList;
import java.util.List;


public class Router extends ChannelInboundHandlerAdapter {

    public static List<Handler> handlerList;

    public Router () {
        handlerList = new ArrayList<>();
    }

    @Override
    public void channelRead (ChannelHandlerContext ctx, Object msg) throws Exception {

        BytePack pack = (BytePack) msg;

        for (Handler handler: handlerList
             ) {

            if (pack.getAddress1() == handler.getNum()){

                handler.channelRead0(ctx, pack); break;
            }

        }

    }






}
