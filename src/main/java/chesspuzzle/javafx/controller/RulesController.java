package chesspuzzle.javafx.controller;

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

public class RulesController {

    @FXML
    private void handleBack(MouseEvent event) throws IOException {
        Logger.debug("\"{}\" button pressed, switching scenes", ((Button) event.getSource()).getText());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/mainmenu.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

}
