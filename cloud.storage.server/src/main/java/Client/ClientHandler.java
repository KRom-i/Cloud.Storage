package Client;

import Configuration.Config;
import DataBase.DataClients;
import Logger.Log;
import Server.Server;
import java.io.*;
import java.net.Socket;
import Commands.*;



public class ClientHandler implements Runnable {

    private final Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private Client client;
    private Clients clients;

    public ClientHandler (Server server, Socket socket) throws IOException {
        this.socket = socket;
        this.out = new DataOutputStream(socket.getOutputStream());
        this.in = new DataInputStream(socket.getInputStream());
        this.clients = server.getClients();
    }

    //    Обработка команд от клиента
    @Override
    public void run () {

                Log.info(String.format("CLIENT CONNECTED { InetAddress=%s } {PORT=%s} {PORT_SERVER=%s}",
                                       socket.getInetAddress(), socket.getPort(), Config.PORT));

        try {

                while (true) {

                    String[] command = getMessage().split(Commands.DELIMITER[0]);

                    if(Commands.AUTH.equals(command[0])) {

                        if(Commands.CHECK.equals(command[1])) {

                            if(Commands.USER.equals(command[2])) {

                                client = new DataClients().checkLoginAndPass(command[3], Integer.parseInt(command[4]));

                                if(client != null) {
                                    clients.add(client);

                                    client.authorization(this);


                                } else {
                                    sendMessage(Commands.BUILD(0, Commands.AUTH, Commands.CHECK,
                                                               Commands.INFO, String.valueOf(-3)));
                                }

                            } else if(Commands.ADMIN.equals(command[2]) && client.isAuth()) {
                                    client.checkAdmin(command[3]);


                            } else if (Commands.NICK.equals(command[2]) && client.isAuth()){
                                sendMessage(clients.getCheckNick(command[3]));
                            }

                        } else if(Commands.ADD.equals(command[1])) {
                             if(Commands.USER.equals(command[2])) {

                                 String nick = command[3];
                                 String log = command[4];
                                 int pass = Integer.parseInt(command[5]);

                                int check = new DataClients().addNewClient(nick, log, pass);

                                sendMessage(Commands.BUILD(0, Commands.AUTH, Commands.CHECK,
                                                           Commands.INFO, String.valueOf(check)));


                            }

                        } else if(Commands.EXIT.equals(command[1])) {
                            client.close();
                            break;
                        }

                    } else if (client != null) {

                        if (client.isAuth()) {

                            if(Commands.CURRENT_DIRECTORY.equals(command[0])) {

                                if(Commands.UP.equals(command[1])) {
                                    client.currentDirectoryUp(command[2]);

                                } else if(Commands.DOWN.equals(command[1])) {
                                    client.currentDirectoryDown();

                                } else if(Commands.DIRECTORY_HOME.equals(command[1])) {
                                    client.setCurrentDirectory(null);

                                } else if(Commands.SET.equals(command[1])) {
                                    client.setCurrentDirectory(command[2]);

                                } else if(Commands.ROOT.equals(command[1])) {
                                    if(Commands.SET.equals(command[2])) client.setRoot(command[3]);

                                } else if(Commands.SIZE.equals(command[1])) {
                                    client.setCurrentMaxSize(command[2]);

                                }

                            } else if(Commands.FILE.equals(command[0])) {

                                if(Commands.CREATE_FILE.equals(command[1])) {
                                    client.createFile(command[2]);

                                } else if(Commands.CREATE_DIR.equals(command[1])) {
                                    client.createDirectory(command[2]);

                                } else if(Commands.DELETE.equals(command[1])) {
                                    client.delete(command[2]);

                                } else if(Commands.RENAME.equals(command[1])) {
                                    client.renameFile(command[2], command[3]);

                                } else if(Commands.COPY.equals(command[1])) {
                                    client.copy(command[2], command[3]);

                                } else if(Commands.ZIP.equals(command[1])) {
                                    client.zip(command[2], command[3], command[4], command[5]);

                                } else if(Commands.UNZIP.equals(command[1])) {
                                    client.unzip(command[2], command[3], command[4], command[5]);

                                } else if(Commands.LIST.equals(command[1])) {

                                    if(Commands.GET.equals(command[2])) {
                                        client.update();

                                    } else if(Commands.SEARCH.equals(command[2])) {
                                        client.getSearchList(command[3]);
                                    }

                                } else if(Commands.INCOMING.equals(command[1])) {

                                    if(Commands.LIST.equals(command[2])) {
                                        client.sendListIncomingFiles();

                                    } if(Commands.GET.equals(command[2])) {
                                        client.getIncomingFile(command[3]);
                                    }

                                } else if(Commands.NICK.equals(command[1])) {

                                    if(Commands.COPY.equals(command[2])) {
                                        clients.setFileNick(client, command[3], command[4]);

                                    } else if (Commands.LOAD.equals(command[2])) {

                                    }
                                }
                            }

                        }

                    }



                    if (Commands.LOAD.equals(command[0])) {

                            if(Commands.UP.equals(command[1])) {
                                clients.upload(command[2], in, out);


                            } else if(Commands.DOWN.equals(command[1])) {
                                clients.download(command[2],  in, out);
                            }

                            break;
                    }


                }

        } catch(Exception e) {
            e.printStackTrace();

        } finally {

            if (client != null) {
                clients.remove(client);
            }

            Log.info(String.format("CLIENT DISCONNECTED { InetAddress=%s } { PORT=%s } {PORT_SERVER=%s}",
            socket.getInetAddress(), socket.getPort(), Config.PORT));
            close();
        }


        }



    // Отправка сообщения в цикле клиенту в формате UTF-8
    public void sendMessage(String... strings) {
        for (String msg: strings) {
            sendMessage(msg);
        }
    }

    // Отправка сообщения клиенту в формате UTF-8
    public void sendMessage (String massage)  {
        try {
            out.writeUTF(massage);
            Log.info(String.format("CLIENT OUT MSG { InetAddress=%s } { PORT=%s }  {PORT_SERVER=%s}\n %s",
                                   socket.getInetAddress(), socket.getPort(), Config.PORT, massage));
        } catch (Exception e) {
            Log.error(String.format("CLIENT OUT MSG { InetAddress=%s } { PORT=%s } {PORT_SERVER=%s}\n %s\n",
                                   socket.getInetAddress(), socket.getPort(), Config.PORT, massage), e);
        }

    }

    //    Метод возвращает сообщения от клиента в формате UTF-8
    public String getMessage()  {
        String massage = null;
        try {
            massage = in.readUTF();
            Log.info(String.format("CLIENT IN MSG { InetAddress=%s } { PORT=%s } {PORT_SERVER=%s}\n %s",
                                   socket.getInetAddress(), socket.getPort(), Config.PORT, massage));
        } catch (IOException e) {
            Log.error(String.format("CLIENT IN MSG { InetAddress=%s } { PORT=%s } {PORT_SERVER=%s}\n %s\n",
                                    socket.getInetAddress(), socket.getPort(), Config.PORT, massage), e);
            e.printStackTrace();
        }

        return massage;
    }

    public void close(){

        Log.infoClose(this);

        try {
            socket.close();
//            Log.infoClose(socket);

        } catch (IOException e) {
            Log.errorClose(socket, e);
            e.printStackTrace();
        }

        try {
            in.close();
//            Log.infoClose(in);

        } catch (IOException e) {
            Log.errorClose(in, e);
            e.printStackTrace();
        }

        try {
            out.close();
//            Log.infoClose(out);

        } catch (IOException e) {
            Log.errorClose(out, e);
            e.printStackTrace();
        }
    }
}
