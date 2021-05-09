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

    Direction(int rowChange, int colChange) {
        this.rowChange = rowChange;
        this.colChange = colChange;
    }

    public int getRowChange() {
        return rowChange;
    }

    public int getColChange() {
        return colChange;
    }

    public static Direction of(int rowChange, int colChange) {
        for (var direction : values()) {
            if (direction.rowChange == rowChange && direction.colChange == colChange) {
                return direction;
            }
        }
        throw new IllegalArgumentException();
    }

}