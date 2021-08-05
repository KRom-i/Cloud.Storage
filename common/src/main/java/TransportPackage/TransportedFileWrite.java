package TransportPackage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TransportedFileWrite {


    public void write(TransportedFile transportedFile, byte[] data){

        File file = transportedFile.getFile();

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try(FileOutputStream fos = new FileOutputStream(file, true)){

            fos.write(data, 0, data.length);

            transportedFile.setByteRead(data.length);


        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
