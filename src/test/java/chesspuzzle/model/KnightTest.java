package chesspuzzle.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KnightTest {

    Knight knight;

    @BeforeEach
    void init() {
        knight = new Knight(new Position(0, 0), Color.BLACK);
    }

    @Test
    void testEquals() {
        assertTrue(knight.equals(knight));
        assertTrue(knight.equals(new Knight(new Position(0, 0), Color.BLACK)));
        assertFalse(knight.equals(new Knight(new Position(0, 0), Color.WHITE)));
        assertFalse(knight.equals(new Knight(new Position(1, 0), Color.BLACK)));
        assertFalse(knight.equals(null));
        assertFalse(knight.equals(new Knight(new Position(Integer.MAX_VALUE, Integer.MAX_VALUE), Color.BLACK)));
    }

    @Test
    void testHashCode() {
        assertTrue(knight.hashCode() == knight.hashCode());
        assertTrue(knight.hashCode() == knight.clone().hashCode());
        assertTrue(knight.hashCode() == new Knight(knight.getPosition(), Color.BLACK).hashCode());
        assertTrue(knight.hashCode() == new Knight(new Position(0, 0), Color.BLACK).hashCode());
        assertFalse(knight.hashCode() == new Knight(knight.getPosition(), Color.WHITE).hashCode());
        assertFalse(knight.hashCode() == new Knight(new Position(0, 1), Color.BLACK).hashCode());
    }

    @Test
    void testClone() {
        var clone = knight.clone();
        assertTrue(knight.equals(clone));
        assertNotSame(knight, clone);
    }

    @Test
    void testToString() {
        assertEquals("[(0,0),BLACK]", knight.toString());
        knight.getPosition().setTarget(Direction.DOWNRIGHT);
        assertEquals("[(2,1),BLACK]", knight.toString());
    }

}
