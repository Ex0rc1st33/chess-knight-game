package model;

import java.util.Objects;

/**
 * Represents a knight chess piece.
 */
public class Knight implements Cloneable {

    private Position position;
    private Color color;

    /**
     * Creates a {@code Knight} object.
     *
     * @param position the position (2D representation) of the knight piece
     * @param color    the color of the knight piece
     */
    public Knight(Position position, Color color) {
        this.position = position;
        this.color = color;
    }

    /**
     * {@return the position of the knight piece}
     */
    public Position getPosition() {
        return position;
    }

    /**
     * {@return the color of the knight piece}
     */
    public Color getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Knight)) {
            return false;
        }
        return ((Knight) o).position.equals(position) && ((Knight) o).color == color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, color);
    }

    @Override
    public Knight clone() {
        Knight copy;
        try {
            copy = (Knight) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
        copy.position = position.clone();
        copy.color = this.color;
        return copy;
    }

    @Override
    public String toString() {
        return String.format("[%s,%s]", position.toString(), color.toString());
    }
}
