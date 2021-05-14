package chesspuzzle.model;

import javafx.beans.property.*;

import java.util.*;

/**
 * Represents the state of the chess puzzle.
 */
public class BoardState implements Cloneable {

    /**
     * The number of rows on the board.
     */
    public static final int BOARD_ROWCOUNT = 4;

    /**
     * The number of columns on the board.
     */
    public static final int BOARD_COLUMNCOUNT = 3;

    /*
    The array which contains the Knight objects represented in this state.
    Each knight has a position, and a color.
    */
    private Knight[] knights;

    // The color of the knight piece to be moved next.
    private Color nextColor;

    private BooleanProperty gameOver = new SimpleBooleanProperty();

    /**
     * Creates a {@code BoardState} object that corresponds to the starting state of the chess puzzle.
     */
    public BoardState() {
        this(Color.WHITE, new Knight(new Position(0, 0), Color.BLACK),
                new Knight(new Position(0, 1), Color.BLACK),
                new Knight(new Position(0, 2), Color.BLACK),
                new Knight(new Position(3, 0), Color.WHITE),
                new Knight(new Position(3, 1), Color.WHITE),
                new Knight(new Position(3, 2), Color.WHITE)
        );
    }

    /**
     * Creates a {@code BoardState} object initializing the color of the knight piece to be moved next
     * and the knights to be represented in this state. The constructor expects a {@code Color} object,
     * and an array of six {@code Knight} objects.
     *
     * @param nextColor the color of the chess piece to be moved next
     * @param knights   the initial knight pieces
     */
    public BoardState(Color nextColor, Knight... knights) {
        checkKnights(knights);
        this.knights = deepClone(knights);
        this.nextColor = nextColor;
        gameOver.set(isGoal());
    }

    /**
     * {@return the array of knights in this state}
     */
    public Knight[] getKnights() {
        return knights;
    }

    /**
     * {@return a knight with the specified index}
     *
     * @param n the number (index) of the knight
     */
    public Knight getKnight(int n) {
        return knights[n];
    }

    /**
     * {@return the color of the knight piece to be moved next}
     */
    public Color getNextColor() {
        return nextColor;
    }

    public ObjectProperty<Position> positionObjectProperty(int pieceNumber) {
        return knights[pieceNumber].positionObjectProperty();
    }

    public BooleanProperty gameOverBooleanProperty() {
        return gameOver;
    }

    public boolean getGameOver() {
        return gameOver.get();
    }

    public OptionalInt getKnightIndex(Position position) {
        for (int i = 0; i < knights.length; i++) {
            if (knights[i].getPosition().equals(position)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    /*
    Checks if the given Knight objects correspond to a valid state of the puzzle,
    if they don't, throws an IllegalArgumentException.
     */
    private void checkKnights(Knight[] knights) {
        // Checks if there aren't exactly six knights on the board.
        if (knights.length != 6) {
            throw new IllegalArgumentException();
        }

        // Checks whether the knights' positions are outside the play area.
        for (var knight : knights) {
            if (!isOnBoard(knight.getPosition())) {
                throw new IllegalArgumentException();
            }
        }

        // Checks whether any of the two knights have the same positions.
        for (int i = 0; i < knights.length; i++) {
            for (int j = 0; j < knights.length; j++) {
                if (i != j && knights[i].getPosition().equals(knights[j].getPosition())) {
                    throw new IllegalArgumentException();
                }
            }
        }

        // Checks whether any of the knights is in a position which is attacked by an enemy knight.
        for (Knight knight : knights) {
            if (isAttacked(knight, knight.getPosition(), knights)) {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * {@return whether the knight piece with the given index can move in the direction specified}
     *
     * @param knightIndex the index of the knight to be checked
     * @param direction   the direction in which the knight is intended to be moved
     */
    public boolean canMove(int knightIndex, Direction direction) {
        // Checks if the specified index is invalid.
        if (knightIndex < 0 || knightIndex >= knights.length) {
            return false;
        }

        // Checks if the specified knight's color doesn't match the color of the piece to be moved next.
        if (!nextColor.equals(knights[knightIndex].getColor())) {
            return false;
        }

        // Checks whether the specified knight's new position would be out of the play area, or the tile
        // corresponding to the new position is already occupied on the board.
        Position target = knights[knightIndex].getPosition().getTarget(direction);
        if (!isOnBoard(target) || !isEmpty(target, knights)) {
            return false;
        }

        // Checks whether the specified knight's new position is attacked by enemy knights.
        if (isAttacked(knights[knightIndex], target, knights)) {
            return false;
        }
        return true;
    }

    /**
     * Moves the knight piece with the given index in the direction specified.
     *
     * @param knightIndex the index of the knight to be moved
     * @param direction   the direction in which the knight is moved
     */
    public void move(int knightIndex, Direction direction) {
        Position newPos = knights[knightIndex].getPosition().getTarget(direction);
        knights[knightIndex].positionObjectProperty().set(newPos);
        nextColor = nextColor == Color.WHITE ? Color.BLACK : Color.WHITE;
        gameOver.set(isGoal());
    }

    /**
     * {@return the set of directions in which the knight piece with the given index can be moved}
     *
     * @param knightIndex the index of the knight to be checked
     */
    public EnumSet<Direction> getLegalMoves(int knightIndex) {
        var legalMoves = EnumSet.noneOf(Direction.class);
        for (var direction : Direction.values()) {
            if (canMove(knightIndex, direction)) {
                legalMoves.add(direction);
            }
        }
        return legalMoves;
    }

    /**
     * {@return whether the chess puzzle is solved}
     */
    public boolean isGoal() {
        for (Knight knight : knights) {
            // Check if the black knights are all in the bottom row
            if (knight.getColor() == Color.BLACK && knight.getPosition().getRow() != 3) {
                return false;
            }
            // Check if the white knights are all in the top row
            if (knight.getColor() == Color.WHITE && knight.getPosition().getRow() != 0) {
                return false;
            }
        }
        return true;
    }

    /*
    Checks if the given knight's target position is attacked by any of the enemy knights.
     */
    private boolean isAttacked(Knight knight, Position target, Knight[] knights) {
        Position attackedFrom;
        for (Direction dir : Direction.values()) {
            attackedFrom = target.getTarget(dir);
            if (isOnBoard(attackedFrom) && !isEmpty(attackedFrom, knights)) {
                for (int j = 0; j < knights.length; j++) {
                    if (knights[j].getPosition().equals(attackedFrom) &&
                            !knights[j].getColor().equals(knight.getColor())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isOnBoard(Position position) {
        return position.getRow() >= 0 && position.getRow() < BOARD_ROWCOUNT &&
                position.getCol() >= 0 && position.getCol() < BOARD_COLUMNCOUNT;
    }

    private boolean isEmpty(Position position, Knight[] knights) {
        for (Knight knight : knights) {
            if (knight.getPosition().equals(position)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BoardState)) {
            return false;
        }
        return Arrays.equals(knights, ((BoardState) o).knights) && nextColor == ((BoardState) o).nextColor;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(knights) + nextColor.hashCode();
    }

    @Override
    public BoardState clone() {
        BoardState copy;
        try {
            copy = (BoardState) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
        copy.knights = deepClone(knights);
        copy.nextColor = this.nextColor;
        return copy;
    }

    @Override
    public String toString() {
        var sj = new StringJoiner(",", "{", "}");
        for (var position : knights) {
            sj.add(position.toString());
        }
        sj.add(nextColor.toString());
        return sj.toString();
    }

    private static Knight[] deepClone(Knight[] k) {
        Knight[] copy = k.clone();
        for (var i = 0; i < k.length; i++) {
            copy[i] = k[i].clone();
        }
        return copy;
    }

}