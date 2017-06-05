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
    private String location;
    private String description;
    private int uploadedAlbums;
    private int uploadedSongs;
    private ObservableList<Song> ownedSongs;
    private ArrayList<String> favoriteArtists;

    public User(String username, String password,
                String email, String location,
                String description, int uploadedAlbums,
                int uploadedSongs)
    {
        this.username = username;
        this.password = password;
        this.email = email;
        this.location = location;
        this.description = description;
        this.uploadedAlbums = uploadedAlbums;
        this.uploadedSongs = uploadedSongs;
        favoriteArtists = new ArrayList<>();
        ownedSongs = FXCollections.observableArrayList();
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

    public ArrayList<String> getFavoriteArtists()
    {
        return favoriteArtists;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUploadedAlbums() {
        return uploadedAlbums;
    }

    public void setUploadedAlbums(int uploadedAlbums) {
        this.uploadedAlbums = uploadedAlbums;
    }

    public int getUploadedSongs() {
        return uploadedSongs;
    }

    public void setUploadedSongs(int uploadedSongs) {
        this.uploadedSongs = uploadedSongs;
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
