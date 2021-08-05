package Server.Handlers;

import DataTransferTools.BytePack;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class Handler extends SimpleChannelInboundHandler<BytePack> {


    @Override
    protected void channelRead0 (ChannelHandlerContext channelHandlerContext,
                                 BytePack bytePack) throws Exception {

    }

    public int getNum(){
        return 0;
    }



}
