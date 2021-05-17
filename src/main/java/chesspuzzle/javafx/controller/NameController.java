package chesspuzzle.javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

public class NameController {

    @FXML
    private TextField name;

    @FXML
    private void initialize() {
        name.setPromptText("Enter a valid name");
        name.getStyleClass().add("name");
    }

    @FXML
    private void handleNext(MouseEvent event) throws IOException {
        if (!name.getText().isEmpty()) {
            Logger.debug("\"{}\" button pressed, switching scenes", ((Button) event.getSource()).getText());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));
            Parent root = loader.load();
            GameController controller = loader.getController();
            Logger.debug("Setting current player's name to \"{}\"", name.getText());
            controller.setPlayerName(name.getText());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            Logger.debug("\"{}\" button pressed, invalid name entered, not switching scenes", ((Button) event.getSource()).getText());
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid name!");
            alert.setHeaderText("Please enter a valid name.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleBack(MouseEvent event) throws IOException {
        Logger.debug("\"{}\" button pressed, switching scenes", ((Button) event.getSource()).getText());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/mainmenu.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

}
