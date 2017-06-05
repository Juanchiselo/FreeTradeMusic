package FreeTradeMusic;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class MusicPlayer
{
    private static MusicPlayer instance = null;
    private File currentFile;
    private Song currentSong;
    private MediaPlayer mediaPlayer;
    private ArrayList<MediaPlayer> playlist = new ArrayList<>();
    private ArrayList<Song> songs = new ArrayList<>();
    private ArrayList<Song> previousSongs = new ArrayList<>();
    private ArrayList<MediaPlayer> previousPlaylist = new ArrayList<>();
    private boolean repeat = false;
    private boolean shuffle = false;
    private boolean playing = false;
    private Random random = new Random();


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
        songs.add(song);
        currentFile = new File(song.getUrl());
        playlist.add(new MediaPlayer(new Media(currentFile.toURI().toString())));
        FreeTradeMusic.mainWindowController.disableMusicPlayer(false);
    }

    public void playSong()
    {
        if(!playlist.isEmpty())
        {
            if(mediaPlayer == null)
            {
                if(shuffle)
                {
                    currentSong = songs.remove(0);
                    mediaPlayer = playlist.remove(0);
                }
                else
                {
                    int index = random.nextInt(playlist.size());
                    currentSong = songs.remove(index);
                    mediaPlayer = playlist.remove(index);
                }
                mediaPlayer.play();
                playing = true;
                FreeTradeMusic.mainWindowController
                        .updateMusicPlayer(currentSong.getTitle(),
                                currentSong.getArtist(),
                                currentSong.getDuration());

                mediaPlayer.setOnEndOfMedia(() -> {
                    mediaPlayer.stop();
                    if(!playlist.isEmpty())
                    {
                        mediaPlayer = null;
                        playSong();
                    }
                    else
                        FreeTradeMusic.mainWindowController.disableMusicPlayer(true);
                });
            }
            else
                mediaPlayer.play();
        }
    }

    public void stopSong()
    {
        mediaPlayer.stop();
        playing = false;
        mediaPlayer = null;
        playlist.clear();
        songs.clear();
        previousSongs.clear();
        previousPlaylist.clear();
    }

    public void nextSong()
    {
        if(!playlist.isEmpty())
        {
            mediaPlayer.stop();
            previousSongs.add(currentSong);
            previousPlaylist.add(mediaPlayer);
            mediaPlayer = null;
            playSong();
        }
    }

    public void previousSong()
    {
        if(!previousPlaylist.isEmpty())
        {
            mediaPlayer.stop();
            songs.add(0, currentSong);
            playlist.add(0, mediaPlayer);
            currentSong = previousSongs.remove(previousSongs.size() - 1);
            mediaPlayer = previousPlaylist.remove(previousPlaylist.size() - 1);
            songs.add(0, currentSong);
            playlist.add(0, mediaPlayer);
            mediaPlayer = null;
            playSong();
        }
    }

    public void pauseSong()
    {
        if(mediaPlayer != null)
        {
            playing = false;
            mediaPlayer.pause();
        }
    }

    public void repeatPlaylist(boolean repeat)
    {
        this.repeat = repeat;
    }

    public void shufflePlaylist(boolean shuffle)
    {
        this.shuffle = shuffle;
    }

    public String getCurrentTime()
    {
        double seconds = mediaPlayer.getCurrentTime().toSeconds();
        return String.format("%02d:%02d", (int)seconds / 60, (int)seconds % 60);
    }

    public boolean isSongPlaying()
    {
        return playing;
    }

    public boolean isPlaylistEmpty()
    {
        return playlist.isEmpty();
    }

    public boolean isPreviousPlaylistEmpty()
    {
        return previousPlaylist.isEmpty();
    }
}
