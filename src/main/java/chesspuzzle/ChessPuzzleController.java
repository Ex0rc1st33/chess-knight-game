package chesspuzzle;

import chesspuzzle.model.*;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class ChessPuzzleController {

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

    private BoardState model = new BoardState();

    private SelectionPhase selectionPhase = SelectionPhase.SELECT_FROM;

    private List<Position> selectablePositions = new ArrayList<>();

    private Position selected;

    @FXML
    private void initialize() {
        createBoard();
        createKnights();
        setSelectablePositions();
        showSelectablePositions();
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
        Image image = new Image(getClass().getResourceAsStream("/" + ((i + j) % 2 == 0 ? "light" : "dark") + "_tile.png"));
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
        /*Image image = new Image(getClass().getResourceAsStream("/" + color + "_knight.png"));
        ImageView view = new ImageView();
        view.setImage(image);
        view.setFitHeight(100);
        view.setPreserveRatio(true);
        view.setSmooth(true);
        return view;*/
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
                    model.move(knightIndex, direction);
                    deselectSelectedPosition();
                    alterSelectionPhase();
                } else if (position.equals(selected)) {
                    deselectSelectedPosition();
                    alterSelectionPhase();
                }
            }
        }
        if (model.isGoal()) {
            Platform.exit();
            System.exit(0);
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
        tile.getStyleClass().add("selected");
        /*var arrow = new Label("\u25B2");
        arrow.setFont(new Font(30));
        arrow.setTextFill(javafx.scene.paint.Color.BLUE);
        tile.getChildren().add(arrow);
        System.out.println(tile.getChildren().size());*/

    }

    private void deselectSelectedPosition() {
        hideSelectedPosition();
        selected = null;
    }

    private void hideSelectedPosition() {
        var tile = getTile(selected);
        tile.getStyleClass().remove("selected");
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
            tile.getStyleClass().add("selectable");
            if (selectionPhase == SelectionPhase.SELECT_FROM) {
                var arrow = new Label("\u25B2");
                arrow.setFont(new Font(30));
                arrow.setTextFill(javafx.scene.paint.Color.GREEN);
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
            tile.getStyleClass().remove("selectable");
            if (selectionPhase == SelectionPhase.SELECT_TO) {
                tile.getChildren().remove(tile.getChildren().size() - 1);
            } else {
                tile.getChildren().remove(0);
            }
        }
    }

    private Circle createCircle() {
        Circle circle = new Circle();
        circle.setRadius(20);
        circle.setFill(javafx.scene.paint.Color.LIGHTGRAY);
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
        StackPane oldTile = getTile(oldPosition);
        StackPane newTile = getTile(newPosition);
        newTile.getChildren().addAll(oldTile.getChildren());
        //newTile.getChildren().remove(newTile.getChildren().size() - 1);
        oldTile.getChildren().clear();
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

}
