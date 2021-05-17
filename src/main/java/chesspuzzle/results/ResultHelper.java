package chesspuzzle.results;

import chesspuzzle.results.jaxb.JAXBHelper;
import jakarta.xml.bind.JAXBException;
import org.tinylog.Logger;

import java.io.*;
import java.util.*;

/**
 * Helper class for managing the leaderboard system.
 */
public class ResultHelper {

    private static final String dirPath = System.getProperty("user.home") + File.separator + ".leaderboard_results";

    private static final String fileName = "leaderboard.xml";

    /*
    The maximum number of results which can be stored on the leaderboard.
     */
    private static final int bound = 50;

    /**
     * Saves a single result into a specified file.
     *
     * @param gameResult the result to be saved
     * @throws IOException   If any I/O errors occurred
     * @throws JAXBException If any unexpected errors occurred during XML serialization
     */
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

    /**
     * Returns a list of the top {@code n} results of the leaderboard. The {@code n} parameter specified is
     * an upper bound: if there are more (or an equal number of) results than {@code n}, this method returns
     * exactly {@code n} results, otherwise returns less.
     *
     * @param n the maximum number of results to be returned
     * @return a list of the top {@code n} results of the leaderboard
     * @throws IOException   If any I/O errors occurred
     * @throws JAXBException If any unexpected errors occurred during XML serialization
     */
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

    /**
     * Clears the leaderboard results by deleting the file containing the records.
     */
    public static void clearResults() {
        Logger.debug("Clearing results");
        File file = new File(dirPath + File.separator + fileName);
        file.delete();
    }

    /*
    This helper method checks whether the file with the path given in this class exists. If it does not exist, then
    it creates the file after creating the correct parent directory too. After creating the file, it also inserts
    an empty list of game results into the file.
     */
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

    /*
    This helper method inserts a game result into an existing, non-empty leaderboard, modifying the leaderboard.
     */
    private static void insertIntoExistingLeaderboard(GameResult gameResult, Leaderboard leaderboard) {
        if (leaderboard.getResults().size() < bound) {
            addElementAndSort(gameResult, leaderboard.getResults());
        } else {
            int worstMoveCount = leaderboard.getResults().get(leaderboard.getResults().size() - 1).getMoveCount();
            if (gameResult.getMoveCount() >= worstMoveCount) {
                Logger.debug("Player \"{}\" did not get into the leaderboard with {} moves",
                        gameResult.getPlayerName(), gameResult.getMoveCount());
            } else {
                addElementAndSort(gameResult, leaderboard.getResults());
                leaderboard.getResults().remove(leaderboard.getResults().size() - 1);
            }
        }
    }

    /*
    This helper method adds a game result to an existing list of game results, then sorts it.
     */
    private static void addElementAndSort(GameResult gameResult, List<GameResult> gameResults) {
        gameResults.add(gameResult);
        gameResults.sort(Comparator.comparing(GameResult::getMoveCount));
    }

}
