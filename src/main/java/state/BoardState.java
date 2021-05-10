package state;

import java.util.*;

public class BoardState implements Cloneable {

    public static final int BOARD_ROWCOUNT = 4;

    public static final int BOARD_COLUMNCOUNT = 3;

    private Knight[] knights;

    private Color color;

    public BoardState() {
        this(Color.WHITE, new Knight(new Position(0, 0), Color.BLACK),
                new Knight(new Position(0, 1), Color.BLACK),
                new Knight(new Position(0, 2), Color.BLACK),
                new Knight(new Position(3, 0), Color.WHITE),
                new Knight(new Position(3, 1), Color.WHITE),
                new Knight(new Position(3, 2), Color.WHITE)
        );
    }

    public BoardState(Color color, Knight... knights) {
        this.knights = deepClone(knights);
        this.color = color;
        checkPositions(knights);
    }

    private void checkPositions(Knight[] knights) {
        if (knights.length != 6) {
            throw new IllegalArgumentException();
        }

        for (var knight : knights) {
            if (!isOnBoard(knight.getPosition())) {
                throw new IllegalArgumentException();
            }
        }

        for (int i = 0; i < knights.length; i++) {
            for (int j = 0; j < knights.length; j++) {
                if (i != j && knights[i].getPosition().equals(knights[j].getPosition())) {
                    throw new IllegalArgumentException();
                }
            }
        }

        for (int i = 0; i < knights.length; i++) {
            if (isAttacked(knights[i], knights[i].getPosition(), knights)) {
                throw new IllegalArgumentException();
            }
        }
    }

    public Knight[] getKnights() {
        return knights;
    }

    public Knight getKnight(int n) {
        return knights[n].clone();
    }

    public Color getColor() {
        return color;
    }

    public boolean isGoal() {
        for (Knight knight : knights) {
            if (knight.getColor() == Color.BLACK && knight.getPosition().getRow() != 3) {
                return false;
            }
            if (knight.getColor() == Color.WHITE && knight.getPosition().getRow() != 0) {
                return false;
            }
        }
        return true;
    }

    public boolean canMove(int knightIndex, Direction direction) {
        if (knightIndex < 0 || knightIndex >= knights.length) {
            return false;
        }

        if (!color.equals(knights[knightIndex].getColor())) {
            return false;
        }

        Position target = knights[knightIndex].getPosition().getTarget(direction);
        if (!isOnBoard(target) || !isEmpty(target)) {
            return false;
        }

        if (isAttacked(knights[knightIndex], target, knights)) {
            return false;
        }
        return true;
    }

    public void move(int knightIndex, Direction direction) {
        knights[knightIndex].getPosition().setTarget(direction);
        color = color == Color.WHITE ? Color.BLACK : Color.WHITE;
    }

    public EnumSet<Direction> getLegalMoves(int knightIndex) {
        var legalMoves = EnumSet.noneOf(Direction.class);
        for (var direction : Direction.values()) {
            if (canMove(knightIndex, direction)) {
                legalMoves.add(direction);
            }
        }
        return legalMoves;
    }

    private boolean isOnBoard(Position position) {
        return position.getRow() >= 0 && position.getRow() < BOARD_ROWCOUNT &&
                position.getCol() >= 0 && position.getCol() < BOARD_COLUMNCOUNT;
    }

    private boolean isEmpty(Position position) {
        for (Knight knight : knights) {
            if (knight.getPosition().equals(position)) {
                return false;
            }
        }
        return true;
    }

    private boolean isAttacked(Knight knight, Position target, Knight[] knights) {
        Position attackedFrom;
        for (Direction dir : Direction.values()) {
            attackedFrom = target.getTarget(dir);
            if (isOnBoard(attackedFrom) && !isEmpty(attackedFrom)) {
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

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BoardState)) {
            return false;
        }
        return Arrays.equals(knights, ((BoardState) o).knights) && color == ((BoardState) o).color;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(knights) + color.hashCode();
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
        copy.color = this.color;
        return copy;
    }

    @Override
    public String toString() {
        var sj = new StringJoiner(",", "{", "}");
        for (var position : knights) {
            sj.add(position.toString());
        }
        sj.add(color.toString());
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