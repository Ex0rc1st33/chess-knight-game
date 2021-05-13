package chesspuzzle.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    Position position;

    @BeforeEach
    void init() {
        position = new Position(0, 0);
    }

    @Test
    void testGetTarget() {
        assertEquals(new Position(-2, -1), position.getTarget(Direction.UPLEFT));
        assertEquals(new Position(-2, 1), position.getTarget(Direction.UPRIGHT));
        assertEquals(new Position(-1, 2), position.getTarget(Direction.RIGHTUP));
        assertEquals(new Position(1, 2), position.getTarget(Direction.RIGHTDOWN));
        assertEquals(new Position(2, 1), position.getTarget(Direction.DOWNRIGHT));
        assertEquals(new Position(2, -1), position.getTarget(Direction.DOWNLEFT));
        assertEquals(new Position(1, -2), position.getTarget(Direction.LEFTDOWN));
        assertEquals(new Position(-1, -2), position.getTarget(Direction.LEFTUP));
    }

    @Test
    void testSetTarget_upleft() {
        position.setTarget(Direction.UPLEFT);
        assertEquals(new Position(-2, -1), position);
    }

    @Test
    void testSetTarget_upright() {
        position.setTarget(Direction.UPRIGHT);
        assertEquals(new Position(-2, 1), position);
    }

    @Test
    void testSetTarget_rightup() {
        position.setTarget(Direction.RIGHTUP);
        assertEquals(new Position(-1, 2), position);
    }

    @Test
    void testSetTarget_rightdown() {
        position.setTarget(Direction.RIGHTDOWN);
        assertEquals(new Position(1, 2), position);
    }

    @Test
    void testSetTarget_downright() {
        position.setTarget(Direction.DOWNRIGHT);
        assertEquals(new Position(2, 1), position);
    }

    @Test
    void testSetTarget_downleft() {
        position.setTarget(Direction.DOWNLEFT);
        assertEquals(new Position(2, -1), position);
    }

    @Test
    void testSetTarget_leftdown() {
        position.setTarget(Direction.LEFTDOWN);
        assertEquals(new Position(1, -2), position);
    }

    @Test
    void testSetTarget_leftup() {
        position.setTarget(Direction.LEFTUP);
        assertEquals(new Position(-1, -2), position);
    }

    @Test
    void testEquals() {
        assertTrue(position.equals(position));
        assertTrue(position.equals(new Position(position.getRow(), position.getCol())));
        assertTrue(new Position(-2, -1).equals(new Position(0, 0).getTarget(Direction.UPLEFT)));
        assertFalse(position.equals(null));
        assertFalse(position.equals(new Position(Integer.MAX_VALUE, Integer.MAX_VALUE)));

    }

    @Test
    void testHashCode() {
        assertTrue(position.hashCode() == position.hashCode());
        assertTrue(position.hashCode() == position.clone().hashCode());
        assertTrue(position.hashCode() == new Position(position.getRow(), position.getCol()).hashCode());
    }

    @Test
    void testClone() {
        var clone = position.clone();
        assertTrue(position.equals(clone));
        assertNotSame(position, clone);
    }

    @Test
    void testToString() {
        assertEquals("(0,0)", position.toString());
        assertEquals("(-2,-1)", position.getTarget(Direction.UPLEFT).toString());
    }

}
