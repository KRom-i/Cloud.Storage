package TransportPackage;

import Commands.Commands;
import DataTransferTools.BytePack;


import java.io.File;
import java.io.Serializable;

public class TransportedFile implements Serializable, Cloneable {

    private int idPack;
    private int id;
    private String path;
    private String pathDirs;
    private String fileName;

    private long size;
    private long byteRead;

    private File file;

    private boolean mkdirs;


    public TransportedFile (File file, int id, int idPack) {
        this.idPack = idPack;
        this.id = id;
        this.fileName = file.getName();
        this.file = file;
        this.size = file.length();
    }

    public TransportedFile(File file, String pathDir,  int id, int idPack){
        this.idPack = idPack;
        this.id = id;
        this.fileName = file.getName();
        this.file = file;
        this.size = file.length();
        this.mkdirs = (pathDir != null);
        this.pathDirs = pathDir;
    }

    public int getId () {
        return id;
    }

    public int getIdPack () {
        return idPack;
    }

    public File getFile () {
        return file;
    }

    public long getByteRead () {
        return byteRead;
    }

    public void setByteRead (int read) {
        this.byteRead += read;
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

    @Override
    public String toString () {
        return "TransportedFile{" +
                "idPack=" + idPack +
                ", id=" + id +
                ", path='" + path + '\'' +
                ", pathDirs='" + pathDirs + '\'' +
                ", fileName='" + fileName + '\'' +
                ", size=" + size +
                ", byteRead=" + byteRead +
                ", file=" + file +
                ", mkdirs=" + mkdirs +
                '}';
    }

    public void setFile (File file) {
        this.file = file;
    }

    @Override
    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }


}
