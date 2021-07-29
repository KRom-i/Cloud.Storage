package Server;

import Client.*;
import Configuration.Config;
import DataBase.MySqlServer;
import Logger.Log;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {


    private Clients clients;
    private ExecutorService executorService;

    public Server () {

        clients = new Clients();

        MySqlServer.getConnection();

        executorService = Executors.newFixedThreadPool(Config.THREADS);

        try (ServerSocket mainServer = new ServerSocket(Config.PORT)) {

            Log.info(String.format("SERVER START PORT [%s]", Config.PORT));

            while (true) {
              executorService.execute(new ClientHandler(this, mainServer.accept()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        MySqlServer.disconnect();

    }


    public Clients getClients () {
        return clients;
    }
}
