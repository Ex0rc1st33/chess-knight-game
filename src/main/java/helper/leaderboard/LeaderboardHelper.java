package helper.leaderboard;

import helper.jaxb.JAXBHelper;
import jakarta.xml.bind.JAXBException;
import org.tinylog.Logger;

import java.io.*;
import java.util.*;

public class LeaderboardHelper {

    private String dirPath;

    private String fileName;

    private int bound;

    public LeaderboardHelper(String dirPath, String fileName, int bound) {
        this.dirPath = dirPath;
        this.fileName = fileName;
        this.bound = bound;
    }

    public void validate() {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdir();
        }
        file = new File(dirPath + File.separator + fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                JAXBHelper.toXML(new Leaderboard(new ArrayList<>()), new FileOutputStream(file));
            } catch (IOException | JAXBException e) {
                e.printStackTrace();
            }
        }
    }

    public void save(GameResult gameResult) throws FileNotFoundException, JAXBException {
        validate();
        String path = dirPath + File.separator + fileName;
        Leaderboard leaderboard = JAXBHelper.fromXML(Leaderboard.class, new FileInputStream(path));
        if (leaderboard.getResults().isEmpty()) {
            leaderboard.setResults(Arrays.asList(gameResult));
        } else {
            int worstMoveCount = leaderboard.getResults().get(leaderboard.getResults().size() - 1).getMoveCount();
            if (leaderboard.getResults().size() < bound) {
                addElementAndSort(gameResult, leaderboard.getResults());
            } else {
                if (gameResult.getMoveCount() >= worstMoveCount) {
                    Logger.info("Player \"{}\" did not get into the leaderboards with {} moves", gameResult.getPlayerName(), gameResult.getMoveCount());
                } else {
                    addElementAndSort(gameResult, leaderboard.getResults());
                    leaderboard.getResults().remove(leaderboard.getResults().size() - 1);
                }
            }
        }
        JAXBHelper.toXML(leaderboard, new FileOutputStream(path));
    }

    private void addElementAndSort(GameResult gameResult, List<GameResult> gameResults) {
        gameResults.add(gameResult);
        gameResults.sort(Comparator.comparing(GameResult::getMoveCount));
    }

    public List<GameResult> findTop() throws FileNotFoundException, JAXBException {
        validate();
        String path = dirPath + File.separator + fileName;
        Leaderboard leaderboard = JAXBHelper.fromXML(Leaderboard.class, new FileInputStream(path));
        return leaderboard.getResults();
    }

}
