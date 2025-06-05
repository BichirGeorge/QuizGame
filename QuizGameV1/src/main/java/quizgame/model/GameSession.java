package quizgame.model;

import java.sql.Timestamp;

public class GameSession {
    private int id;
    private int playerId;
    private int topicId;
    private Timestamp date;

    public GameSession(int id, int playerId, int topicId, Timestamp date) {
        this.id = id;
        this.playerId = playerId;
        this.topicId = topicId;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getTopicId() {
        return topicId;
    }
}
