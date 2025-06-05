package quizgame.service;

import quizgame.dao.PlayerDAO;
import quizgame.dao.UserDAO;
import quizgame.factory.UserFactory;
import quizgame.model.Player;
import quizgame.model.User;

import java.sql.Connection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlayerService {
    private PlayerDAO playerDAO;
    private UserDAO userDAO;

    public PlayerService(Connection connection) {
        this.playerDAO = new PlayerDAO(connection);
        this.userDAO = new UserDAO(connection);
    }

    public void updateScore(int userId, int score) {
        User user = userDAO.getUserById(userId);

        if (user != null) {
            int currentScore = playerDAO.getPlayerScore(userId);
            int totalScore = currentScore + score;
            Player player = UserFactory.createPlayer(user.getId(), user.getName(), user.getPassword(), totalScore);
            boolean updated = playerDAO.updatePlayerScore(player);
            if (!updated) {
                System.out.println("Failed to update score.");
            }
        } else {
            System.out.println("User not found.");
        }
    }
    public Map<String, Integer> getLeaderboard() {
        List<Player> players = playerDAO.getAllPlayersWithScores();
        Map<String, Integer> leaderboard = new LinkedHashMap<>();

        Collections.sort(players);

        for (Player player : players) {
            leaderboard.put(player.getName(), player.getScore());
        }

        return leaderboard;
    }
}
