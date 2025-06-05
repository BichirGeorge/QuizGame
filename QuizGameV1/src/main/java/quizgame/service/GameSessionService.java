package quizgame.service;

import quizgame.dao.GameSessionDAO;
import quizgame.model.GameSession;

import java.sql.Connection;

public class GameSessionService {
    private GameSessionDAO gameSessionDAO;

    public GameSessionService(Connection connection) {
        this.gameSessionDAO = new GameSessionDAO(connection);
    }

    public boolean createGameSession(int playerId, int topicId) {
        GameSession session = new GameSession(0, playerId, topicId, new java.sql.Timestamp(System.currentTimeMillis()));
        return gameSessionDAO.createGameSession(session);
    }
}
