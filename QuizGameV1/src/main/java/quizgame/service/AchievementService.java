package quizgame.service;

import quizgame.dao.AchievementDAO;
import quizgame.model.Achievement;
import quizgame.model.NoAchievementsException;

import java.sql.Connection;

public class AchievementService {
    private AchievementDAO achievementDAO;

    public AchievementService(Connection connection) {
        this.achievementDAO = new AchievementDAO(connection);
    }

    public boolean createAchievement(String name, String description) {
        Achievement achievement = new Achievement(0, name, description);
        return achievementDAO.createAchievement(achievement);
    }

    public Achievement getAchievementById(int id) throws NoAchievementsException {
        Achievement achievement = achievementDAO.getAchievementById(id);
        if (achievement == null) {
            throw new NoAchievementsException("No achievement found with ID: " + id);
        }
        return achievement;
    }
}
