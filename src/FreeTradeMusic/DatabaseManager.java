package FreeTradeMusic;

import javafx.application.Platform;
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

    public void close(){
        try{
            stmt.close();
            conn.close();
        }catch(SQLException e){System.out.println("DB Error on close.");}
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
                + "WHERE User = " + "'" + username + "'"
                + "AND Password = " + "'" + password + "'";

        try {
            if(!queryDatabase(userTest).absolute(1)){return Error.USERNAME_WRONG;}
            else if(!queryDatabase(pwTest).absolute(1)){return Error.PASSWORD_WRONG;}
            else{
                ResultSet rs = queryDatabase(pwTest);
                // TODO: Create a user object with the data you got from the database.
                while(rs.next()) {
                    FreeTradeMusic.user = new User(rs.getString(1), rs.getString(2),
                            rs.getString(3), rs.getString(4), rs.getString(5),
                            0, 0);
                }
                //FreeTradeMusic.user = new User("User", "Pass", "fake@outlook.com",
                //        "User", "Name");
                return Error.NO_ERROR;}
        } catch (SQLException e) {
            System.out.println("Dbm");
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
            if(queryDatabase(varSQL).absolute(1)){return Error.USERNAME_NOT_AVAILABLE;}
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

        varSQL = "SELECT Title,Artist,Album,Genre,Year,Duration,FileName "
                + "FROM Music";
        Song temp;
        String fn;
        try{
            ResultSet rs = stmt.executeQuery(varSQL);
            while (rs.next()) {
                temp = new Song(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getInt(6),
                        null);
                temp.setFileName(rs.getString(7));
                songs.add(temp);
            }
        }catch(SQLException e){}
        return songs;
    }

    public ObservableList<Song> getUserSongs(String user)
    {
        ObservableList<Song> songs = FXCollections.observableArrayList();

        varSQL = "SELECT Owned.Title,Owned.Artist,Music.Album,Music.Genre,Music.Year,Music.Duration "
                + "FROM Music, Owned "
                + "WHERE Music.Title = Owned.Title"
                + " AND Music.Artist = Owned.Artist"
                + " AND Owned.User = '" + user + "'";
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
        }catch(SQLException e){System.out.println("Wrong");}
        return songs;
    }

    public Error submitSong(String title, String artist, String album, String genre,
                            int year, int duration, File file)
    {
        varSQL = "INSERT INTO Music (Title,Artist,Album,Year,Genre,Duration,FileName)" +
                "Values('" + title + "'," + "'" + artist +
                "'," + "'" + album + "'," + "'" + year +
                "','"  + genre + "'," + duration +
                ",'" + file.getName() + "')";

        String intoOwned = "INSERT INTO Owned (User, Title, Artist)" +
                "Values('" + FreeTradeMusic.user.getUsername() +
                "','" + title + "','" + artist +"')";

        Platform.runLater(() -> FreeTradeMusic.mainWindowController.setStatus("STATUS",
                "Uploading \"" + title + "\" to the database."));
        new Thread(() -> AmazonClass.getInstance().upload(file)).start();
        Platform.runLater(() -> FreeTradeMusic.mainWindowController.setStatus("STATUS",
                "Finished uploading \"" + title + "\" to the database."));

        try{
            stmt.executeUpdate(varSQL);
            stmt.executeUpdate(intoOwned);
        }
        catch(SQLException e){return Error.DATABASE_ERROR;}

        return Error.NO_ERROR;
    }
    /* Didn't use
    private String uploadFile(File file)
    {
        String url = "";

        return url;
    }
    */

    public User getProfile(String username)
    {
        User artist = null;

        String query = "SELECT * "
                + "FROM Users "
                + "WHERE User = " + "'" + username + "'";

        try {
            if(!queryDatabase(query).absolute(1)){return null;}
            else{
                ResultSet rs = queryDatabase(query);
                while(rs.next()) {
                    artist = new User(rs.getString(1), rs.getString(2),
                            rs.getString(3), rs.getString(4), rs.getString(5),
                            0, 0);
                }
            }
        } catch (SQLException e) {
            System.out.println("Dbm");
        }

        return artist;
    }

    public Error updateProfile(String username, String location, String description)
    {
        varSQL = "UPDATE Users SET location = '" + location + "',description = '" +
                description + "' " + "WHERE Users.user = '" + username + "'";
        try { stmt.executeUpdate(varSQL);
        }
        catch(SQLException e) { System.out.println("Wrong");/*(return Error.DATABASE_ERROR;*/ }
        return Error.NO_ERROR;
    }

    public Error boughtSong(Song song)
    {
        varSQL = "INSERT INTO Owned (User, Title, Artist)" +
                "Values('" + FreeTradeMusic.user.getUsername() +
                "','" + song.getTitle() + "','" + song.getArtist() +"')";

        try{stmt.executeUpdate(varSQL);}
        catch(SQLException e){return Error.DATABASE_ERROR;}

        return Error.NO_ERROR;
    }

    public void downloadSong(Song song)
    {
        String currentDirectory = System.getProperty("user.dir");
        Platform.runLater(() -> FreeTradeMusic.mainWindowController.setStatus("STATUS",
                "Downloading \"" + song.getTitle() + "\" to the database."));
        new Thread(() -> AmazonClass.getInstance().download(currentDirectory , song.getFileName(), song)).start();
    }
}
