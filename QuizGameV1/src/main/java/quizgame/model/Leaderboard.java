package quizgame.model;

import quizgame.service.PlayerService;
import java.util.Map;

public class Leaderboard {
    private static Leaderboard instance;
    private PlayerService playerService;

    private Leaderboard(PlayerService playerService) {
        this.playerService = playerService;
    }

    public static Leaderboard getInstance(PlayerService playerService) {
        if (instance == null) {
            instance = new Leaderboard(playerService);
        }
        return instance;
    }

    public void displayLeaderboard() {
        Map<String, Integer> leaderboard = playerService.getLeaderboard();

        System.out.println("\nLeaderboard:");
        if (leaderboard.isEmpty()) {
            System.out.println("No scores available yeti.");
        } else {
            int rank = 1;
            for (Map.Entry<String, Integer> entry : leaderboard.entrySet()) {
                System.out.println(rank + ". " + entry.getKey() + ": " + entry.getValue() + " points");
                rank++;
            }
        }
    }
}
