package TransportPackage;

import Commands.Commands;
import DataTransferTools.BytePack;


import java.io.File;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

public class TransportPackage implements Serializable, Cloneable {

    private int id;
    private String userCommand;
    private String pathServer;
    private String namePack;
    private String pathDownload;
    private String pathUpload;
    private int numFiles;
    private LinkedHashMap<Integer, TransportedFile> transportedFileMap;
    private boolean upload;
    private boolean loaded;
    private boolean directory;
    private long size;

    public TransportPackage () {
    }

    public TransportPackage (File file, boolean upload, int id)  {

        this.id = id;
        this.namePack = file.getName();
        this.upload = upload;
        this.loaded = false;

        initMap(file);
    }

    private void initMap(File file){

        if (file.isDirectory()){
            this.directory = true;
            this.transportedFileMap = new RunFile(file, id).getTransportedFiles();
        } else {
            this.directory = false;
            this.transportedFileMap = new LinkedHashMap<>(1);
            TransportedFile transportedFile = new TransportedFile(file, 1, id);
            this.transportedFileMap.put(transportedFile.getId(), transportedFile);
        }
        this.size = getSizeFiles();
        this.numFiles = transportedFileMap.size();
    }

    public LinkedHashMap<Integer, TransportedFile> getTransportedFileMap () {
        return transportedFileMap;
    }

    public void setTransportedFileMap (LinkedHashMap<Integer, TransportedFile> transportedFileMap) {
        this.transportedFileMap = transportedFileMap;
    }

    public TransportPackage (String namePack, boolean upload, String pathServer, String pathDownload, int id) {
        this.id = id;
        this.namePack = namePack;
        this.upload = upload;
        this.pathServer = pathServer;
        this.loaded = false;
        this.pathDownload = pathDownload;
    }


    public void setLoaded (boolean loaded) {
        this.loaded = loaded;
    }

    public int getNumFiles () {
        return numFiles;
    }

    public boolean isUpload () {
        return upload;
    }

    public boolean isLoaded () {
        return loaded;
    }

    public long getSizeFiles() {

        long size = 0;

        for (int i = 0; i < transportedFileMap.size(); i++) {
            size += transportedFileMap.get(i + 1).getSize();
        }

        return size;
    }

    public String getNamePack () {
        return namePack;
    }

    public boolean isDirectory () {
        return directory;
    }

    public long getSize () {
        return size;
    }

    public void setNamePack (String namePack) {
        this.namePack = namePack;
    }


    public String getPathServer () {
        return pathServer;
    }

    public String getPathUpload () {
        return pathUpload;
    }

    public String getPathDownload () {
        return pathDownload;
    }

    public String getUserCommand () {
        return userCommand;
    }

    public void setUserCommand (String userCommand) {
        this.userCommand = userCommand;
    }

    public String packLoad(){
        return Commands.LOAD;
    }

    public String packExist(){
        return Commands.EXIST;
    }

    public String packRename(String name){
        return Commands.BUILD(0, Commands.RENAME, name);
    }

    public String packRename(){
        return Commands.RENAME;
    }

    public String packCopy(){
        return Commands.COPY;
    }

    public String packReplace(){
        return Commands.REPLACE;
    }

    public TransportedFile getTransportedFile (int idFile){
        return transportedFileMap.get(idFile);
    }

    @Override
    protected Object clone () throws CloneNotSupportedException {
        return super.clone();
    }

    public int getId () {
        return id;
    }

    @Override
    public String toString () {
        return "TransportPackage{" +
                "id=" + id +
                ", userCommand='" + userCommand + '\'' +
                ", pathServer='" + pathServer + '\'' +
                ", namePack='" + namePack + '\'' +
                ", pathDownload='" + pathDownload + '\'' +
                ", pathUpload='" + pathUpload + '\'' +
                ", numFiles=" + numFiles +
                ", transportedFileMap=" + transportedFileMap +
                ", upload=" + upload +
                ", loaded=" + loaded +
                ", directory=" + directory +
                ", size=" + size +
                '}';
    }


    public TransportPackage getClone() throws CloneNotSupportedException {

        TransportPackage transportPackage = (TransportPackage) clone();

        LinkedHashMap<Integer, TransportedFile> linkedHashMapClone = new LinkedHashMap<>();

        for (int i = 0; i < transportPackage.getNumFiles(); i++) {

            TransportedFile transportedFile = (TransportedFile) transportedFileMap.get(i + 1).clone();

            transportedFile.setFile(null);

            linkedHashMapClone.put(transportedFile.getId(), transportedFile);
        }

        transportPackage.setTransportedFileMap(linkedHashMapClone);

        return transportPackage;
    }
}
