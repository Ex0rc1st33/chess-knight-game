package chesspuzzle.javafx;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;

public class ChessPuzzleApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Logger.info("Application started");
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/mainmenu.fxml"));
        stage.setTitle("JavaFX chess puzzle");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

}
