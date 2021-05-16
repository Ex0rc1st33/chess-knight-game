package chesspuzzle.controller;

import chesspuzzle.model.*;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameController {

    private enum SelectionPhase {
        SELECT_FROM,
        SELECT_TO;

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

    private BoardState model = new BoardState();

    private SelectionPhase selectionPhase = SelectionPhase.SELECT_FROM;

    private List<Position> selectablePositions = new ArrayList<>();

    private Position selected;

    private StringProperty name = new SimpleStringProperty();

    private IntegerProperty moveCount = new SimpleIntegerProperty();

    @FXML
    private void initialize() {
        createBoard();
        createKnights();
        setSelectablePositions();
        showSelectablePositions();
        model.gameOverBooleanProperty().addListener(this::isGoalHandler);
        numberOfMoves.textProperty().bind(moveCount.asString());
        moveCount.set(0);

    }

    public void setName(String name) {
        this.name.set(name);
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
        Background background = new Background(backgroundImage);
        tile.setBackground(background);
        return tile;
    }

    private void createKnights() {
        int size = model.getKnights().length;
        for (int i = 0; i < size; i++) {
            model.positionObjectProperty(i).addListener(this::knightPositionChange);
            var knight = createKnight(model.getKnight(i).getColor());
            getTile(model.getKnight(i).getPosition()).getChildren().add(knight);
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
        Logger.info("Click on square {}", position);
        switch (selectionPhase) {
            case SELECT_FROM -> {
                if (selectablePositions.contains(position)) {
                    selectPosition(position);
                    alterSelectionPhase();
                }
            }
            case SELECT_TO -> {
                if (selectablePositions.contains(position)) {
                    int knightIndex = model.getKnightIndex(selected).getAsInt();
                    Direction direction = Direction.of(position.getRow() - selected.getRow(), position.getCol() - selected.getCol());
                    deselectSelectedPosition();
                    Logger.info("Moving piece {} {}", knightIndex, direction);
                    moveCount.set(moveCount.get() + 1);
                    model.move(knightIndex, direction);
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
        var arrow = createArrow(javafx.scene.paint.Color.BLUE);
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

    private void setSelectablePositions() {
        selectablePositions.clear();
        switch (selectionPhase) {
            case SELECT_FROM -> {
                for (Knight knight : model.getKnights()) {
                    if (knight.getColor() == model.getNextColor()) {
                        selectablePositions.add(knight.getPosition());
                    }
                }
            }
            case SELECT_TO -> {
                int knightIndex = model.getKnightIndex(selected).getAsInt();
                for (Direction direction : model.getLegalMoves(knightIndex)) {
                    selectablePositions.add(selected.getTarget(direction));
                }
            }
        }
    }

    private void showSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var tile = getTile(selectablePosition);
            if (selectionPhase == SelectionPhase.SELECT_FROM) {
                var arrow = createArrow(javafx.scene.paint.Color.GREEN);
                tile.getChildren().add(arrow);
            } else {
                Circle circle = createCircle();
                tile.getChildren().add(circle);
            }
        }
    }

    private void hideSelectablePositions() {
        for (var selectablePosition : selectablePositions) {
            var tile = getTile(selectablePosition);
            if (selectionPhase == SelectionPhase.SELECT_TO) {
                if (!selectablePosition.equals(selected)) {
                    tile.getChildren().remove(tile.getChildren().size() - 1);
                }
            } else {
                tile.getChildren().remove(0);
            }
        }
    }

    private Label createArrow(javafx.scene.paint.Color color) {
        var arrow = new Label("\u25E4");
        arrow.setFont(new Font(40));
        arrow.setTextFill(color);
        arrow.setTranslateX(-60);
        arrow.setTranslateY(-60);
        return arrow;
    }

    private Circle createCircle() {
        Circle circle = new Circle();
        circle.setRadius(20);
        circle.setFill(javafx.scene.paint.Color.GRAY);
        circle.setOpacity(0.7);
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

    private void knightPositionChange(ObservableValue<? extends Position> observable, Position oldPosition, Position newPosition) {
        Logger.info("Move: {} -> {}", oldPosition, newPosition);
        StackPane oldTile = getTile(oldPosition);
        StackPane newTile = getTile(newPosition);
        newTile.getChildren().addAll(oldTile.getChildren());
        oldTile.getChildren().clear();
    }

    private void isGoalHandler(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
        if (newValue) {
            handleGameOver();
        }
    }

    private void handleGameOver() {
        ButtonType mainMenu = new ButtonType("Main menu");
        ButtonType close = new ButtonType("Close application");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Game over", mainMenu, close);
        alert.setTitle("Game over!");
        alert.setHeaderText("Congratulations, puzzle completed!");
        alert.setContentText("Do you want to quit to main menu?");
        alert.setGraphic(createImage("/images/medal.png", 80));
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(close);
        if (button == mainMenu) {
            handleExitToMainMenu();
        } else {
            handleExit();
        }
    }

    private ImageView createImage(String url, int height) {
        Image image = new Image(url);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        return imageView;
    }

    @FXML
    private void handleExitToMainMenu() {
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
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void handleReset() {
        table.getChildren().removeAll(table.getChildren());
        model = new BoardState();
        selectionPhase = SelectionPhase.SELECT_FROM;
        selectablePositions.clear();
        selected = null;
        initialize();
    }

    @FXML
    private void handleColorScheme(MouseEvent event) throws IOException {
        /*Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(""));
        stage.setTitle("JavaFX chess puzzle");
        stage.setScene(new Scene(root));
        stage.show();*/
    }

}
