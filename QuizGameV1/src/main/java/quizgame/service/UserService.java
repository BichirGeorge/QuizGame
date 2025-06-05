package quizgame.service;

import quizgame.dao.PlayerDAO;
import quizgame.dao.UserDAO;
import quizgame.factory.UserFactory;
import quizgame.interfaces.UserInterface;
import quizgame.model.Player;
import quizgame.model.User;

import java.sql.Connection;

public class UserService implements UserInterface {
    private UserDAO userDAO;
    private PlayerDAO playerDAO;

    public UserService(Connection connection) {
        this.userDAO = new UserDAO(connection);
        this.playerDAO = new PlayerDAO(connection);
    }

    @Override
    public User registerUser(String name, String password, User.Role role) {
        User user = UserFactory.createUser(0, name, password, role);
        int userId = userDAO.createUserAndReturnId(user);
        if (userId != -1) {
            user.setId(userId);
            if (role == User.Role.PLAYER) {
                Player player = UserFactory.createPlayer(userId, user.getName(), user.getPassword(), 0);
                playerDAO.createPlayer(player);
            }
            return user;
        }
        return null;
    }

    @Override
    public User loginUser(String name, String password) {
        return userDAO.authenticateUser(name, password);
    }
}

