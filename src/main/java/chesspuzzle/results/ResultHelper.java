package chesspuzzle.results;

import chesspuzzle.results.jaxb.JAXBHelper;
import jakarta.xml.bind.JAXBException;
import org.tinylog.Logger;

import java.io.*;
import java.util.*;

public class ResultHelper {

    private static final String dirPath = System.getProperty("user.home") + File.separator + "leaderboard_results";

    private static final String fileName = "leaderboard.xml";

    private static final int bound = 50;

    public static void saveResult(GameResult gameResult) throws IOException, JAXBException {
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

    public static List<GameResult> getResults(int n) throws IOException, JAXBException {
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

    public static void clearResults() {
        Logger.debug("Clearing results");
        File file = new File(dirPath + File.separator + fileName);
        file.delete();
    }

    private static void validate() throws IOException, JAXBException {
        File sourceFile = new File(dirPath + File.separator + fileName);
        if (!sourceFile.exists()) {
            File sourceDir = new File(dirPath);
            if (!sourceDir.exists()) {
                sourceDir.mkdir();
            }
            sourceFile.createNewFile();
            JAXBHelper.toXML(new Leaderboard(new ArrayList<>()), new FileOutputStream(sourceFile));
        }
    }

    private static void insertIntoExistingLeaderboard(GameResult gameResult, Leaderboard leaderboard) {
        if (leaderboard.getResults().size() < bound) {
            addElementAndSort(gameResult, leaderboard.getResults());
        } else {
            int worstMoveCount = leaderboard.getResults().get(leaderboard.getResults().size() - 1).getMoveCount();
            if (gameResult.getMoveCount() >= worstMoveCount) {
                Logger.debug("Player \"{}\" did not get into the leaderboard with {} moves", gameResult.getPlayerName(), gameResult.getMoveCount());
            } else {
                addElementAndSort(gameResult, leaderboard.getResults());
                leaderboard.getResults().remove(leaderboard.getResults().size() - 1);
            }
        }
    }

    private static void addElementAndSort(GameResult gameResult, List<GameResult> gameResults) {
        gameResults.add(gameResult);
        gameResults.sort(Comparator.comparing(GameResult::getMoveCount));
    }

}
