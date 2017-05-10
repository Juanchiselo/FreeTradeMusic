package FreeTradeMusic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DatabaseManager
{
    private static DatabaseManager instance = null;
    private static String dbURL = "jdbc:mysql://ftm.ctcgotdan5u6.us-west-2.rds.amazonaws.com:3306/";
    private static String dbName = "FreeTradeMusicDB";
    private static String dbUser = "cs480";
    private static String dbPW = "cs480ftm";

    protected DatabaseManager()
    {
        // Exists only to defeat instantiation.
    }
    public static DatabaseManager getInstance()
    {
        if(instance == null)
            instance = new DatabaseManager();
        return instance;
    }

    private ResultSet queryDatabase(String query)
    {
        ResultSet result = null;

        return result;
    }

    public boolean login(String username, String password)
    {
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection(dbURL+dbName,dbUser,dbPW);
            Statement stmt = conn.createStatement();
            String varSQL = "SELECT * "
                    + "FROM Users "
                    + "WHERE User = " + "'" + username + "'"
                    + " AND Password = " + "'" + password + "'";
             ResultSet result = stmt.executeQuery(varSQL);
             if(!result.absolute(1)){stmt.close();conn.close();return false;}
             else{stmt.close();conn.close();return true;}
        }catch(SQLException | IllegalAccessException | ClassNotFoundException | InstantiationException e){System.out.println("Dead");}
        return false;
    }

    public boolean register(String username, String password, String email)
    {
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection(dbURL+dbName,dbUser,dbPW);
            Statement stmt = conn.createStatement();
            String varSQL = "INSERT INTO Users (User,Password,Email)" +
                            "Values('" + username + "'," + "'" + password +
                            "'," + "'" + email + "'" + ")";
            int res = stmt.executeUpdate(varSQL);
            if(res==0) return false;
            return true;
        }catch(SQLException |
                IllegalAccessException |
                ClassNotFoundException |
                InstantiationException e){System.out.println("Dead");}
        return false;
    }

    public boolean isUsernameAvailable(String username)
    {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection(dbURL + dbName, dbUser, dbPW);
            Statement stmt = conn.createStatement();
            String varSQL = "SELECT * "
                    + "FROM Users "
                    + "WHERE User = " + "'" + username.toLowerCase() + "'";
            ResultSet result = stmt.executeQuery(varSQL);
            if (stmt.executeQuery(varSQL).absolute(1)) {
                stmt.close();
                conn.close();
                return false;
            }
            return true;
        }catch(SQLException |
                IllegalAccessException |
                ClassNotFoundException |
                InstantiationException e){System.out.println("Dead");}
        return false;
    }
}
