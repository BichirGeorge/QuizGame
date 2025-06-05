package quizgame.dao;

import quizgame.model.Achievement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AchievementDAO {
    private Connection connection;

    public AchievementDAO(Connection connection) {
        this.connection = connection;
    }

    public Achievement getAchievementById(int id) {
        String sql = "SELECT * FROM Achievements WHERE Id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Achievement(
                        rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getString("Description")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Achievement> getAllAchievements() {
        List<Achievement> achievements = new ArrayList<>();
        String sql = "SELECT * FROM Achievements";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                achievements.add(new Achievement(
                        rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getString("Description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return achievements;
    }
    public boolean createAchievement(Achievement achievement) {
        String sql = "INSERT INTO Achievements (Name, Description) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, achievement.getName());
            stmt.setString(2, achievement.getDescription());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
