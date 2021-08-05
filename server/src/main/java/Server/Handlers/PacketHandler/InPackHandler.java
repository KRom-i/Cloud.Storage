package Server.Handlers.PacketHandler;

import DataTransferTools.BinarySystem;
import DataTransferTools.BytePack;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

public class InPackHandler extends ByteToMessageDecoder {


    private final byte[] biLength = new byte[4];

    private CompositeByteBuf composite;

    private boolean start;
    private boolean head;

    private int lengthHead;
    private BytePack inPack;

    public InPackHandler () {
       this.composite = ByteBufAllocator.DEFAULT.compositeBuffer();
    }

    @Override
    protected void decode (ChannelHandlerContext channel, ByteBuf byteBuf, List<Object> list) throws Exception {

        composite.writeBytes(byteBuf, byteBuf.readableBytes());

        while (composite.isReadable()) {

            int readable = composite.readableBytes();

            if(!start && readable >= biLength.length) {

                start = true;

                composite.readBytes(biLength);

                lengthHead = BinarySystem.integerDecoder(biLength);

            } else if (start && !head && readable >= lengthHead) {

                head = true;

                byte[] bytesHead = new byte[lengthHead];

                composite.readBytes(bytesHead);

                inPack = new BytePack(bytesHead);

                if (inPack.getLengthData() == 0){

                    start = false;
                    head = false;

                    channel.fireChannelRead(inPack);
                }

            } else if (start && head && inPack.getLengthData() > 0 && readable >= inPack.getLengthData()) {

                byte[] data = new byte[inPack.getLengthData()];

                composite.readBytes(data);

                inPack.setData(data);

                start = false;
                head = false;

                channel.fireChannelRead(inPack);

            } else {
                break;
            }

            if(composite.readableBytes() == 0) composite = ByteBufAllocator.DEFAULT.compositeBuffer();
        }
    }


}
