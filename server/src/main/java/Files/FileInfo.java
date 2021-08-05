package Files;

import Commands.Commands;
import FileManager.FileManager;
import java.io.File;
import java.io.Serializable;

public class FileInfo implements Serializable {

    private File file;
    private String pathServer;
    private String name;
    private String type;
    private long size;
    private long dateModified;
    private boolean server;
    private boolean directory;
    private boolean searchResults;


    public FileInfo (File file, String pathServer, boolean searchResults) {

        this.file = file;
        this.dateModified = file.lastModified();
        this.pathServer = pathServer + File.separator + file.getName();
        this.searchResults = searchResults;
        initFields();
    }

    private void initFields(){

        if (file.isDirectory()){
            this.name = file.getName();
            this.type = "DIR";
        } else {
            this.name = new FileManager().getFileNameNotExtension(file);
            this.type = new FileManager().getFileExtension(file);
            this.size = file.length();
        }
    }


    public String getInfoPack(){
        return Commands.BUILD(0, pathServer,name,type, String.valueOf(size),
                              String.valueOf(dateModified), String.valueOf(searchResults));
    }

    public static String getHeadPack (int length) {
        return Commands.BUILD(0, Commands.FILE, Commands.LIST, String.valueOf(length));
    }
}
