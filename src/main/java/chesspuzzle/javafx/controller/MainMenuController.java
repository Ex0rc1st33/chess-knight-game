package chesspuzzle.javafx.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private void handleNewGame(MouseEvent event) throws IOException {
        Logger.debug("\"{}\" button pressed, switching scenes", ((Button) event.getSource()).getText());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        switchScenes(stage, "/fxml/nameinput.fxml");
    }

    @FXML
    private void handleLeaderboard(MouseEvent event) throws IOException {
        Logger.debug("\"{}\" button pressed, switching scenes", ((Button) event.getSource()).getText());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        switchScenes(stage, "/fxml/leaderboard.fxml");
    }

    @FXML
    private void handleRules(MouseEvent event) throws IOException {
        Logger.debug("\"{}\" button pressed, switching scenes", ((Button) event.getSource()).getText());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        switchScenes(stage, "/fxml/rules.fxml");
    }

    @FXML
    private void handleExit() {
        Logger.info("Application exited");
        Platform.exit();
    }

    private void switchScenes(Stage stage, String resource) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(resource));
        stage.setScene(new Scene(root));
        stage.show();
    }

}
