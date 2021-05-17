package chesspuzzle.results;

import chesspuzzle.results.jaxb.JAXBHelper;
import jakarta.xml.bind.JAXBException;
import org.tinylog.Logger;

import java.io.*;
import java.util.*;

public class ResultHelper {

    private final String dirPath;

    private final String fileName;

    private final int bound;

    public ResultHelper(String dirPath, String fileName, int bound) {
        this.dirPath = dirPath;
        this.fileName = fileName;
        this.bound = bound;
    }

    public void saveResult(GameResult gameResult) throws IOException, JAXBException {
        Logger.debug("Saving current game's results: {}", gameResult);
        validate();
        String path = dirPath + File.separator + fileName;
        Leaderboard leaderboard = JAXBHelper.fromXML(Leaderboard.class, new FileInputStream(path));
        if (leaderboard.getResults().isEmpty()) {
            leaderboard.getResults().add(gameResult);
        } else {
            insertIntoExistingLeaderboard(gameResult, leaderboard);
        }
        JAXBHelper.toXML(leaderboard, new FileOutputStream(path));
    }

    public List<GameResult> getResults(int n) throws IOException, JAXBException {
        Logger.debug("Getting top {} results", n);
        validate();
        String path = dirPath + File.separator + fileName;
        Leaderboard leaderboard = JAXBHelper.fromXML(Leaderboard.class, new FileInputStream(path));
        if (n <= leaderboard.getResults().size()) {
            return leaderboard.getResults().subList(0, n);
        } else {
            return leaderboard.getResults();
        }
    }

    public void clearResults() {
        Logger.debug("Clearing results");
        File file = new File(dirPath + File.separator + fileName);
        file.delete();
    }

    private void validate() throws IOException, JAXBException {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdir();
        }
        file = new File(dirPath + File.separator + fileName);
        if (!file.exists()) {
            file.createNewFile();
            JAXBHelper.toXML(new Leaderboard(new ArrayList<>()), new FileOutputStream(file));
        }
    }

    private void insertIntoExistingLeaderboard(GameResult gameResult, Leaderboard leaderboard) {
        int worstMoveCount = leaderboard.getResults().get(leaderboard.getResults().size() - 1).getMoveCount();
        if (leaderboard.getResults().size() < bound) {
            addElementAndSort(gameResult, leaderboard.getResults());
        } else {
            if (gameResult.getMoveCount() >= worstMoveCount) {
                Logger.debug("Player \"{}\" did not get into the leaderboard with {} moves", gameResult.getPlayerName(), gameResult.getMoveCount());
            } else {
                addElementAndSort(gameResult, leaderboard.getResults());
                leaderboard.getResults().remove(leaderboard.getResults().size() - 1);
            }
        }
    }

    private void addElementAndSort(GameResult gameResult, List<GameResult> gameResults) {
        gameResults.add(gameResult);
        gameResults.sort(Comparator.comparing(GameResult::getMoveCount));
    }

}
