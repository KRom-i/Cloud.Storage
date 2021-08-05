package Server.Handlers.PacketHandler;

import DataTransferTools.BytePack;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class  OutPackHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write (ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        BytePack pack = (BytePack) msg;

        ByteBuf byteBuf = ctx.alloc().buffer();

        byteBuf.writeBytes(pack.getBiLength());

        byteBuf.writeBytes(pack.getHead());

        if (pack.getLengthData() > 0) byteBuf.writeBytes(pack.getData());

        ctx.writeAndFlush(byteBuf);


    }



}
