package quizgame.service;

import quizgame.dao.PlayerAchievementDAO;
import quizgame.model.NoAchievementsException;
import quizgame.model.PlayerAchievement;

import java.sql.Connection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerAchievementService {
    private PlayerAchievementDAO playerAchievementDAO;

    public PlayerAchievementService(Connection connection) {
        this.playerAchievementDAO = new PlayerAchievementDAO(connection);
    }

    public boolean createPlayerAchievement(PlayerAchievement playerAchievement) {
        Set<Integer> awardedAchievements = new HashSet<>();
        List<PlayerAchievement> playerAchievements = playerAchievementDAO.getPlayerAchievements(playerAchievement.getPlayerId());
        for (PlayerAchievement pa : playerAchievements) {
            awardedAchievements.add(pa.getAchievementId());
        }
        if (!awardedAchievements.contains(playerAchievement.getAchievementId())) {
            return playerAchievementDAO.createPlayerAchievement(playerAchievement);
        } else {
            System.out.println("Player already has this achievement.");
            return false;
        }
    }

    public List<PlayerAchievement> getPlayerAchievements(int playerId) throws NoAchievementsException {
        List<PlayerAchievement> achievements = playerAchievementDAO.getPlayerAchievements(playerId);
        if (achievements == null || achievements.isEmpty()) {
            throw new NoAchievementsException("No achievements found for player with ID: " + playerId);
        }
        return achievements;
    }
}
