package quizgame.factory;

import quizgame.model.Player;
import quizgame.model.User;

public class UserFactory {
    public static User createUser(int id, String name, String password, User.Role role) {
        if (role == User.Role.PLAYER) {
            return new Player(id, name, password, role, 0);
        } else {
            return new User(id, name, password, role);
        }
    }
    public static Player createPlayer(int id, String name, String password, int score) {
        return new Player(id, name, password, User.Role.PLAYER, score);
    }
}