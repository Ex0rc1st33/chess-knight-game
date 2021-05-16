package chesspuzzle.controller;

import helper.leaderboard.GameResult;
import helper.leaderboard.LeaderboardHelper;
import jakarta.xml.bind.JAXBException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class LeaderboardController {

    LeaderboardHelper leaderboardHelper;

    @FXML
    private TableView<GameResult> table;

    @FXML
    private TableColumn<GameResult, String> playerName;

    @FXML
    private TableColumn<GameResult, Integer> moveCount;

    @FXML
    private void initialize() throws FileNotFoundException, JAXBException {
        LeaderboardHelper leaderboardHelper = new LeaderboardHelper(System.getProperty("user.home") + File.separator + "leaderboard_results",
                "leaderboard.xml",
                10);
        List<GameResult> topRecords = leaderboardHelper.findTop();
        playerName.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        moveCount.setCellValueFactory(new PropertyValueFactory<>("moveCount"));
        ObservableList<GameResult> observableResults = FXCollections.observableArrayList();
        observableResults.addAll(topRecords);
        table.setItems(observableResults);
    }

    @FXML
    private void handleBack(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/mainmenu.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

}
