package chesspuzzle.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Objects;

/**
 * Represents a knight chess piece.
 */
public class Knight implements Cloneable {

    private ObjectProperty<Position> position = new SimpleObjectProperty<>();
    private final Color color;

    /**
     * Creates a {@code Knight} object.
     *
     * @param position the position (2D representation) of the knight piece
     * @param color    the color of the knight piece
     */
    public Knight(Position position, Color color) {
        this.position.set(position);
        this.color = color;
    }

    /**
     * {@return the position of the knight piece}
     */
    public Position getPosition() {
        return position.get();
    }

    /**
     * {@return the wrapper object containing the position of the knight}
     */
    public ObjectProperty<Position> positionObjectProperty() {
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
        return ((Knight) o).getPosition().equals(this.getPosition()) && ((Knight) o).color == color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getPosition(), color);
    }

    @Override
    public Knight clone() {
        Knight copy;
        try {
            copy = (Knight) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
        copy.position = new SimpleObjectProperty<>(this.getPosition().clone());
        return copy;
    }

    @Override
    public String toString() {
        return String.format("[%s,%s]", this.getPosition().toString(), color.toString());
    }
}
