# chess-knight-puzzle

JavaFX puzzle game implementation based on the Model-View-Controller architectural pattern. Records are stored in **XML** format using [JAXB](https://projects.eclipse.org/projects/ee4j.jaxb).

*The rules of the puzzle*:
- The puzzle is played on a 4x3 board (4 rows, 3 columns).
- There are 6 knight pieces on the board: 3 dark-coloured, and 3 light-coloured. The dark pieces are located on the top, the light pieces are located on the bottom row of the board.
- The knights can move according to the rules of chess (in an L-shape), with an extra restriction: each knight can only move to a tile which isn't attacked by any of the enemy knights.
- The goal of the player is to switch the places of the dark and light-coloured knights (so the dark knights should be in the bottom row, the light knights should be in the top row).
- The player has to move the black and white knights in turns, alternately. The game starts by moving a light-coloured knight.