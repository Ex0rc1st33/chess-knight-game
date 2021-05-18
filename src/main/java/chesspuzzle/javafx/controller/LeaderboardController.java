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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LeaderboardController {

    @FXML
    private TableView<GameResult> table;

    @FXML
    private TableColumn<GameResult, String> playerName;

    @FXML
    private TableColumn<GameResult, Integer> moveCount;

    @FXML
    private TableColumn<GameResult, LocalDateTime> created;

    @FXML
    private void initialize() throws IOException, JAXBException {
        Logger.debug("Initializing leaderboard");
        List<GameResult> topRecords = ResultHelper.getResults(10);
        playerName.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        moveCount.setCellValueFactory(new PropertyValueFactory<>("moveCount"));
        created.setCellValueFactory(new PropertyValueFactory<>("created"));
        created.setCellFactory(column -> new TableCell<>() {
            private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.format(formatter));
                }
            }
        });
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
        ResultHelper.clearResults();
        initialize();
    }

}
