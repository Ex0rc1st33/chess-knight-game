package chesspuzzle.results;

import chesspuzzle.results.jaxb.LocalDateTimeAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Class representing the results of a single game played.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"playerName", "moveCount", "created"})
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GameResult {

    // The name of the player.
    private String playerName;

    // The number of moves made by the player.
    private int moveCount;

    // The time of this result's creation.
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime created;

}
