package model;

import java.util.Objects;

/**
 * Represents a 2D position.
 */
public class Position implements Cloneable {

    private int row;
    private int col;

    /**
     * Creates a {@code Position} object.
     *
     * @param row the row coordinate of the 2D position
     * @param col the column coordinate of the 2D position
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * {@return the row coordinate of the 2D position}
     */
    public int getRow() {
        return row;
    }

    /**
     * {@return the column coordinate of the 2D position}
     */
    public int getCol() {
        return col;
    }

    /**
     * {@return the new {@code Position} whose vertical and horizontal distances
     * from the current {@code Position} are equal to the changes described in the
     * {@code Direction} given}
     *
     * @param direction the direction that describes a change in the coordinates
     */
    public Position getTarget(Direction direction) {
        return new Position(row + direction.getRowChange(), col + direction.getColChange());
    }

    /**
     * Changes the current {@code Position} by the coordinate changes described by the {@code Direction} given.
     *
     * @param direction the direction that describes a change in the coordinates
     */
    public void setTarget(Direction direction) {
        row = row + direction.getRowChange();
        col = col + direction.getColChange();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }
        return ((Position) o).row == row && ((Position) o).col == col;
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
        return String.format("(%d,%d)", row, col);
    }

}