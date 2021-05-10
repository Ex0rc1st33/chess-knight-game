package state;

import java.util.Objects;

public class Knight implements Cloneable {

    private Position position;

    private Color color;

    public Knight(Position position, Color color) {
        this.position = position;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
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
