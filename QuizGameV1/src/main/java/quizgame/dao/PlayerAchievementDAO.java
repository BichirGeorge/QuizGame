package quizgame.dao;

import quizgame.model.PlayerAchievement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerAchievementDAO {
    private Connection connection;

    public PlayerAchievementDAO(Connection connection) {
        this.connection = connection;
    }
    public boolean createPlayerAchievement(PlayerAchievement playerAchievement) {
        String sql = "INSERT INTO PlayerAchievements (PlayerId, AchievementId, DateAwarded) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, playerAchievement.getPlayerId());
            stmt.setInt(2, playerAchievement.getAchievementId());
            stmt.setDate(3, playerAchievement.getDateAwarded());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<PlayerAchievement> getPlayerAchievements(int playerId) {
        List<PlayerAchievement> playerAchievements = new ArrayList<>();
        String sql = "SELECT * FROM PlayerAchievements WHERE PlayerId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, playerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int achievementId = rs.getInt("AchievementId");
                Date dateAwarded = rs.getDate("DateAwarded");
                playerAchievements.add(new PlayerAchievement(playerId, achievementId, dateAwarded));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playerAchievements;
    }

}
