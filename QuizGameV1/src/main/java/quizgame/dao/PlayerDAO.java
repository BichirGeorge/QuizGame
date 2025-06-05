package quizgame.dao;

import quizgame.factory.UserFactory;
import quizgame.model.Player;
import quizgame.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO {
    private Connection connection;

    public PlayerDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean createPlayer(Player player) {
        String sql = "INSERT INTO Players (UserId, Score) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, player.getId());
            stmt.setInt(2, player.getScore());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePlayerScore(Player player) {
        String sql = "UPDATE Players SET Score = ? WHERE UserId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, player.getScore());
            stmt.setInt(2, player.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getPlayerScore(int userId) {
        String sql = "SELECT Score FROM Players WHERE UserId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("Score");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Player> getAllPlayersWithScores() {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT p.UserId, u.Name, u.Password, u.Role, p.Score " +
                "FROM Players p " +
                "JOIN Users u ON p.UserId = u.Id " +
                "WHERE u.Role = 'PLAYER' ";
        //      +   "ORDER BY p.Score DESC"
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("UserId");
                String name = rs.getString("Name");
                String password = rs.getString("Password");
                User.Role role = User.Role.valueOf(rs.getString("Role"));
                int score = rs.getInt("Score");

                Player player = UserFactory.createPlayer(id, name, password, score);
                players.add(player);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return players;
    }


}
