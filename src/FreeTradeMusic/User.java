package FreeTradeMusic;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class User
{
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private ObservableList<Song> ownedSongs;
    private ArrayList<String> favoriteArtists;

    public User(String username, String password,
                String email, String firstName,
                String lastName)
    {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        favoriteArtists = new ArrayList<>();
        ownedSongs = FXCollections.observableArrayList();
//        ownedSongs.add(new Song("Something About Us", "Daft Punk", "Discovery", "Dance",
//                2001, 229));

        FreeTradeMusic.mainWindowController.updateSongsTable("USER", ownedSongs);
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public ArrayList<String> getFavoriteArtists()
    {
        return favoriteArtists;
    }

    public void setFavoriteArtists(ArrayList<String> favoriteArtists)
    {
        this.favoriteArtists = favoriteArtists;
    }

    public void addFavoriteArtist(String favoriteArtist)
    {
        if(!favoriteArtists.contains(favoriteArtist))
            favoriteArtists.add(favoriteArtist);
        else
            Platform.runLater(() ->
                    FreeTradeMusic.mainWindowController.setStatus("ERROR", favoriteArtist
                            + " is already a favorite."));
    }

    public void removeFavoriteArtist(String favoriteArtist)
    {
        if(favoriteArtists.contains(favoriteArtist))
            favoriteArtists.remove(favoriteArtist);
        else
            Platform.runLater(() ->
                    FreeTradeMusic.mainWindowController.setStatus("ERROR", favoriteArtist
                            + " is not in your favorite artists."));
    }

    public void addOwnedSong(Song song)
    {
        if(!ownedSongs.contains(song))
            ownedSongs.add(song);
        else
            Platform.runLater(() ->
                    FreeTradeMusic.mainWindowController.setStatus("ERROR",
                            "You already own " + song.getTitle() + " by " + song.getArtist() + "."));
    }

    public ObservableList<Song> getOwnedSongs()
    {
        return ownedSongs;
    }
}
