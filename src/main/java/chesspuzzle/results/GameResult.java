package chesspuzzle.results;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class representing the results of a single game played.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"playerName", "moveCount"})
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GameResult {

    // The name of the player.
    private String playerName;

    // The number of moves made by the player.
    private int moveCount;

}
