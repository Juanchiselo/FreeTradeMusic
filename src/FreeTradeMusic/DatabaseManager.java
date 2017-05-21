package FreeTradeMusic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

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

    private DatabaseManager()
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

    private ResultSet queryDatabase(String query){
        try{return stmt.executeQuery(query);}
        catch(SQLException e){System.out.println("ERROR: " + e.getMessage());}
        return null;
    }

    public Error login(String username, String password)
    {
        String userTest = "SELECT * "
                + "FROM Users "
                + "WHERE User = " + "'" + username + "'";
        String pwTest = "SELECT * "
                + "FROM Users "
                + "WHERE Password = " + "'" + password + "'";
        try {
            if(!queryDatabase(userTest).absolute(1)){return Error.USERNAME_WRONG;}
            else if(!queryDatabase(pwTest).absolute(1)){return Error.PASSWORD_WRONG;}
            else{
                // TODO: Create a user object with the data you got from the database.
                FreeTradeMusic.user = new User("User", "Pass", "fake@outlook.com",
                        "User", "Name");
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

        varSQL = "SELECT Title,Artist,Album,Genre,Year,Duration "
                + "FROM Music";
        try{
            ResultSet rs = stmt.executeQuery(varSQL);
            while (rs.next()) {
                songs.add(new Song(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getInt(6),
                        null));
            }
        }catch(SQLException e){}
        return songs;
    }

    public Error submitSong(String title, String artist, String album, String genre, int year, int duration, File file)
    {
        String url;

        // TODO: Upload actual file to Amazon server and get the url.
        url = uploadFile(file);

        // TODO: INSERT song information into database including url but not the file and return error code.
        return Error.NO_ERROR;
    }

    private String uploadFile(File file)
    {
        String url = "";

        return url;
    }
}
