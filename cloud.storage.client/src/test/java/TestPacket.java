import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class TestPacket {

    InputStream inputStream;
    OutputStream outputStream;

    public static void main (String[] args) {
        System.out.println("admin".hashCode());
    }


    public TestPacket () {



    }

    public static String getPacket(String msg){
        return lineLengthToString(msg) + msg;
    }

    public static String lineLengthToString(String msg){
        return String.format("%010d", msg.length());
    }









}
