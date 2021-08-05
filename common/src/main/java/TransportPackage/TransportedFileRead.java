package TransportPackage;

import Commands.Commands;

import java.io.FileInputStream;

public class TransportedFileRead {

    private int maxBuf;
    private String msg;

    public TransportedFileRead (int maxBuf) {
        this.maxBuf = maxBuf;
    }

    public byte[] getDataFile(TransportedFile transportedFile){

        msg = Commands.BUILD(0,
                             String.valueOf(transportedFile.getIdPack()),
                             String.valueOf(transportedFile.getId()));

        return read(transportedFile);
    }

    public String getMsg(){
        return msg;
    }

    private byte[] read(TransportedFile tranFile ){

        byte[] buffer = null;

        try (FileInputStream fis = new FileInputStream(tranFile.getFile())) {

            long read = tranFile.getByteRead();
            long sizeFile = tranFile.getSize();

            fis.getChannel().position(read);

            int sizeBuf;

            if ((sizeFile - read) < maxBuf){
                sizeBuf = (int) (sizeFile - read);
            } else {
                sizeBuf = maxBuf;
            }

            buffer = new byte[sizeBuf];

            fis.read(buffer);

            tranFile.setByteRead(buffer.length);


        } catch (Exception e) {
            e.printStackTrace();
        }


        return buffer;
    }
}
