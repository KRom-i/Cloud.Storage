package Files.FileTransfer;

import Commands.Commands;
import FileManager.WalkFile;
import GraphicalIinterface.CommonСlassesGUI.ListFiles.FileInfo;
import GraphicalIinterface.GUIBoxes.CloudBox.Cloud;
import java.io.File;


public class TransportedFile {

        private int idFile;
        private int idClient;
        private int root;

        private String path;
        private String pathDirs;
        private String fileName;
        private String pathDownload;

        private long size;
        private long byteRead;

        private File file;

        private boolean transported;
        private boolean upload;
        private boolean mkdirs;
        private boolean delete;

        private int typeFile;



    public TransportedFile(){}

    public TransportedFile (File file, String path, Cloud cloud) {
        this.idClient = cloud.getIdCloudClient();
        this.root = cloud.getCurRootInt();
        this.path = path;
        this.pathDirs = null;
        this.fileName = file.getName();
        this.size = file.length();
        this.file = file;
        this.upload = true;
        this.byteRead = 0;
        this.idFile = 0;
    }


    public TransportedFile (WalkFile walkFile, String path, Cloud cloud, boolean upload) {
        this.idClient = cloud.getIdCloudClient();
        this.root = cloud.getCurRootInt();
        this.path = path;
        this.pathDirs = walkFile.getPathDirs();
        this.fileName = walkFile.getFullName();
        this.size = walkFile.getFile().length();
        this.file = walkFile.getFile();
        this.upload = upload;
        this.byteRead = 0;
        this.idFile = 0;
    }

    public TransportedFile (FileInfo fileInfo, Cloud cloud, String pathDownload , boolean delete, int typeFile){
        this.idClient = cloud.getIdCloudClient();
        this.root = cloud.getCurRootInt();
        this.path = fileInfo.getPathServer();
        this.fileName = fileInfo.getFullName();
        this.size = fileInfo.getSize();
        this.upload = false;
        this.byteRead = 0;
        this.root = cloud.getCurRootInt();
        this.pathDownload = pathDownload;
        this.delete = delete;
        this.typeFile = typeFile;
    }


    public TransportedFile (String msg) {
        String[] m = msg.split(Commands.DELIMITER[1]);
        this.idClient = Integer.parseInt(m[0]);
        this.size = Long.parseLong(m[1]);
        this.root = Integer.parseInt(m[2]);
        this.path = m[3];
        this.pathDirs = m[4];
        this.fileName = m[5];
        this.byteRead = Long.parseLong(m[6]);
        this.idFile = Integer.parseInt(m[7]);
        this.transported = Boolean.parseBoolean(m[8]);
        this.upload = Boolean.parseBoolean(m[9]);
        this.delete = Boolean.parseBoolean(m[10]);
        this.pathDownload = m[11];
        this.mkdirs = (!"null".equals(pathDirs));
    }


    public TransportedFile response(String msg) {
        TransportedFile tf = new TransportedFile();
        String[] m = msg.split(Commands.DELIMITER[1]);
        tf.idClient = Integer.parseInt(m[0]);
        tf.size = Long.parseLong(m[1]);
        tf.root = Integer.parseInt(m[2]);
        tf.path = m[3];
        tf.pathDirs = m[4];
        tf.fileName = m[5];
        tf.byteRead = Long.parseLong(m[6]);
        tf.idFile = Integer.parseInt(m[7]);
        tf.transported = Boolean.parseBoolean(m[8]);
        tf.upload = Boolean.parseBoolean(m[9]);
        tf.delete = Boolean.parseBoolean(m[10]);
        tf.pathDownload = m[11];
        tf.mkdirs = (!"null".equals(pathDirs));
        return tf;
    }


    public String getPaketUpload(){
        this.transported = true;
        return Commands.BUILD(0, Commands.LOAD, Commands.UP,
                                 Commands.BUILD(1,
                                                String.valueOf(idClient),
                                                String.valueOf(size),
                                                String.valueOf(root),
                                                path,
                                                pathDirs,
                                                fileName,
                                                String.valueOf(byteRead),
                                                String.valueOf(idFile),
                                                String.valueOf(transported),
                                                String.valueOf(upload),
                                                String.valueOf(delete),
                                                pathDownload)
                                 );
    }

    public String getPaketDownload(){
        return Commands.BUILD(0, Commands.LOAD, Commands.DOWN,
                              Commands.BUILD(1,
                                             String.valueOf(idClient),
                                             String.valueOf(size),
                                             String.valueOf(root),
                                             path,
                                             pathDirs,
                                             fileName,
                                             String.valueOf(byteRead),
                                             String.valueOf(idFile),
                                             String.valueOf(transported),
                                             String.valueOf(upload),
                                             String.valueOf(delete),
                                             pathDownload)
        );
    }


    public int getTypeFile () {
        return typeFile;
    }

    public File getFile () {
        return file;
    }


    public String getPathDownload () {
        return pathDownload;
    }

    public void setFullPath (int root, String path) {
        this.root = root;
        this.path = path;
    }

    public int getId () {
        return idFile;
    }

    public long getByteRead () {
        return byteRead;
    }

    public void setByteRead (long byteRead) {
        this.byteRead = byteRead;
    }

    public boolean isUpload () {
        return upload;
    }

    public void setId (int id) {
        this.idFile = id;
    }

    public void setPath (String path) {
        this.path = path;
    }

    public String getPath () {
        return path;
    }

    public String getPathDirs () {
        return pathDirs;
    }

    public String getFileName () {
        return fileName;
    }

    public long getSize () {
        return size;
    }

    public boolean isMkdirs () {
        return mkdirs;
    }

    public boolean isTransported () {
        return transported;
    }

    public void setTransported (boolean transported) {
        this.transported = transported;
    }

    public void setDelete(boolean value){
        this.delete = value;
    }

    @Override
    public String toString () {
        return "TransportedFile{" +
                "idFile=" + idFile +
                ", idFile=" + idFile +
                ", root=" + root +
                ", path='" + path + '\'' +
                ", pathDirs='" + pathDirs + '\'' +
                ", fileName='" + fileName + '\'' +
                ", size=" + size +
                ", byteRead=" + byteRead +
                ", file=" + file +
                ", transported=" + transported +
                ", upload=" + upload +
                ", mkdirs=" + mkdirs +
                ", delete=" + delete +
                ", pathDownload=" + pathDownload +
                '}';
    }
}
