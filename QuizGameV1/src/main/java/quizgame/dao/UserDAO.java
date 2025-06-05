package quizgame.dao;

import quizgame.factory.UserFactory;
import quizgame.model.User;
import java.sql.*;

public class UserDAO {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public int createUserAndReturnId(User user) {
        String sql = "INSERT INTO Users (Name, Password, Role) VALUES (?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole().name());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public User authenticateUser(String name, String password) {
        String sql = "SELECT * FROM Users WHERE Name = ? AND Password = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("Id");
                String userName = rs.getString("Name");
                String userPassword = rs.getString("Password");
                User.Role role = User.Role.valueOf(rs.getString("Role"));
                return UserFactory.createUser(id, userName, userPassword, role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public User getUserById(int userId) {
        String sql = "SELECT * FROM Users WHERE Id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("Name");
                String password = rs.getString("Password");
                User.Role role = User.Role.valueOf(rs.getString("Role"));
                return UserFactory.createUser(userId, name, password, role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
