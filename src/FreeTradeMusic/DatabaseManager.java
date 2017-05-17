package FreeTradeMusic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DatabaseManager
{
    private static DatabaseManager instance = null;
    private String dbURL = "jdbc:mysql://ftm.ctcgotdan5u6.us-west-2.rds.amazonaws.com:3306/";
    private String dbName = "FreeTradeMusicDB";
    private String dbUser = "cs480";
    private String dbPW = "cs480ftm";
    private Statement stmt;
    private Connection conn;
    private String varSQL;

    protected DatabaseManager()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(dbURL + dbName, dbUser, dbPW);
            stmt = conn.createStatement();
        }catch(SQLException |
                IllegalAccessException |
                ClassNotFoundException |
                InstantiationException e){System.out.println("Dead");}
    }

    public static DatabaseManager getInstance()
    {
        if(instance == null)
            instance = new DatabaseManager();
        return instance;
    }

    public void exitDatabase()
    {
        try {
            stmt.close();
            conn.close();
        } catch(SQLException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    private ResultSet queryDatabase(String query) throws SQLException{return stmt.executeQuery(query);}

    public Error login(String username, String password)
    {
        varSQL = "SELECT * "
                + "FROM Users "
                + "WHERE User = " + "'" + username + "'"
                + " AND Password = " + "'" + password + "'";

        try {
            if(!queryDatabase(varSQL).absolute(1)){return Error.USERNAME_WRONG;}
            else{

                // TODO: Create a user object with the data you got from the database.
                return Error.NO_ERROR;}
        } catch (SQLException e) {
            return Error.DATABASE_ERROR;
        }
    }

    public boolean register(String username, String password, String email)
    {
        //if(isEmailAvailable(email)) return false;
        String varSQL = "INSERT INTO Users (User,Password,Email)" +
                "Values('" + username.toLowerCase() + "'," + "'" + password +
                "'," + "'" + email.toLowerCase() + "'" + ")";
        try{stmt.executeUpdate(varSQL);}catch(SQLException e){return false;}
        return true;
    }

    public Error isUsernameAvailable(String username)
    {
        varSQL = "SELECT * "
                + "FROM Users "
                + "WHERE User = " + "'" + username.toLowerCase() + "'";
        try {
            if(!queryDatabase(varSQL).absolute(1)){return Error.USERNAME_NOT_AVAILABLE;}
            else{return Error.NO_ERROR;}
        } catch (SQLException e) {
            return Error.DATABASE_ERROR;
        }
    }
    public Error isEmailAvailable(String email)
    {
        varSQL = "SELECT * "
                + "FROM Users "
                + "WHERE Email = " + "'" + email.toLowerCase() + "'";
        try {
            if(!queryDatabase(varSQL).absolute(1)){return Error.EMAIL_NOT_AVAILABLE;}
            else{return Error.NO_ERROR;}
        } catch (SQLException e) {
            return Error.DATABASE_ERROR;
        }
    }

    public ObservableList<Song> getSongs()
    {
        ObservableList<Song> songs = FXCollections.observableArrayList();
        int numberOfSongs = 1;
        String title = "Jailbreak";
        String artist = "AWOLNATION";
        String album = "Run";
        String genre = "Alternative Rock";
        int year = 2015;
        int duration = 281;//Duration is in seconds.

        // TODO: Rob query the database and get the songs.
        for(int i = 0; i < numberOfSongs; i++)
        {
            songs.add(new Song(title, artist, album, genre, year, duration));
        }

        songs.add(new Song("Hello", "Adele", "25", "Pop", 2016, 227));

        return songs;
    }
}
