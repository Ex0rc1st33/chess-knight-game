package chesspuzzle.results;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Leaderboard {

    @XmlElementWrapper(name = "results")
    @XmlElement(name = "result")
    private List<GameResult> results;

}
