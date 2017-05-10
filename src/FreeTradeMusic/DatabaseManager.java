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
    private static Statement stmt;
    private static Connection conn;

    protected DatabaseManager()
    {
        // Exists only to defeat instantiation.
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(dbURL + dbName, dbUser, dbPW);
            stmt = conn.createStatement();
        }catch(SQLException |
                IllegalAccessException |
                ClassNotFoundException |
                InstantiationException e){System.out.println("Dead");}
    }
    public void exitDatabase() throws SQLException{stmt.close();conn.close();}
    public static DatabaseManager getInstance() throws SQLException
    {
        if(instance == null)
            instance = new DatabaseManager();
        return instance;
    }

    private ResultSet queryDatabase(String query) throws SQLException {return stmt.executeQuery(query);}

    public boolean login(String username, String password) throws SQLException
    {
        String varSQL = "SELECT * "
                + "FROM Users "
                + "WHERE User = " + "'" + username + "'"
                + " AND Password = " + "'" + password + "'";
       queryDatabase(varSQL);
       if(!queryDatabase(varSQL).absolute(1)){return false;}
       else{return true;}
    }

    public boolean register(String username, String password, String email) throws SQLException
    {
        String varSQL = "INSERT INTO Users (User,Password,Email)" +
                "Values('" + username.toLowerCase() + "'," + "'" + password +
                "'," + "'" + email.toLowerCase() + "'" + ")";
        if(stmt.executeUpdate(varSQL)==0) return false;
        return true;
    }

    public boolean isUsernameAvailable(String username) throws SQLException
    {
        String varSQL = "SELECT * "
                + "FROM Users "
                + "WHERE User = " + "'" + username.toLowerCase() + "'";
            if (queryDatabase(varSQL).absolute(1)) return false;
            return true;
    }
}
