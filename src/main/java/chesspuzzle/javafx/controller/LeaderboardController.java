package chesspuzzle.javafx.controller;

import chesspuzzle.results.GameResult;
import chesspuzzle.results.ResultHelper;
import jakarta.xml.bind.JAXBException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LeaderboardController {

    @FXML
    private TableView<GameResult> table;

    @FXML
    private TableColumn<GameResult, String> playerName;

    @FXML
    private TableColumn<GameResult, Integer> moveCount;

    ResultHelper resultHelper;

    @FXML
    private void initialize() throws IOException, JAXBException {
        Logger.debug("Initializing leaderboard");
        resultHelper = new ResultHelper(System.getProperty("user.home") + File.separator + "leaderboard_results",
                "leaderboard.xml",
                50);
        List<GameResult> topRecords = resultHelper.getResults(10);
        playerName.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        moveCount.setCellValueFactory(new PropertyValueFactory<>("moveCount"));
        ObservableList<GameResult> observableResults = FXCollections.observableArrayList();
        observableResults.addAll(topRecords);
        table.setItems(observableResults);
    }

    @FXML
    private void handleBack(MouseEvent event) throws IOException {
        Logger.debug("\"{}\" button pressed, switching scenes", ((Button) event.getSource()).getText());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/mainmenu.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleResetLeaderboard(MouseEvent event) throws IOException, JAXBException {
        Logger.debug("\"{}\" button pressed, resetting leaderboard", ((Button) event.getSource()).getText());
        resultHelper.clearResults();
        initialize();
    }

}
