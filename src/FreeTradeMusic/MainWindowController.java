package FreeTradeMusic;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import org.apache.tika.metadata.Metadata;

import java.io.File;
import java.util.*;

public class MainWindowController
{
    // TableView
    @FXML private TableView<Song> storeTableView;
    @FXML private TableColumn<Song, String> storeTitleColumn;
    @FXML private TableColumn<Song, String> storeArtistColumn;
    @FXML private TableColumn<Song, String> storeAlbumColumn;
    @FXML private TableColumn<Song, Integer> storeYearColumn;
    @FXML private TableColumn<Song, String> storeGenreColumn;
    @FXML private TableColumn<Song, Integer> storeDurationColumn;
    private ObservableList<Song> songs = FXCollections.observableArrayList();

    // TableView
    @FXML private TableView<Song> userSongsTableView;
    @FXML private TableColumn<Song, String> userTitleColumn;
    @FXML private TableColumn<Song, String> userArtistColumn;
    @FXML private TableColumn<Song, String> userAlbumColumn;
    @FXML private TableColumn<Song, Integer> userYearColumn;
    @FXML private TableColumn<Song, String> userGenreColumn;
    @FXML private TableColumn<Song, Integer> userDurationColumn;

    @FXML private ChoiceBox<String> storeFilterChoiceBox;
    @FXML private TextField storeFilterTextField;

    @FXML private ChoiceBox<String> userFilterChoiceBox;
    @FXML private TextField userFilterTextField;

    @FXML private TabPane mainWindowTabPane;
    @FXML private Tab homeTab;
    @FXML private Tab storeTab;
    @FXML private Tab profileTab;

    @FXML private TabPane homeTabPane;
    @FXML private Tab musicLibraryTab;
    @FXML private Tab submitSongTab;
    @FXML private Tab walletTab;


    @FXML private ContextMenu songsContextMenu;

    @FXML private Label statusLabel;

    @FXML private ImageView backButton;
    @FXML private ImageView playButton;
    @FXML private ImageView pauseButton;
    @FXML private ImageView nextButton;
    @FXML private GridPane musicPlayerGridPane;
    public Timer timer = new Timer();

    @FXML private Label titleLabel;
    @FXML private Label artistLabel;
    @FXML public Label currentTimeLabel;
    @FXML private Label durationLabel;
    @FXML private Slider timeSlider;

    @FXML private TextField titleTextField;
    @FXML private TextField albumTextField;
    @FXML private TextField genreTextField;
    @FXML private TextField yearTextField;
    @FXML private Button submitSongButton;

    @FXML private ImageView profileImageView;
    @FXML private Circle profileImage;

    File file;
    List<File> files;
    FileChooser fileChooser = new FileChooser();

    private final PseudoClass errorClass = PseudoClass.getPseudoClass("error");
    private ArrayList<Error> errors = new ArrayList<>();
    private Error error;
    private ArrayList<TextField> textFields = new ArrayList<>();

    private Metadata metadata;

    @FXML private Label artistNameLabel;
    @FXML private Label artistLocationLabel;
    @FXML private Label artistDescriptionLabel;
    @FXML private Label artistAlbumsLabel;
    @FXML private Label artistSongsLabel;
    @FXML private Label editProfileButton;
    @FXML private TextField artistUsernameTextField;
    @FXML private TextField artistLocationTextField;
    @FXML private TextArea artistDescriptionTextArea;
    @FXML private Button followButton;
    @FXML private Button seeMusicButton;
    @FXML private Button updateProfileButton;

    public void initialize()
    {
        textFields.add(titleTextField);
        textFields.add(albumTextField);
        textFields.add(genreTextField);
        textFields.add(yearTextField);

        Circle maskCircle = new Circle(75, 75, 70);
        profileImageView.setClip(maskCircle);


        songs = DatabaseManager.getInstance().getSongs();

        storeTitleColumn.setCellValueFactory(
                new PropertyValueFactory<>("title"));

        storeArtistColumn.setCellValueFactory(
                new PropertyValueFactory<>("artist"));

        storeAlbumColumn.setCellValueFactory(
                new PropertyValueFactory<>("album"));

        storeYearColumn.setCellValueFactory(
                new PropertyValueFactory<>("year"));

        storeGenreColumn.setCellValueFactory(
                new PropertyValueFactory<>("genre"));

        storeDurationColumn.setCellValueFactory(
                new PropertyValueFactory<>("duration"));



        userTitleColumn.setCellValueFactory(
                new PropertyValueFactory<>("title"));

        userArtistColumn.setCellValueFactory(
                new PropertyValueFactory<>("artist"));

        userAlbumColumn.setCellValueFactory(
                new PropertyValueFactory<>("album"));

        userYearColumn.setCellValueFactory(
                new PropertyValueFactory<>("year"));

        userGenreColumn.setCellValueFactory(
                new PropertyValueFactory<>("genre"));

        userDurationColumn.setCellValueFactory(
                new PropertyValueFactory<>("duration"));



        storeFilterChoiceBox.getSelectionModel().selectedIndexProperty()
                .addListener((observable, oldValue, newValue) ->
                {
                    storeFilterTextField.clear();

                    if(newValue.intValue() == 5)
                        showFavoriteArtists();
                    else
                        updateSongsTable("STORE", songs);
                });

        userFilterChoiceBox.getSelectionModel().selectedIndexProperty()
                .addListener((observable, oldValue, newValue) ->
                {
                    userFilterTextField.clear();

                    if(newValue.intValue() == 5)
                        showFavoriteArtists();
                    else
                        if(FreeTradeMusic.user != null)
                            updateSongsTable("USER", FreeTradeMusic.user.getOwnedSongs());
                });

        // Initializes the Filter ChoiceBox.
        storeFilterChoiceBox.setItems(FXCollections.observableArrayList(
                "Title", "Artist", "Album", "Year",
                "Genre", "Favorite Artists"));
        storeFilterChoiceBox.getSelectionModel().selectFirst();

        userFilterChoiceBox.setItems(FXCollections.observableArrayList(
                "Title", "Artist", "Album", "Year",
                "Genre", "Favorite Artists"));
        userFilterChoiceBox.getSelectionModel().selectFirst();

        // Right click context menu.
        storeTableView.addEventHandler(MouseEvent.MOUSE_CLICKED, t ->
        {
            if(t.getButton() == MouseButton.SECONDARY)
                songsContextMenu.show(storeTableView, t.getScreenX() , t.getScreenY());
        });



//        timeSlider.valueProperty().addListener(new InvalidationListener() {
//            public void invalidated(Observable ov)
//            {
//                if (timeSlider.isValueChanging())
//                {
//                    // multiply duration by percentage calculated by slider position
//                    if (duration != null) {
//                        mediaPlayer.seek(duration.multiply(slider.getValue() / 100.0));
//                    }
//                    updateValues();
//
//                }
//            }
//        });

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP3", "*.mp3")
        );
    }

    public void attachFilter(TableView<Song> tableView, ObservableList<Song> songs)
    {
        FilteredList<Song> filteredData = new FilteredList<>(songs, p -> true);

        storeFilterTextField.textProperty().addListener((observable, oldValue, newValue)
                -> filteredData.setPredicate(song ->
        {
            String filter = storeFilterChoiceBox.getValue();
            // If filter text is empty, display all songs.
            if (newValue == null || newValue.isEmpty())
                return true;

            String lowerCaseFilter = newValue.toLowerCase();

            if (filter.equals("Title")
                    && song.getTitle().toLowerCase().contains(lowerCaseFilter))
                return true;
            else if ((filter.equals("Artist") || filter.equals("Favorite Artists"))
                    && song.getArtist().toLowerCase().contains(lowerCaseFilter))
                return true;
            else if (filter.equals("Album")
                    && song.getAlbum().toLowerCase().contains(lowerCaseFilter))
                return true;
            else if (filter.equals("Year")
                    && String.valueOf(song.getYear()).contains(lowerCaseFilter))
                return true;
            else if (filter.equals("Genre")
                    && song.getGenre().toLowerCase().contains(lowerCaseFilter))
                return true;
            // Does not match.
            return false;
        }));

        SortedList<Song> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableView.setItems(sortedData);
    }

    public void updateSongsTable(String table, ObservableList<Song> songs)
    {
        TableView<Song> tableView = null;
        switch (table)
        {
            case "STORE":
                tableView = storeTableView;
                break;
            case "USER":
                tableView = userSongsTableView;
                break;
        }

        attachFilter(tableView, songs);
        tableView.refresh();
    }

    private void updateStoreTable()
    {
        songs = DatabaseManager.getInstance().getSongs();
        attachFilter(storeTableView, songs);
        storeTableView.refresh();
    }

    /**
     * The event handler for the Logout button in the MainWindow scene.
     */
    public void onLogout()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Logout");
        alert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            if(MusicPlayer.getInstance().isSongPlaying())
                MusicPlayer.getInstance().stopSong();
            FreeTradeMusic.stage.setScene(FreeTradeMusic.loginScene);
            FreeTradeMusic.stage.setResizable(false);
        }
    }

    /**NAVIGATION HANDLERS**/
    public void onGoToHome()
    {
        mainWindowTabPane.getSelectionModel().select(homeTab);
        homeTabPane.getSelectionModel().select(musicLibraryTab);
    }

    public void onGoToStore()
    {
        mainWindowTabPane.getSelectionModel().select(storeTab);
    }

    public void onGoToProfile()
    {
        artistNameLabel.setText(FreeTradeMusic.user.getUsername());
        artistLocationLabel.setText(FreeTradeMusic.user.getLocation());
        artistAlbumsLabel.setText(String.valueOf(FreeTradeMusic.user.getUploadedAlbums()) + " Albums");
        artistSongsLabel.setText(String.valueOf(FreeTradeMusic.user.getUploadedSongs()) + " Songs");
        artistDescriptionLabel.setText(FreeTradeMusic.user.getDescription());
        editProfileButton.setVisible(true);
        mainWindowTabPane.getSelectionModel().select(profileTab);
    }

    public void onGoToMusicLibrary()
    {
        homeTabPane.getSelectionModel().select(musicLibraryTab);
    }

    public void onGoToSubmitSong()
    {
        homeTabPane.getSelectionModel().select(submitSongTab);
    }


    public void onEditProfile()
    {
        enableUpdateProfileComponents(true);
    }

    public void onAddFavoriteArtist()
    {
        Song song = storeTableView.getSelectionModel().getSelectedItem();
        FreeTradeMusic.user.addFavoriteArtist(song.getArtist());
    }

    public void onRemoveFavoriteArtist()
    {
        Song song = storeTableView.getSelectionModel().getSelectedItem();
        FreeTradeMusic.user.removeFavoriteArtist(song.getArtist());
        showFavoriteArtists();
    }

    private void showFavoriteArtists()
    {
        ObservableList<Song> songs = FXCollections.observableArrayList();

        for(Song song : this.songs)
        {
            if(FreeTradeMusic.user.getFavoriteArtists()
                    .contains(song.getArtist()))
                songs.add(song);
        }

        updateSongsTable("STORE", songs);
    }


    public void onSubmitSong()
    {
        String title = titleTextField.getText().trim();
        String album = albumTextField.getText().trim();
        String genre = genreTextField.getText().trim();
        String yearString = yearTextField.getText().trim();

        if(album.isEmpty())
        {
            album = "Single Release";
            albumTextField.setText("Single Release");
        }

        if(!title.isEmpty() && !genre.isEmpty() && !yearString.isEmpty() && file != null)
        {
            int duration = (int)Double.parseDouble(metadata.get("xmpDM:duration")) / 1000;
            int year = Integer.valueOf(yearString);

            errors.add(DatabaseManager.getInstance().submitSong(title, FreeTradeMusic.user.getUsername(),
                    album, genre, year, duration, file));

            if(errors.get(0) == Error.NO_ERROR)
            {
                updateStoreTable();
                updateSongsTable("USER", DatabaseManager.getInstance().getUserSongs(
                        FreeTradeMusic.user.getUsername()));

                alertUser("Song has been submitted successfully",
                        "Your song has been submitted successfully.",
                        "INFORMATION");
            }
        }
        else
        {
            if(title.isEmpty())
                errors.add(Error.TITLE_EMPTY);
            if(genre.isEmpty())
                errors.add(Error.GENRE_EMPTY);
            if(yearString.isEmpty())
                errors.add(Error.YEAR_EMPTY);
        }

        setErrors();
    }

    public void onLocateFile()
    {
        fileChooser.setTitle("Locate File");
        file = fileChooser.showOpenDialog(FreeTradeMusic.stage);
        if(file != null)
        {
            submitSongButton.setDisable(false);
            metadata = FileParser.getInstance()
                    .getMetadata(file.getAbsolutePath());

            titleTextField.setText(metadata.get("title"));
            albumTextField.setText(metadata.get("xmpDM:album"));
            genreTextField.setText(metadata.get("xmpDM:genre"));
            yearTextField.setText(metadata.get("xmpDM:releaseDate"));
        }
    }

    public void onLocateFiles()
    {
        fileChooser.setTitle("Locate Files");
        files = fileChooser.showOpenMultipleDialog(FreeTradeMusic.stage);
        if(files != null)
            submitSongButton.setDisable(false);
    }

    public void onAddSongToPlaylist()
    {
        Song song = storeTableView.getSelectionModel().getSelectedItem();
        DatabaseManager.getInstance().downloadSong(song);
    }

    public void onPlay()
    {
        if(!MusicPlayer.getInstance().isSongPlaying()
                && !MusicPlayer.getInstance().isPlaylistEmpty())
        {
            MusicPlayer.getInstance().playSong();
            playButton.setVisible(false);
            pauseButton.setVisible(true);
        }
    }

    public void onPause()
    {
        MusicPlayer.getInstance().pauseSong();
        pauseButton.setVisible(false);
        playButton.setVisible(true);
    }

    public void onNext()
    {
        MusicPlayer.getInstance().nextSong();
    }

    public void onPrevious()
    {
        MusicPlayer.getInstance().previousSong();
    }

    public void onShuffle()
    {
        MusicPlayer.getInstance().shufflePlaylist(true);
    }

    public void updateMusicPlayer(String title, String artist,
                                  String duration)
    {
        titleLabel.setText(title);
        artistLabel.setText(artist);
        durationLabel.setText(duration);

        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                Platform.runLater(() ->
                        currentTimeLabel.setText(MusicPlayer
                                .getInstance().getCurrentTime()));
            }
        }, 0, 1000);
    }

    public void disableMusicPlayer(boolean disable)
    {
        musicPlayerGridPane.setVisible(!disable);
    }

    private void clearSubmissionForm()
    {
        for(TextField textField : textFields)
            textField.clear();
        submitSongButton.setDisable(true);
    }

    public void onViewArtistProfile()
    {
        enableUpdateProfileComponents(false);
        Song song = storeTableView.getSelectionModel().getSelectedItem();
        User artist = null;

        if(song != null)
            artist = DatabaseManager.getInstance().getProfile(song.getArtist());

        if(artist != null)
        {
            if(artist.getUsername().equals(FreeTradeMusic.user.getUsername()))
                editProfileButton.setVisible(true);
            else
                editProfileButton.setVisible(false);
            artistNameLabel.setText(artist.getUsername());
            artistLocationLabel.setText(artist.getLocation());
            artistAlbumsLabel.setText(String.valueOf(artist.getUploadedAlbums()) + " Albums");
            artistSongsLabel.setText(String.valueOf(artist.getUploadedSongs()) + " Songs");
            artistDescriptionLabel.setText(artist.getDescription());
            mainWindowTabPane.getSelectionModel().select(profileTab);
        }
    }

    public void enableUpdateProfileComponents(boolean enable)
    {
        editProfileButton.setDisable(enable);
        artistNameLabel.setVisible(!enable);
        artistLocationLabel.setVisible(!enable);
        artistDescriptionLabel.setVisible(!enable);
        artistUsernameTextField.setVisible(enable);
        artistLocationTextField.setVisible(enable);
        artistDescriptionTextArea.setVisible(enable);
        //followButton.setVisible(!enable);
        //seeMusicButton.setVisible(!enable);
        artistUsernameTextField.setText(FreeTradeMusic.user.getUsername());
        artistLocationTextField.setText(FreeTradeMusic.user.getLocation());
        artistDescriptionTextArea.setText(FreeTradeMusic.user.getDescription());
        updateProfileButton.setVisible(enable);
    }

    public void onBuySong()
    {
        Song song = storeTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Buy Song");
        alert.setHeaderText("Buy Song");
        alert.setContentText("Are you sure you want to buy this song?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            error = DatabaseManager.getInstance().boughtSong(song);
            if(error == Error.NO_ERROR)
            {
                updateSongsTable("USER", DatabaseManager.getInstance().getUserSongs(
                        FreeTradeMusic.user.getUsername()));
            }
            else
            {
                alertUser("Error While Buying Song",
                        "There was an error while buying your song." +
                                " You have not been charged. Try again later.",
                        "ERROR");
            }
        }
    }

    public void onUpdateProfile()
    {
        String username = artistUsernameTextField.getText();
        String location = artistLocationTextField.getText();
        String description = artistDescriptionTextArea.getText();

        error = DatabaseManager.getInstance().updateProfile(username, location, description);

        if(error == Error.NO_ERROR)
        {
            FreeTradeMusic.user.setUsername(username);
            FreeTradeMusic.user.setLocation(location);
            FreeTradeMusic.user.setDescription(description);

            enableUpdateProfileComponents(false);
            onGoToProfile();
        }
    }

    /**
     * Displays the status messages located in the status bar.
     * @param type - The type of the status message.
     * @param message - The message to display.
     */
    public void setStatus(String type, String message)
    {
        if(type.equals("ERROR"))
            statusLabel.setStyle("-fx-text-fill: red");
        else
            statusLabel.setStyle("-fx-text-fill: white");

        statusLabel.setText(type + ": " + message);
    }

    private void setErrors()
    {
        for(TextField textField : textFields)
            textField.pseudoClassStateChanged(errorClass, false);

        for(Error error : errors)
        {
            switch(error)
            {
                case TITLE_EMPTY:
                    titleTextField.pseudoClassStateChanged(errorClass, true);
                    break;
                case GENRE_EMPTY:
                    genreTextField.pseudoClassStateChanged(errorClass, true);
                    break;
                case YEAR_EMPTY:
                    yearTextField.pseudoClassStateChanged(errorClass, true);
                    break;
                case NO_ERROR:
                    clearSubmissionForm();
            }
        }

        errors.clear();
    }

    /**
     * Displays error messages in the form of alerts.
     * @param title - The title of the alert.
     * @param errorMessage - The error message.
     */
    private void alertUser(String title, String errorMessage, String type)
    {
        Alert alert = new Alert(Alert.AlertType.NONE);

        switch (type)
        {
            case "INFORMATION":
                alert = new Alert(Alert.AlertType.INFORMATION);
                break;
            case "ERROR":
                alert = new Alert(Alert.AlertType.ERROR);
                break;
            case "CONFIRMATION":
                alert = new Alert(Alert.AlertType.CONFIRMATION);
        }

        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
