package state;

import java.util.Objects;

public class Position implements Cloneable {

    private int row;
    private int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Position getTarget(Direction direction) {
        return new Position(row + direction.getRowChange(), col + direction.getColChange());
    }

    public void setTarget(Direction direction) {
        row = row + direction.getRowChange();
        col = col + direction.getColChange();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        return (o instanceof Position p) && p.row == row && p.col == col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public Position clone() {
        Position copy;
        try {
            copy = (Position) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
        return copy;
    }

    @Override
    public String toString() {
        return String.format("[%d,%d]", row, col);
    }

}