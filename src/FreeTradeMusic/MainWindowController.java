package FreeTradeMusic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

public class MainWindowController
{
    // TableView
    @FXML private TableView<Song> songsTableView;
    @FXML private TableColumn<Song, String> titleColumn;
    @FXML private TableColumn<Song, String> artistColumn;
    @FXML private TableColumn<Song, String> albumColumn;
    @FXML private TableColumn<Song, Integer> yearColumn;
    @FXML private TableColumn<Song, String> genreColumn;
    @FXML private TableColumn<Song, Integer> durationColumn;
    private ObservableList<Song> songs = FXCollections.observableArrayList();

    @FXML private ChoiceBox<String> filterChoiceBox;
    @FXML private TextField filterTextField;

    public void initialize()
    {
        songs = DatabaseManager.getInstance().getSongs();

        titleColumn.setCellValueFactory(
                new PropertyValueFactory<>("title"));

        artistColumn.setCellValueFactory(
                new PropertyValueFactory<>("artist"));

        albumColumn.setCellValueFactory(
                new PropertyValueFactory<>("album"));

        yearColumn.setCellValueFactory(
                new PropertyValueFactory<>("year"));

        genreColumn.setCellValueFactory(
                new PropertyValueFactory<>("genre"));

        durationColumn.setCellValueFactory(
                new PropertyValueFactory<>("duration"));

        FilteredList<Song> filteredData = new FilteredList<>(songs, p -> true);

        filterTextField.textProperty().addListener((observable, oldValue, newValue)
                -> filteredData.setPredicate(song ->
        {
            String filter = filterChoiceBox.getValue();
            // If filter text is empty, display all songs.
            if (newValue == null || newValue.isEmpty())
                return true;

            String lowerCaseFilter = newValue.toLowerCase();

            if (filter.equals("Title")
                    && song.getTitle().toLowerCase().contains(lowerCaseFilter))
                return true;
            else if (filter.equals("Artist")
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
        sortedData.comparatorProperty().bind(songsTableView.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        songsTableView.setItems(sortedData);

        filterChoiceBox.setItems(FXCollections.observableArrayList(
                "Title", "Artist", "Album", "Year",
                "Genre", "Favorite Artists"));
        filterChoiceBox.getSelectionModel().selectFirst();
    }

    public void updateSongsTable()
    {
        songs = DatabaseManager.getInstance().getSongs();
        songsTableView.setItems(songs);
        songsTableView.refresh();
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
            FreeTradeMusic.stage.setScene(FreeTradeMusic.loginScene);
            FreeTradeMusic.stage.setResizable(false);
        }
    }
}
