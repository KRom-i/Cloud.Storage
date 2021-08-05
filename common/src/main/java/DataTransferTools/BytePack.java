package DataTransferTools;

import Commands.Commands;
import java.nio.charset.StandardCharsets;


public class BytePack {

    private byte[] biLength;
    private byte[] head;
    private byte[] data;

    private String msg;

    private int address1;
    private int address2;
    private int lengthData;


    public BytePack(int address1, int address2, String msg, byte[] data){
        this.address1 = address1;
        this.address2 = address2;
        initHead(msg, data);
        encodeHead();
        this.biLength = BinarySystem.integerEncoder(head.length);
    }

    public BytePack(byte[] head){
        this.head = head;
        decodeHead();
    }

    private void initHead(String msg, byte[] data){
        if (msg == null){
            this.msg = "";
        } else {
            this.msg = msg;
        }

        if (data == null){
            this.lengthData = 0;
        } else {
            this.data = data;
            this.lengthData = data.length;
        }

    }

    private void encodeHead(){
        this.head = Commands.BUILD(1,
                                   String.valueOf(address1),
                                   String.valueOf(address2),
                                   String.valueOf(lengthData),
                                   msg).getBytes(StandardCharsets.UTF_8);
    }

    private void decodeHead(){
        try {
            String[] h = new String(head, "UTF-8").split(Commands.DELIMITER[1]);
            this.address1 = Integer.parseInt(h[0]);
            this.address2 = Integer.parseInt(h[1]);
            this.lengthData = Integer.parseInt(h[2]);
            if (h.length > 3){
                this.msg = h[3];
            } else {
                this.msg = "";
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public byte[] getBiLength () {
        return biLength;
    }

    public byte[] getHead () {
        return head;
    }

    public int getLengthData () {
        return lengthData;
    }

    public int getAddress1 () {
        return address1;
    }

    public int getAddress2 () {
        return address2;
    }

    public String[] getMsg () {
        return msg.split(Commands.DELIMITER[0]);
    }

    public byte[] getData () {
        return data;
    }

    public void setData (byte[] data) {
        this.data = data;
    }


    @Override
    public String toString () {

        String str = "";
        try {
            str = " *** Byte PACK *** \n" +
                    " --- address1=" + address1 +
                    " --- address2=" + address2 +
                    " --- lengthData=" + lengthData;

            if (biLength != null) str +=  " --- biLength=" + BinarySystem.integerDecoder(biLength);

            str += "\n ** HEAD = " + new String(head, "UTF-8");;

            if(data != null) {
            }
            str += "\n ** DATA ="  + new String(data, "UTF-8");

        } catch (Exception e){}

        return str;
    }
}
