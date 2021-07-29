package Files.FileTransfer;

import Client.*;
import Commands.Commands;
import Configuration.Config;
import javafx.scene.layout.HBox;
import FileManager.*;
import java.io.*;
import java.util.List;

public class FileTransfer extends HBox{

    byte[] buffer;

    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    public FileTransfer (DataInputStream dataInputStream, DataOutputStream dataOutputStream) {

        this.dataOutputStream = dataOutputStream;
        this.dataInputStream = dataInputStream;
        this.buffer =  new byte[Config.BUFFER_SIZE];

    }

    public void uploadListFileTran (Client client) throws IOException {


        while (true) {

            String msg = dataInputStream.readUTF();

            TransportedFile transportedFile = new TransportedFile(msg.split(Commands.DELIMITER[0])[2]);

            client.setUploadPath(transportedFile);

            long size = transportedFile.getSize();

            File file = getFileFromTransportedFile(transportedFile);

            FileOutputStream fos = new FileOutputStream(file);

                for(int i = 0; i < (size + (buffer.length - 1)) / (buffer.length); i++) {
                    int read = dataInputStream.read(buffer);
                    fos.write(buffer, 0, read);
                }

            fos.close();

            transportedFile.setByteRead(file.length());

            dataOutputStream.writeUTF(transportedFile.getUploadResponse());

            if (!dataInputStream.readBoolean()) break;

        }

    }


    private File getFileFromTransportedFile(TransportedFile tf){

        File inFile = null;

        try {
            String writeDir = tf.getPath();

            if(tf.isMkdirs()) writeDir = writeDir + File.separator + tf.getPathDirs();

            File dir = new File(writeDir);

            if(!dir.exists()) dir.mkdirs();

            String fileName = tf.getFileName();

            inFile = new File(dir + File.separator + fileName);

            if(inFile.exists()) {
                String path = new FileManager().checkFile(inFile.getAbsolutePath());
                inFile = new File(path);
                inFile.createNewFile();

            } else {
                inFile.createNewFile();
            }

        }   catch (Exception e){
            e.printStackTrace();
        }

        return inFile;
    }

    public void downloadListFileTran (Client client) {

        try {

            boolean next = true;

            while (next){

            String msgIn = dataInputStream.readUTF();

            TransportedFile transportedFile = new TransportedFile(msgIn.split(Commands.DELIMITER[0])[2]);

            List<TransportedFile> transportedFileList = client.getTransportedFileList(transportedFile);


                String msgOut;


               int files  =  transportedFileList.size();

                dataOutputStream.writeInt(files);

                for (int i = 0; i < files; i++) {

                    msgOut = transportedFileList.get(i).getPaketDownload();

                    dataOutputStream.writeUTF(msgOut);

                    File file = transportedFileList.get(i).getFile();

                    FileInputStream fis = new FileInputStream(file);

                    int read = 0;

                    while ((read = fis.read(buffer)) != -1) {
                        dataOutputStream.write(buffer, 0, read);
                    }

                    fis.close();

                    dataOutputStream.flush();

                }

                next = dataInputStream.readBoolean();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
