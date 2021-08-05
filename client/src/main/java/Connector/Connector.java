package Connector;

import Config.Property;
import DataTransferTools.BinarySystem;
import DataTransferTools.BytePack;
import GraphicalIinterface.GUIBoxes.CloudBox.Cloud;
import Logger.Log;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class Connector implements Runnable {

    private final byte[] biLength = new byte[4];
    private BytePack packStart;
    private Cloud cloud;
    private Semaphore smp;
    private OutputStream out;
    private InputStream in;
    private String host;
    private int port;
    private boolean closeRead;

    public Connector (Cloud cloud, BytePack packStart)  {
        this.smp = new Semaphore(1);
        this.port = Integer.parseInt(Property.get("client.conf", "PORT"));
        this.host = Property.get("client.conf", "HOST");
        this.cloud = cloud;
        this.packStart = packStart;
    }

    @Override
    public void run(){

        try (
                Socket socket = new Socket(host, port);
                OutputStream out = socket.getOutputStream();
                InputStream in = socket.getInputStream()
        ) {

            Log.info(String.format("START CONNECT { InetAddress=%s } { PORT=%s  \n}",
                                   socket.getInetAddress(), socket.getPort()));

            this.out = out;
            this.in = in;

            if (packStart != null) writeBytes(packStart);

            startRead();

        } catch (Exception e){
            Log.error(String.format("START CONNECT { InetAddress=%s } { PORT=%s  \n}",
                                    host, port), e);
        }
    }


    public void writeBytes (BytePack pack) {

        try {

            smp.acquire();

            out.write(pack.getBiLength());
            out.write(pack.getHead());

            if (pack.getLengthData()> 0) out.write(pack.getData());

            out.flush();

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            smp.release();
        }
    }

    private void startRead() throws IOException {

        while (!closeRead){

               in.read(biLength);

               int lengthHead = BinarySystem.integerDecoder(biLength);

               byte[] head = new byte[lengthHead];

                in.read(head);

               BytePack pack = new BytePack(head);

               if (pack.getLengthData() > 0){

                  byte[] data = new byte[pack.getLengthData()];

                  in.read(data);

                  pack.setData(data);
               }

            cloud.route(pack);

        }

    }


    public void close () {
        this.closeRead = true;
    }
}
