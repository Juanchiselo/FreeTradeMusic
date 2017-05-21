package FreeTradeMusic;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class MusicPlayer
{
    private static MusicPlayer instance = null;
    private File currentFile;
    private Song currentSong;
    private Media media;
    public MediaPlayer mediaPlayer;


    private MusicPlayer()
    {
    }

    public static MusicPlayer getInstance()
    {
        if(instance == null)
            instance = new MusicPlayer();
        return instance;
    }

    public void addToPlaylist(Song song)
    {
        currentSong = song;
        currentFile = new File(song.getUrl());
        media = new Media(currentFile.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
    }

    public void playSong()
    {
        mediaPlayer.play();
        FreeTradeMusic.mainWindowController
                .updateMusicPlayer(currentSong.getTitle(),
                        currentSong.getArtist(),
                        currentSong.getDuration());
    }

    public void stopSong()
    {
    }

    public void nextSong()
    {
    }

    public void previousSong()
    {
    }

    public void pauseSong()
    {
        mediaPlayer.pause();
    }

    public String getCurrentTime()
    {
        double seconds = mediaPlayer.getCurrentTime().toSeconds();
        return String.format("%02d:%02d", (int)seconds / 60, (int)seconds % 60);
    }
}
