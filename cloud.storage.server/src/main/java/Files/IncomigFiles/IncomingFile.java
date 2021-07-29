package Files.IncomigFiles;

import Commands.Commands;

import java.io.File;

public class IncomingFile  {

    private String fromUser;
    private String name;
    private long size;


    private String fullName;

    private boolean download;
    private boolean delete;

    public IncomingFile (File file) {
        String[] fileName = file.getName().split(Commands.DELIMITER[3]);
        this.fromUser = fileName[0];
        this.name = fileName[1];
        this.size = file.length();


    }

    public IncomingFile (String msg) {
        String[] m = msg.split(Commands.DELIMITER[1]);

        this.name = m[0];
        this.fromUser = m[1];
        this.fullName = fromUser + Commands.DELIMITER[3] + name;
        this.download = Boolean.parseBoolean(m[2]);
        this.delete = Boolean.parseBoolean(m[3]);

    }

    public String getName () {
        return name;
    }

    public String getFullName () {
        return fullName;
    }

    public boolean isDownload () {
        return download;
    }

    public boolean isDelete () {
        return delete;
    }

    public String getCmd (){
        return Commands.BUILD(0, Commands.INCOMING, name, String.valueOf(size), fromUser);
    }

    @Override
    public String toString() {
        return String.format("IncomingFile:%s:%s:%s:%s", name, size, fromUser );
    }
}
