package quizgame.dao;

import quizgame.model.GameSession;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameSessionDAO {
    private Connection connection;

    public GameSessionDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean createGameSession(GameSession gameSession) {
        String sql = "INSERT INTO GameSessions (PlayerId, TopicId) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, gameSession.getPlayerId());
            stmt.setInt(2, gameSession.getTopicId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
