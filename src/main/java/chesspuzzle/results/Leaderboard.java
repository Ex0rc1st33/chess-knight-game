package chesspuzzle.results;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Class representing the leaderboard, which consists of previous game results.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Leaderboard {

    /**
     * The list of the existing game results.
     */
    @XmlElementWrapper(name = "results")
    @XmlElement(name = "result")
    private List<GameResult> results;

}
