package chesspuzzle.model;

/**
 * Represents the eight main directions.
 */
public enum Direction {

    UPLEFT(-2, -1),
    UPRIGHT(-2, 1),
    RIGHTUP(-1, 2),
    RIGHTDOWN(1, 2),
    DOWNRIGHT(2, 1),
    DOWNLEFT(2, -1),
    LEFTDOWN(1, -2),
    LEFTUP(-1, -2);

    private final int rowChange;
    private final int colChange;

    /**
     * Creates a {@code Direction} object.
     *
     * @param rowChange the change in the row coordinate described by this {@code Direction}
     * @param colChange the change in the column coordinate described by this {@code Direction}
     */
    Direction(int rowChange, int colChange) {
        this.rowChange = rowChange;
        this.colChange = colChange;
    }

    /**
     * {@return the change in the row coordinate when moving to the direction}
     */
    public int getRowChange() {
        return rowChange;
    }

    /**
     * {@return the change in the column coordinate when moving to the direction}
     */
    public int getColChange() {
        return colChange;
    }

    /**
     * {@return the {@code Direction} that corresponds to the coordinate changes specified}
     *
     * @param rowChange the change in the row coordinate
     * @param colChange the change in the column coordinate
     */
    public static Direction of(int rowChange, int colChange) {
        for (var direction : values()) {
            if (direction.rowChange == rowChange && direction.colChange == colChange) {
                return direction;
            }
        }
        throw new IllegalArgumentException();
    }

}