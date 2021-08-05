package DataBase;

import Config.Property;
import Logger.Log;

import java.sql.*;

public class MySqlServer {

    private static Connection connection;
    private static String HOST;
    private static String PORT;
    private static String USER_LOG;
    private static String PASS;
    private static String NAME_DATA_BASE;
    private static String URL;


    public static Connection getConnection(){

        if (connection == null){

            initProperty();

            try {
                URL = String.format ("jdbc:mysql://%s:%s/%s", HOST,PORT, NAME_DATA_BASE );

                Log.info (String.format ("Connect MYSQL HOST [%s] PORT [%s] NAME_DATA_BASE [%s]",
                                         HOST, PORT, NAME_DATA_BASE));

                connection = DriverManager.
                        getConnection(URL, USER_LOG, PASS);

            } catch (SQLException e) {
                Log.error("Connection MYSQL", e);
                e.printStackTrace();
            }
        }

        return connection;
    }

    private static void initProperty () {
        HOST = Property.get("server.conf", "SQL_HOST");
        PORT = Property.get("server.conf", "SQL_PORT");
        USER_LOG = Property.get("server.conf", "SQL_USER_LOG");
        PASS = Property.get("server.conf", "SQL_PASS");
        NAME_DATA_BASE = Property.get("server.conf", "SQL_NAME_DATA_BASE");
    }

    public static void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            Log.error("Connection.close mysql", e);
            e.printStackTrace();
        }
    }


    public static void statementClose(Statement statement){
        try {
          if (statement != null)  statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void resultSetClose(ResultSet resultSet){
        try {
            if (resultSet != null) resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
