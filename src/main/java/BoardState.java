import java.util.*;

public class BoardState implements Cloneable {

    public static final int BOARD_ROWCOUNT = 4;

    public static final int BOARD_COLUMNCOUNT = 3;

    public static final int[] BLACK_KNIGHTS = new int[]{0, 1, 2};

    public static final int[] WHITE_KNIGHTS = new int[]{3, 4, 5};

    private Position[] positions;

    private Color color;

    public BoardState() {
        this(Color.WHITE, new Position(0, 0),
                new Position(0, 1),
                new Position(0, 2),
                new Position(3, 0),
                new Position(3, 1),
                new Position(3, 2)
        );
    }

    public BoardState(Color color, Position... positions) {
        checkPositions(positions);
        this.positions = deepClone(positions);
        this.color = color;
    }

    private void checkPositions(Position[] positions) {
        if (positions.length != 6) {
            throw new IllegalArgumentException();
        }
        for (var position : positions) {
            if (!isOnBoard(position)) {
                throw new IllegalArgumentException();
            }
        }
        for (int i = 0; i < positions.length; i++) {
            for (int j = 0; j < positions.length; j++) {
                if (i != j && positions[i].equals(positions[j])) {
                    throw new IllegalArgumentException();
                }
            }
        }
        List<Position> attackedTiles = new ArrayList<>();
        int from;
        int to;
        for (int i = 0; i < positions.length; i++) {
            if (i < WHITE_KNIGHTS[0]) {
                from = WHITE_KNIGHTS[0];
                to = WHITE_KNIGHTS[WHITE_KNIGHTS.length - 1];
            } else {
                from = BLACK_KNIGHTS[0];
                to = BLACK_KNIGHTS[BLACK_KNIGHTS.length - 1];
            }
            for (int j = from; j <= to; j++) {
                attackedTiles.addAll(getAttackedTiles(positions[j]));
                for (Position pos : attackedTiles) {
                    if (pos.equals(positions[i]))
                        throw new IllegalArgumentException();
                }
                attackedTiles.clear();
            }
        }
    }

    public Position getPosition(int n) {
        return positions[n].clone();
    }

    public Color getColor() {
        return color;
    }

    public boolean isGoal() {
        return haveSwitchedPositions(BLACK_KNIGHTS, WHITE_KNIGHTS);
    }

    public boolean canMove(int knightIndex, Direction direction) {
        if ((color == Color.WHITE && knightIndex >= BLACK_KNIGHTS[0] && knightIndex <= BLACK_KNIGHTS[BLACK_KNIGHTS.length - 1]) ||
                (color == Color.BLACK && knightIndex >= WHITE_KNIGHTS[0] && knightIndex <= WHITE_KNIGHTS[WHITE_KNIGHTS.length - 1])) {
            return false;
        }

        Position target = positions[knightIndex].getTarget(direction);
        if (!isOnBoard(target) || !isEmpty(target)) {
            return false;
        }

        List<Position> attackedTiles = new ArrayList<>();
        int from;
        int to;
        if (color == Color.WHITE) {
            from = BLACK_KNIGHTS[0];
            to = BLACK_KNIGHTS[BLACK_KNIGHTS.length - 1];
        } else {
            from = WHITE_KNIGHTS[0];
            to = WHITE_KNIGHTS[WHITE_KNIGHTS.length - 1];
        }
        for (int i = from; i <= to; i++) {
            attackedTiles.addAll(getAttackedTiles(positions[i]));
            for (Position pos : attackedTiles) {
                if (pos.equals(target)) {
                    return false;
                }
            }
            attackedTiles.clear();
        }
        return true;
    }

    public void move(int knightIndex, Direction direction) {
        positions[knightIndex].setTarget(direction);
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

    private boolean haveSwitchedPositions(int[] blacks, int[] whites) {
        for (int black : blacks) {
            if (positions[black].getRow() != 3)
                return false;
        }
        for (int white : whites) {
            if (positions[white].getRow() != 0)
                return false;
        }
        return true;
    }

    private boolean isOnBoard(Position position) {
        return position.getRow() >= 0 && position.getRow() < BOARD_ROWCOUNT &&
                position.getCol() >= 0 && position.getCol() < BOARD_COLUMNCOUNT;
    }

    private boolean isEmpty(Position position) {
        for (var p : positions) {
            if (p.equals(position)) {
                return false;
            }
        }
        return true;
    }

    private List<Position> getAttackedTiles(Position position) {
        List<Position> attacked = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            Position pos = position.getTarget(direction);
            if (isOnBoard(pos))
                attacked.add(pos);
        }
        return attacked;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BoardState)) {
            return false;
        }
        return Arrays.equals(positions, ((BoardState) o).positions);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(positions);
    }

    @Override
    public BoardState clone() {
        BoardState copy;
        try {
            copy = (BoardState) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
        copy.positions = deepClone(positions);
        return copy;
    }

    @Override
    public String toString() {
        var sj = new StringJoiner(",", "[", "]");
        for (var position : positions) {
            sj.add(position.toString());
        }
        return sj.toString();
    }

    private static Position[] deepClone(Position[] a) {
        Position[] copy = a.clone();
        for (var i = 0; i < a.length; i++) {
            copy[i] = a[i].clone();
        }
        return copy;
    }

}