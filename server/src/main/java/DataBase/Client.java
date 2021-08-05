package DataBase;

import Commands.Commands;
import Files.FileStorage;

public class Client {

    private int id;
    private String name;
    private FileStorage fileStorage;


    public Client (int id, String name) {
        this.name = name;
        this.id = id;
        this.fileStorage = new FileStorage(name);
    }

    public FileStorage getFileStorage () {
        return fileStorage;
    }

    public String getPackAuth () {
        return Commands.BUILD(0, String.valueOf(true), name);
    }
}
