package quizgame.model;

import java.sql.Date;

public class PlayerAchievement {
    private int playerId;
    private int achievementId;
    private Date dateAwarded;

    public PlayerAchievement(int playerId, int achievementId, Date dateAwarded) {
        this.playerId = playerId;
        this.achievementId = achievementId;
        this.dateAwarded = dateAwarded;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getAchievementId() {
        return achievementId;
    }

    public Date getDateAwarded() {
        return dateAwarded;
    }
}
