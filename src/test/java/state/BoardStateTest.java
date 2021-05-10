package state;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BoardStateTest {

    BoardState state1 = new BoardState(); //original state; init

    BoardState state2 = new BoardState(Color.BLACK,
            new Knight(new Position(3, 1), Color.BLACK),
            new Knight(new Position(3, 2), Color.BLACK),
            new Knight(new Position(3, 0), Color.BLACK),
            new Knight(new Position(0, 2), Color.WHITE),
            new Knight(new Position(0, 0), Color.WHITE),
            new Knight(new Position(0, 1), Color.WHITE)); //a goal state

    BoardState state3 = new BoardState(Color.BLACK,
            new Knight(new Position(0, 0), Color.BLACK),
            new Knight(new Position(1, 1), Color.BLACK),
            new Knight(new Position(3, 1), Color.BLACK),
            new Knight(new Position(0, 2), Color.WHITE),
            new Knight(new Position(2, 0), Color.WHITE),
            new Knight(new Position(2, 2), Color.WHITE)); //a state with no legal moves (dead-end)

    BoardState state4 = new BoardState(Color.WHITE,
            new Knight(new Position(2, 1), Color.BLACK),
            new Knight(new Position(0, 1), Color.BLACK),
            new Knight(new Position(0, 2), Color.BLACK),
            new Knight(new Position(3, 0), Color.WHITE),
            new Knight(new Position(3, 1), Color.WHITE),
            new Knight(new Position(1, 1), Color.WHITE)); //a non-goal state

    @Test
    void testConstructor_invalid() {
        assertThrows(IllegalArgumentException.class, () -> new BoardState(Color.WHITE,
                new Knight(new Position(0, 0), Color.BLACK)));
        assertThrows(IllegalArgumentException.class, () -> new BoardState(Color.WHITE,
                new Knight(new Position(0, -1), Color.BLACK),
                new Knight(new Position(0, 1), Color.BLACK),
                new Knight(new Position(0, 2), Color.BLACK),
                new Knight(new Position(3, 0), Color.WHITE),
                new Knight(new Position(3, 1), Color.WHITE),
                new Knight(new Position(3, 2), Color.WHITE)));
        assertThrows(IllegalArgumentException.class, () -> new BoardState(Color.WHITE,
                new Knight(new Position(4, 0), Color.BLACK),
                new Knight(new Position(0, 1), Color.BLACK),
                new Knight(new Position(0, 2), Color.BLACK),
                new Knight(new Position(3, 0), Color.WHITE),
                new Knight(new Position(3, 1), Color.WHITE),
                new Knight(new Position(3, 2), Color.WHITE)));
        assertThrows(IllegalArgumentException.class, () -> new BoardState(Color.WHITE,
                new Knight(new Position(0, 0), Color.BLACK),
                new Knight(new Position(0, 0), Color.BLACK),
                new Knight(new Position(0, 0), Color.BLACK),
                new Knight(new Position(0, 0), Color.WHITE),
                new Knight(new Position(0, 0), Color.WHITE),
                new Knight(new Position(0, 0), Color.WHITE)));
        assertThrows(IllegalArgumentException.class, () -> new BoardState(Color.WHITE,
                new Knight(new Position(0, 0), Color.BLACK),
                new Knight(new Position(1, 1), Color.BLACK),
                new Knight(new Position(0, 2), Color.BLACK),
                new Knight(new Position(3, 0), Color.WHITE),
                new Knight(new Position(3, 1), Color.WHITE),
                new Knight(new Position(3, 2), Color.WHITE)));
    }

    @Test
    void testConstructor_valid() {
        assertDoesNotThrow(() -> new BoardState(Color.WHITE,
                new Knight(new Position(0, 0), Color.BLACK),
                new Knight(new Position(0, 1), Color.BLACK),
                new Knight(new Position(0, 2), Color.BLACK),
                new Knight(new Position(3, 0), Color.WHITE),
                new Knight(new Position(3, 1), Color.WHITE),
                new Knight(new Position(3, 2), Color.WHITE)));
        assertDoesNotThrow(() -> new BoardState(Color.WHITE,
                new Knight(new Position(3, 0), Color.BLACK),
                new Knight(new Position(3, 1), Color.BLACK),
                new Knight(new Position(3, 2), Color.BLACK),
                new Knight(new Position(0, 0), Color.WHITE),
                new Knight(new Position(0, 1), Color.WHITE),
                new Knight(new Position(0, 2), Color.WHITE)));
    }

    @Test
    void testIsGoal() {
        assertFalse(state1.isGoal());
        assertTrue(state2.isGoal());
        assertFalse(state3.isGoal());
        assertFalse(state4.isGoal());
    }

    @Test
    void testCanMove_state1() {
        assertFalse(state1.canMove(-1, Direction.UPLEFT));
        assertFalse(state1.canMove(6, Direction.UPLEFT));
        assertFalse(state1.canMove(0, Direction.DOWNRIGHT));
        assertFalse(state1.canMove(3, Direction.UPLEFT));
        assertFalse(state1.canMove(5, Direction.LEFTUP));
        assertTrue(state1.canMove(3, Direction.UPRIGHT));
    }

    @Test
    void testCanMove_state2() {
        assertFalse(state2.canMove(-1, Direction.UPLEFT));
        assertFalse(state2.canMove(6, Direction.UPLEFT));
        assertFalse(state2.canMove(3, Direction.DOWNLEFT));
        assertFalse(state2.canMove(2, Direction.UPLEFT));
        assertFalse(state2.canMove(1, Direction.LEFTUP));
        assertTrue(state2.canMove(2, Direction.UPRIGHT));
    }

    @Test
    void testCanMove_state3() {
        assertFalse(state3.canMove(-1, Direction.UPLEFT));
        assertFalse(state3.canMove(6, Direction.UPLEFT));
        assertFalse(state3.canMove(3, Direction.DOWNLEFT));
        assertFalse(state3.canMove(0, Direction.RIGHTUP));
        assertFalse(state3.canMove(0, Direction.RIGHTDOWN));
    }

    @Test
    void testCanMove_state4() {
        assertFalse(state4.canMove(-1, Direction.UPLEFT));
        assertFalse(state4.canMove(6, Direction.UPLEFT));
        assertFalse(state4.canMove(1, Direction.DOWNRIGHT));
        assertFalse(state4.canMove(3, Direction.LEFTUP));
        assertFalse(state4.canMove(3, Direction.UPRIGHT));
        assertFalse(state4.canMove(4, Direction.UPLEFT));
        assertTrue(state4.canMove(4, Direction.UPRIGHT));
    }

    @Test
    void testGetLegalMoves() {
        assertEquals(state1.getLegalMoves(0), EnumSet.noneOf(Direction.class));
        assertEquals(state1.getLegalMoves(3), EnumSet.of(Direction.UPRIGHT));
        assertEquals(state1.getLegalMoves(4), EnumSet.noneOf(Direction.class));
        assertEquals(state2.getLegalMoves(0), EnumSet.noneOf(Direction.class));
        assertEquals(state2.getLegalMoves(1), EnumSet.of(Direction.UPLEFT));
        assertEquals(state2.getLegalMoves(4), EnumSet.noneOf(Direction.class));
        assertEquals(state3.getLegalMoves(0), EnumSet.noneOf(Direction.class));
        assertEquals(state3.getLegalMoves(4), EnumSet.noneOf(Direction.class));
        assertEquals(state4.getLegalMoves(3), EnumSet.noneOf(Direction.class));
        assertEquals(state4.getLegalMoves(5), EnumSet.of(Direction.DOWNRIGHT));
        assertEquals(state4.getLegalMoves(0), EnumSet.noneOf(Direction.class));
    }

}
