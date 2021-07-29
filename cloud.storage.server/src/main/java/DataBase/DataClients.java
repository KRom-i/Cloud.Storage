package DataBase;

import Client.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataClients {

    public Client checkLoginAndPass(String log, int pass){

        PreparedStatement statement = null;
        ResultSet rs = null;

        try {

            statement = MySqlServer.getConnection ().prepareStatement(
                    "SELECT * FROM users WHERE logUser = ?;"
            );

            statement.setString(1, log);

            rs = statement.executeQuery();

            if (rs.next()){

                int abPass = rs.getInt("passUser");

                if (pass == abPass){

                    int idClint = rs.getInt("idUser");
                    String nickName =  rs.getString("nickNameUser");
                    return new Client(idClint, nickName);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySqlServer.statementClose(statement);
            MySqlServer.resultSetClose(rs);
        }

        return null;

    }

    public boolean checkUnique(String nameColumn, String value){

        boolean check = false;

        PreparedStatement statement = null;
        ResultSet rs = null;

        try {

            statement = MySqlServer.getConnection ().prepareStatement(
                    "SELECT * FROM users WHERE " + nameColumn +" = ?;"
            );

            statement.setString(1, value);


            rs = statement.executeQuery();

            check = !rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySqlServer.statementClose(statement);
            MySqlServer.resultSetClose(rs);
        }

        return check;
    }

    public int addNewClient(String nickName, String log, int pass){

        if (!checkUnique("nickNameUser", nickName)) return -1;

        if (!checkUnique("logUser", log)) return -2;

        PreparedStatement statement = null;

        try {

            String query = "INSERT INTO users (nickNameUser, logUser, passUser) VALUES (?, ?, ?);";


            statement = MySqlServer.getConnection ().prepareStatement(query);

            statement.setString(1, nickName);
            statement.setString(2, log);
            statement.setInt (3, pass);


            return statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            MySqlServer.statementClose(statement);
        }

    }

    public boolean checkAdmin (int idClient, int pass) {

        PreparedStatement statement = null;
        ResultSet rs = null;

        try {

            statement = MySqlServer.getConnection ().prepareStatement(
                    "SELECT * FROM admins WHERE idClient = ?;"
            );

            statement.setInt(1, idClient);

            rs = statement.executeQuery();

            if (rs.next()){

                int dataPass = rs.getInt("passAdmin");

                if (dataPass == pass) return true;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySqlServer.statementClose(statement);
            MySqlServer.resultSetClose(rs);
        }

        return false;

    }


}
