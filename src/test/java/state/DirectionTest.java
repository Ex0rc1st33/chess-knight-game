package state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {

    @Test
    void testOf() {
        assertEquals(Direction.UPLEFT, Direction.of(-2, -1));
        assertEquals(Direction.UPRIGHT, Direction.of(-2, 1));
        assertEquals(Direction.RIGHTUP, Direction.of(-1, 2));
        assertEquals(Direction.RIGHTDOWN, Direction.of(1, 2));
        assertEquals(Direction.DOWNRIGHT, Direction.of(2, 1));
        assertEquals(Direction.DOWNLEFT, Direction.of(2, -1));
        assertEquals(Direction.LEFTDOWN, Direction.of(1, -2));
        assertEquals(Direction.LEFTUP, Direction.of(-1, -2));
    }

    @Test
    void testOf_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Direction.of(1, 1));
        assertThrows(IllegalArgumentException.class, () -> Direction.of(1, 0));
        assertThrows(IllegalArgumentException.class, () -> Direction.of(-3, -1));
        assertThrows(IllegalArgumentException.class, () -> Direction.of(2, 3));
    }

}
