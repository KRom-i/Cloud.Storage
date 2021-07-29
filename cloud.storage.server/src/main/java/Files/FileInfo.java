package Files;

import Commands.Commands;
import FileManager.*;

import java.io.File;


public class FileInfo {

    private String pathServer;
    private String name;
    private String type;
    private long dateModified;
    private long size;
    private boolean searchResults;

    public FileInfo (File file, String pathServer, boolean searchResults) {

        if (file.isDirectory()){
            this.name = file.getName();
            this.type = "DIR";
        } else {
            this.name = new FileManager().getFileNameNotExtension(file);
            this.type = new FileManager().getFileExtension(file);
            this.size = file.length();
        }
        this.dateModified = file.lastModified();
        this.pathServer = pathServer + File.separator + file.getName();
        this.searchResults = searchResults;

    }


    @Override
    public String toString () {
        return Commands.BUILD(0, String.valueOf(pathServer), name, type, String.valueOf(size),
                              String.valueOf(dateModified), String.valueOf(searchResults));
    }


    

}





