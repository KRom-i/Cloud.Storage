package Client;

import Archive.ArhZipFile;
import Configuration.Config;
import DataBase.DataClients;
import FileManager.FileManager;
import Files.FileTransfer.FileTransfer;
import Commands.Commands;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Clients {

    private List<Client> clients;

    public Clients () {
       clients = new ArrayList<>();
    }

    public void add(Client client){
        clients.add(client);
    };

    public void remove(Client client){
        clients.remove(client);

    };

    public String getCheckNick (String nick) {
        return Commands.BUILD(0, Commands.NICK, Commands.CHECK, nick,
                              String.valueOf(checkNick(nick)));
    }

    private boolean checkNick(String nick){
        return !new DataClients().checkUnique("nickNameUser", nick);
    }

    public void upload (String msg, DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {

        Client client = getClientById(Integer.parseInt(msg));

        boolean connect = (client != null);

        dataOutputStream.writeBoolean(connect);

        if (connect) new FileTransfer(dataInputStream, dataOutputStream).uploadListFileTran(client);

    }

    public void download (String msg, DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {

        Client client = getClientById(Integer.parseInt(msg));

        boolean connect = (client != null);

        dataOutputStream.writeBoolean(connect);

        if (connect) new FileTransfer(dataInputStream, dataOutputStream).downloadListFileTran(client);

    }


    private Client getClientById (int idClient) {
        for(Client c: clients
            ) {
            if (c.getIdClient() == idClient) return c;
        }
        return null;
    }

    private Client getClientByNick (String nick) {

        for(Client c: clients
        ) {
            if (c.getName().equals(nick)) return c;
        }

        return null;
    }

    public void setFileNick(Client clientSender, String path, String nick){

        File dir = new File(Config.ROOT_SERVER_DIR + File.separator + Config.ROOT_BY_USER_DIR + File.separator + nick);

        if (!dir.exists()) dir.mkdirs();

        File src = new File(clientSender.getRootFullAbsolutePath() + File.separator + path);

        String nameFile = String.format("%s%s%s", clientSender.getName(), Commands.DELIMITER[3], src.getName());

        new ArhZipFile().packUser(dir, nameFile, src);

        Client clientRecipient = getClientByNick(nick);

        if (clientRecipient == null) return;

        clientRecipient.update();


    };





}
