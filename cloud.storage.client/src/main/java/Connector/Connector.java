package Connector;

import Configuration.Config;
import Commands.Commands;
import GraphicalIinterface.CommonСlassesGUI.ListFiles.FileInfo;
import GraphicalIinterface.GUIBoxes.CloudBox.Cloud;
import Files.IncomigFiles.IncomingFile;
import Logger.Log;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Connector implements Runnable {

    private Cloud cloud;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private String startMsg;

    public Connector (Cloud cloud, String startMsg)  {
        this.cloud = cloud;
        this.startMsg = startMsg;
    }

    @Override
    public void run () {

        try {

            this.socket = new Socket(Config.HOST, Config.PORT);
            this.out = new DataOutputStream(socket.getOutputStream());
            this.in = new DataInputStream(socket.getInputStream());

            Log.info(String.format("START CONNECT { InetAddress=%s } { PORT=%s  \n}",
                                   socket.getInetAddress(), socket.getPort()));
        } catch (Exception e) {
            Log.error(String.format("START CONNECT { InetAddress=%s } { PORT=%s  \n}",
                                    socket.getInetAddress(), socket.getPort()), e);
        }

        try {

            sendMsg(startMsg);

            while (true) {

                String[] msg = getMessage().split(Commands.DELIMITER[0]);

                if(Commands.AUTH.equals(msg[0])) {

                    if(Commands.CHECK.equals(msg[1])) {

                        if(Commands.USER.equals(msg[2])) {
                            cloud.setAuth(Boolean.parseBoolean(msg[3]));

                        } else if(Commands.INFO.equals(msg[2])) {
                            cloud.setResultAuthBox(msg[3]);

                        }

                    } else if(Commands.INFO.equals(msg[1])) {
                        cloud.setConnectInfo(msg[2]);

                    } else if(Commands.EXIT.equals(msg[1])) {
                        cloud.closeBox();
                        break;
                    }

                } else if(Commands.NICK.equals(msg[0])) {

                    if (Commands.SET.equals(msg[1])) {
                        cloud.setNickName(msg[2]);

                    } else if(Commands.CHECK.equals(msg[1])) {
                        cloud.checkNick(msg[2], Boolean.parseBoolean(msg[3]));
                    }

                } else if (Commands.SIZE.equals(msg[0])) {

                    if (Commands.SET.equals(msg[1])) {
                        cloud.setCurrentSize(msg[2]);
                        cloud.setCurrentMaxSize(msg[3]);
                        cloud.setMaxSizes(msg[4]);
                    }


                } else if(Commands.ROOT.equals(msg[0])) {
                    if(Commands.INFO.equals(msg[1])) {
                        cloud.setRoots(msg[2]);
                        cloud.setRoot(msg[3]);
                    }


                } else if(Commands.CURRENT_DIRECTORY.equals(msg[0])) {
                    if(Commands.DIRECTORY_HOME.equals(msg[1])) {
                        cloud.currentDirectory(null);

                    } else {
                        cloud.currentDirectory(msg[1]);
                    }

                } else if(Commands.FILE.equals(msg[0])) {

                    if(Commands.LIST.equals(msg[1])) {
                        int numberFile = Integer.parseInt(msg[2]);
                        List<FileInfo> fileInfoList = new ArrayList<>();
                        for(int i = 0; i < numberFile; i++) {
                            fileInfoList.add(new FileInfo(getMessage()));
                        }
                        cloud.update(fileInfoList);

                    } else if(Commands.INCOMING.equals(msg[1])) {

                        if (Commands.INFO.equals(msg[2])) {
                            cloud.setNumInFile(msg[3]);

                        } else if (Commands.LIST.equals(msg[2])) {

                            int files = Integer.parseInt(msg[3]);

                            List<IncomingFile> incomingFiles = new ArrayList<>();

                            for(int i = 0; i < files; i++) {
                                incomingFiles.add(new IncomingFile(getMessage()));
                            }

                            cloud.showInFilesStage(incomingFiles);

                        }
                    }

                }


            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.error(String.format("DISCONNECT { InetAddress=%s } { PORT=%s  \n}",
                                    socket.getInetAddress(), socket.getPort()), e);

        } finally {
            if(!cloud.getExecutorService().isShutdown()) cloud.getExecutorService().shutdown();
            cloud.setAuth(false);
            close();
        }

    }

    //    Метод возвращает сообщения от сервера в формате UTF-8
    public String getMessage ()  {
        String massage = null;
        try {
            massage = in.readUTF();
            Log.info(String.format("SERVER IN MSG { InetAddress=%s } { PORT=%s  \n %s}",
                                   socket.getInetAddress(), socket.getPort(), massage));
        } catch (IOException e) {
            e.printStackTrace();
            Log.error(String.format("SERVER IN MSG { InetAddress=%s } { PORT=%s  \n %s}",
                                   socket.getInetAddress(), socket.getPort(), massage), e);
        }

        return massage;
    }

    // Отправка сообщения серверу в формате UTF-8

    public void sendMsg (String msg) {

            try {
                out.writeUTF(msg);
                Log.info(String.format("CLIENT OUT MSG { InetAddress=%s } { PORT=%s  \n %s}",
                                       socket.getInetAddress(), socket.getPort(), msg));
            } catch (Exception e) {
                Log.error(String.format("CLIENT OUT MSG { InetAddress=%s } { PORT=%s  \n %s}",
                                       socket.getInetAddress(), socket.getPort(), msg), e);
            }


    }




    public String getPacket(String msg){
        return lineLengthToString(msg) + msg;
    }

    public String lineLengthToString(String msg){
        return String.format("%010d", msg.length());
    }

    public DataOutputStream getOut () {
        return out;
    }


    public DataInputStream getIn () {
        return in;
    }

    public void close(){

        Log.infoClose(this);

        try {
            socket.close();
            Log.infoClose(socket);

        } catch (IOException e) {
            Log.errorClose(socket, e);
            e.printStackTrace();
        }

        try {
            in.close();
            Log.infoClose(in);

        } catch (IOException e) {
            Log.errorClose(in, e);
            e.printStackTrace();
        }

        try {
            out.close();
            Log.infoClose(out);

        } catch (IOException e) {
            Log.errorClose(out, e);
            e.printStackTrace();
        }
    }


}
