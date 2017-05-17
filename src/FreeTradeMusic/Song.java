package FreeTradeMusic;

import javafx.beans.property.SimpleStringProperty;

public class Song
{
    private final SimpleStringProperty title;
    private final SimpleStringProperty artist;
    private final SimpleStringProperty album;
    private final SimpleStringProperty genre;
    private int year;
    private int durationInSeconds;
    private final SimpleStringProperty duration;

    public Song(String title, String artist, String album, String genre, int year, int duration)
    {
        this.title = new SimpleStringProperty(title);
        this.artist = new SimpleStringProperty(artist);
        this.album = new SimpleStringProperty(album);
        this.genre = new SimpleStringProperty(genre);
        this.year = year;
        this.durationInSeconds = duration;
        this.duration = new SimpleStringProperty(convertDuration(duration));
    }

    public String getTitle()
    {
        return title.get();
    }

    public void setTitle(String title)
    {
        this.title.set(title);
    }

    public String getArtist()
    {
        return artist.get();
    }

    public void setArtist(String artist)
    {
        this.artist.set(artist);
    }

    public String getAlbum()
    {
        return album.get();
    }

    public void setAlbum(String album)
    {
        this.album.set(album);
    }

    public String getGenre()
    {
        return genre.get();
    }

    public void setGenre(String genre)
    {
        this.genre.set(genre);
    }

    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public String getDuration()
    {
        return duration.get();
    }

    public int getDurationInSeconds()
    {
        return durationInSeconds;
    }

    public void setDuration(int duration)
    {
        durationInSeconds = duration;
        this.duration.set(convertDuration(duration));
    }

    private String convertDuration(int duration)
    {
        return String.valueOf(duration / 60)
                + ":" + String.valueOf(duration % 60);
    }
}