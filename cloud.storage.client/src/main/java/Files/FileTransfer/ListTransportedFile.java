package Files.FileTransfer;

import Configuration.Config;
import Commands.Commands;
import FileManager.FileManager;
import FileManager.WalkFile;
import GraphicalIinterface.CommonСlassesGUI.ListFiles.FileInfo;
import GraphicalIinterface.GUIBoxes.CloudBox.Cloud;
import GraphicalIinterface.MainUserInterface.MainBox;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class ListTransportedFile {



    private Cloud cloud;

    byte[] bufferOut;
    byte[] bufferIn;

    private ExecutorService executorService;
    private MainBox mainBox;

    public ListTransportedFile (Cloud cloud, MainBox mainBox) {
        this.executorService = cloud.getExecutorService();
        this.cloud = cloud;
        this.bufferOut = new byte[Config.BUFFER_SIZE];
        this.bufferIn = new byte[Config.BUFFER_SIZE];
        this.mainBox = mainBox;
    }

    public void add (List<TransportedFile> transportedFileList) {
        transportedFileList.addAll(transportedFileList);
    }


    public void getFiles (List<FileInfo> bufListFileInfo, boolean delete, String path, Cloud cloud) {

        List<TransportedFile> transportedFileListDownload = new ArrayList<>();

        for(FileInfo file: bufListFileInfo
            ) {
            transportedFileListDownload.add(new TransportedFile(file, cloud, path, delete, 0));
        }


        executorService.execute(new DownloadTransportedFile(transportedFileListDownload));
    }




    public List<TransportedFile> getTransportedFileList (String path, Cloud cloud, File... files) {

        List<WalkFile> walkFiles = new FileManager().getWalkFiles(files);

        List<TransportedFile> transportedFileList = new ArrayList<>();

            if(walkFiles.isEmpty()) return null;

            for(WalkFile wf : walkFiles) {
                if(!wf.isDirectory()) transportedFileList.add(new TransportedFile(wf, path, cloud,true));
            }


        return transportedFileList;
    }

    public File[] getArrayFilesFromListFileInfo(List<FileInfo> fileInfoList){

        File[] files = new File[fileInfoList.size()];

        for(int i = 0; i < files.length; i++) {

            files[i] = fileInfoList.get(i).getFile();
        }
        return files;
    }


    public void outFiles (String path, List<FileInfo> fileInfoList, long maxSize) {

        File[] files = getArrayFilesFromListFileInfo(fileInfoList);

        List<TransportedFile> uploadList = getTransportedFileList(path, cloud, files);


        long sizePAck = 0;

        for (TransportedFile tranFile: uploadList
             ) {
            sizePAck += tranFile.getSize();
        }

        if (sizePAck < maxSize ) {

           executorService.execute(new UploadTransportedFile(uploadList));

        } else {
            cloud.addText("Недостаточно места на облачном хранении");

        }


        }


    public void getFile (FileInfo file, Cloud cloud, int type) {

        File dir = new File("~download");

        if (!dir.exists()) dir.mkdirs();

        TransportedFile transportedFile = new TransportedFile(file, cloud, dir.getAbsolutePath(), false, type);

        List<TransportedFile> transportedFileListDownload = new ArrayList<>();

        transportedFileListDownload.add(transportedFile);

        executorService.execute(new DownloadTransportedFile(transportedFileListDownload));

    }



    private class UploadTransportedFile implements Runnable {

        private List<TransportedFile> transportedFileList;


        public UploadTransportedFile (List<TransportedFile> transportedFileList) {
            this.transportedFileList = transportedFileList;
        }

        @Override
        public void run () {

            try (Socket socket = new Socket(Config.HOST, Config.PORT);
                 DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                 DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream())
            ) {

                String msgOut = cloud.getUploadMsg();

                dataOutputStream.writeUTF(msgOut);

                boolean connect;

                connect = dataInputStream.readBoolean();


                while (connect) {

                    TransportedFile transportedFile = transportedFileList.get(0);

                    transportedFileList.remove(transportedFile);

                    transportedFile.setTransported(true);

                    msgOut = transportedFile.getPaketUpload();

                    dataOutputStream.writeUTF(msgOut);

                    FileInputStream fis = new FileInputStream(transportedFile.getFile());

                    int read = 0;

                    while ((read = fis.read(bufferOut)) != -1) {
                        dataOutputStream.write(bufferOut, 0, read);
                    }


                    fis.close();

                    dataOutputStream.flush();


                    String msgIn = dataInputStream.readUTF();


                    transportedFile.response(msgIn.split(Commands.DELIMITER[0])[2]);

                    mainBox.updateClouds(cloud);

                    cloud.addText(transportedFile.getFileName() + " загружен");


                    if(transportedFileList.isEmpty()) connect = false;


                    dataOutputStream.writeBoolean(connect);

                }


            } catch (Exception e){

                e.printStackTrace();


            }

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


    private class DownloadTransportedFile implements Runnable{

        private List<TransportedFile> transportedFileListDownload;

        public DownloadTransportedFile (List<TransportedFile> transportedFileListDownload) {
            this.transportedFileListDownload = transportedFileListDownload;
        }

        @Override
        public void run () {

            try (Socket socket = new Socket(Config.HOST, Config.PORT);
                 DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                 DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())
            ) {

                String msgOut = cloud.getDownloadMsg();

                dataOutputStream.writeUTF(msgOut);

                boolean connect = dataInputStream.readBoolean();

                while (connect) {

                    TransportedFile transportedFile = transportedFileListDownload.get(0);

                    msgOut = transportedFile.getPaketDownload();

                    transportedFileListDownload.remove(transportedFile);

                    dataOutputStream.writeUTF(msgOut);

                    int numFiles = dataInputStream.readInt();

                    for (int f = 0; f < numFiles; f++) {

                        String msgIN = dataInputStream.readUTF();

                        TransportedFile transportedFileDownload = new TransportedFile(msgIN.split(Commands.DELIMITER[0])[2]);

                        long size = transportedFileDownload.getSize();

                        transportedFileDownload.setPath(transportedFile.getPathDownload());

                        File file = getFileFromTransportedFile(transportedFileDownload);

                        FileOutputStream fos = new FileOutputStream(file);

                        for (int i = 0; i < (size + (bufferIn.length - 1)) / (bufferIn.length); i++) {
                            int read = dataInputStream.read(bufferIn);
                            fos.write(bufferIn, 0, read);
                        }

                        fos.close();


                        if(transportedFile.getTypeFile() > 0) {
                            switch (transportedFile.getTypeFile()) {
                                case 1:
                                    cloud.openBoxShowTextFile(file);
                                    break;
                                case 2:
                                    cloud.openBoxShowImgFile(file);
                                    break;
                                case 3:
                                    cloud.openBoxShowMediaFile(file);
                                    break;
                            }
                        } else {
                            cloud.addText(transportedFile.getFileName() + " скачан");
                        }

                        mainBox.updateClouds(cloud);
                        mainBox.updateUser(transportedFile.getPathDownload());

                    }


                    if(transportedFileListDownload.size() > 0) {
                        transportedFile = transportedFileListDownload.get(0);

                    } else {
                        connect = false;
                    }


                    dataOutputStream.writeBoolean(connect);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }

}