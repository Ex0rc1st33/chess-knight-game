package chesspuzzle.javafx.controller;

import chesspuzzle.model.*;
import chesspuzzle.results.GameResult;
import chesspuzzle.results.ResultHelper;
import jakarta.xml.bind.JAXBException;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameController {

    private enum SelectionPhase {
        SELECT_FROM, SELECT_TO;

        public SelectionPhase alter() {
            return switch (this) {
                case SELECT_FROM -> SELECT_TO;
                case SELECT_TO -> SELECT_FROM;
            };
        }
    }

    @FXML
    private GridPane table;

    @FXML
    private TextField numberOfMoves;

    @FXML
    private Label nextColor;

    private BoardState state = new BoardState();

    private SelectionPhase selectionPhase = SelectionPhase.SELECT_FROM;

    private List<Position> selectablePositions = new ArrayList<>();

    private Position selected;

    private StringProperty playerName = new SimpleStringProperty();

    private IntegerProperty moveCount = new SimpleIntegerProperty();

    @FXML
    private void initialize() {
        Logger.debug("Initializing the board game");
        createBoard();
        createKnights();
        setSelectablePositions();
        showSelectablePositions();
        state.gameOverBooleanProperty().addListener(this::isGoalHandler);
        numberOfMoves.textProperty().bind(moveCount.asString());
        moveCount.set(0);
        nextColor.setText(state.getNextColor().toString() + " piece to move next");
    }

    public void setPlayerName(String playerName) {
        this.playerName.set(playerName);
    }

    private void createBoard() {
        table.getStyleClass().add("table");
        for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getColumnCount(); j++) {
                var tile = createTile(i, j);
                table.add(tile, j, i);
            }
        }
    }

    private StackPane createTile(int i, int j) {
        var tile = new StackPane();
        tile.getStyleClass().add("tile");
        tile.setOnMouseClicked(this::handleMouseClick);
        Image image = new Image(getClass().getResourceAsStream("/images/" + ((i + j) % 2 == 0 ? "light" : "dark") + "_tile.png"));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, null, null, null, backgroundSize);
        tile.setBackground(new Background(backgroundImage));
        return tile;
    }

    private void createKnights() {
        int size = state.getKnights().length;
        for (int i = 0; i < size; i++) {
            state.positionObjectProperty(i).addListener(this::knightPositionChangeHandler);
            var knight = createKnight(state.getKnight(i).getColor());
            getTile(state.getKnight(i).getPosition()).getChildren().add(knight);
        }
    }

    private Label createKnight(chesspuzzle.model.Color color) {
        var knight = new Label((color == Color.BLACK ? "\u265E" : "\u2658"));
        knight.setFont(new Font(100));
        knight.setTextFill(javafx.scene.paint.Color.BLACK);
        return knight;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var tile = (StackPane) event.getSource();
        handleClickOnTile(new Position(GridPane.getRowIndex(tile), GridPane.getColumnIndex(tile)));
    }

    private void handleClickOnTile(Position position) {
        Logger.debug("Click on tile {}", position);
        switch (selectionPhase) {
            case SELECT_FROM -> {
                if (selectablePositions.contains(position)) {
                    selectPosition(position);
                    alterSelectionPhase();
                }
            }
            case SELECT_TO -> {
                if (selectablePositions.contains(position)) {
                    int knightIndex = state.getKnightIndex(selected).getAsInt();
                    Direction direction = Direction.of(position.getRow() - selected.getRow(), position.getCol() - selected.getCol());
                    deselectSelectedPosition();
                    Logger.debug("Moving knight [{}] {}", knightIndex, direction);
                    moveCount.set(moveCount.get() + 1);
                    state.move(knightIndex, direction);
                    nextColor.setText(state.getNextColor().toString() + " piece to move next");
                    alterSelectionPhase();
                } else if (position.equals(selected)) {
                    deselectSelectedPosition();
                    alterSelectionPhase();
                }
            }
        }
    }

    private void alterSelectionPhase() {
        selectionPhase = selectionPhase.alter();
        hideSelectablePositions();
        setSelectablePositions();
        showSelectablePositions();
    }

    private void selectPosition(Position position) {
        selected = position;
        showSelectedPosition();
    }

    private void showSelectedPosition() {
        var tile = getTile(selected);
        tile.getChildren().remove(tile.getChildren().size() - 1);
        var arrow = createArrow(javafx.scene.paint.Color.BLUE, 25);
        tile.getChildren().add(arrow);
    }

    private void deselectSelectedPosition() {
        hideSelectedPosition();
        selected = null;
    }

    private void hideSelectedPosition() {
        var tile = getTile(selected);
        tile.getChildren().remove(tile.getChildren().size() - 1);
    }

    private void hideSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var tile = getTile(selectablePosition);
            if (selectionPhase == SelectionPhase.SELECT_FROM) {
                tile.getChildren().remove(0);
            } else {
                if (!selectablePosition.equals(selected)) {
                    tile.getChildren().remove(tile.getChildren().size() - 1);
                }
            }
        }
    }

    private void setSelectablePositions() {
        selectablePositions.clear();
        switch (selectionPhase) {
            case SELECT_FROM -> {
                for (Knight knight : state.getKnights()) {
                    if (knight.getColor() == state.getNextColor()) {
                        selectablePositions.add(knight.getPosition());
                    }
                }
            }
            case SELECT_TO -> {
                int knightIndex = state.getKnightIndex(selected).getAsInt();
                for (Direction direction : state.getLegalMoves(knightIndex)) {
                    selectablePositions.add(selected.getTarget(direction));
                }
            }
        }
    }

    private void showSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var tile = getTile(selectablePosition);
            if (selectionPhase == SelectionPhase.SELECT_FROM) {
                Label arrow = createArrow(javafx.scene.paint.Color.GREEN, 25);
                tile.getChildren().add(arrow);
            } else {
                Circle circle = createCircle(javafx.scene.paint.Color.GRAY, 20, 0.7);
                tile.getChildren().add(circle);
            }
        }
    }

    private Label createArrow(javafx.scene.paint.Color color, double size) {
        Label arrow = new Label("\u27A4 ");
        arrow.setFont(new Font(size));
        arrow.setTextFill(color);
        arrow.setTranslateX(-50);
        arrow.setTranslateY(-50);
        arrow.setRotate(45);
        return arrow;
    }

    private Circle createCircle(javafx.scene.paint.Color color, double size, double opacity) {
        Circle circle = new Circle();
        circle.setRadius(size);
        circle.setFill(color);
        circle.setOpacity(opacity);
        return circle;
    }

    private StackPane getTile(Position position) {
        for (var child : table.getChildren()) {
            if (GridPane.getRowIndex(child) == position.getRow() && GridPane.getColumnIndex(child) == position.getCol()) {
                return (StackPane) child;
            }
        }
        throw new AssertionError();
    }

    private void knightPositionChangeHandler(ObservableValue<? extends Position> observable, Position oldPosition, Position newPosition) {
        Logger.debug("Move: {} -> {}", oldPosition, newPosition);
        StackPane oldTile = getTile(oldPosition);
        StackPane newTile = getTile(newPosition);
        newTile.getChildren().addAll(oldTile.getChildren());
        oldTile.getChildren().clear();
    }

    private void isGoalHandler(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
        Logger.debug("Value of gameOver attribute changed: {} -> {}", oldValue, newValue);
        if (newValue) {
            saveResults();
            handleGameOver();
        }
    }

    private void saveResults() {
        try {
            ResultHelper.saveResult(new GameResult(playerName.get(), moveCount.get(), LocalDateTime.now()));
        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        }
    }

    private void handleGameOver() {
        ButtonType mainMenu = new ButtonType("Main menu");
        ButtonType close = new ButtonType("Close application");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Game over", mainMenu, close);
        alert.setTitle("Game over!");
        alert.setHeaderText("Congratulations, puzzle completed!");
        alert.setContentText("Do you want to quit to main menu?");
        alert.setGraphic(createImageView("/images/medal.png", 80));
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(close);
        if (button == mainMenu) {
            handleExitToMainMenu();
        } else {
            handleExit();
        }
    }

    private ImageView createImageView(String url, int height) {
        Image image = new Image(getClass().getResourceAsStream(url));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        return imageView;
    }

    @FXML
    private void handleExitToMainMenu() {
        Logger.debug("\"Exit\" button pressed, switching scenes");
        Stage stage = (Stage) table.getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/mainmenu.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            handleExit();
        }
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void handleExit() {
        Logger.info("Application exited");
        Platform.exit();
    }

    @FXML
    private void handleReset(MouseEvent event) {
        Logger.debug("\"{}\" button pressed, resetting the puzzle", ((Button) event.getSource()).getText());
        table.getChildren().removeAll(table.getChildren());
        state = new BoardState();
        selectionPhase = SelectionPhase.SELECT_FROM;
        selectablePositions.clear();
        selected = null;
        initialize();
    }

}
