package DataBase;

import Logger.Log;

import java.sql.*;

public class MySqlServer {

    private static Connection connection;
    private static String LOCAL = "localhost";
    private static String PORT = "3306";
    private static String USER_LOG = "root";
    private static String PASS = "limit2301";
    private static String NAME_DATA_BASE = "dataCloudStorage";
    private static String URL;


    public static Connection getConnection(){

        if (connection == null){
            try {
                URL = String.format ("jdbc:mysql://%s:%s/%s", LOCAL,PORT, NAME_DATA_BASE );
                Log.info (String.format ("Connect Date Base URL [%s]", URL));
                connection = DriverManager.
                        getConnection(URL, USER_LOG, PASS);

            } catch (SQLException e) {
                Log.error("Connection mysql", e);
                e.printStackTrace();
            }
        }

        return connection;
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
