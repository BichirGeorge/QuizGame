package quizgame.interfaces;

import quizgame.model.User;

public interface UserInterface {
    User registerUser(String name, String password, User.Role role);
    User loginUser(String name, String password);
}