package quizgame.dao;

import quizgame.model.Topic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TopicDAO {
    private Connection connection;

    public TopicDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean createTopic(Topic topic) {
        String sql = "INSERT INTO Topics (Name, Description) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, topic.getName());
            stmt.setString(2, topic.getDescription());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Topic> getAllTopics() {
        List<Topic> topics = new ArrayList<>();
        String sql = "SELECT * FROM Topics";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("Id");
                String name = rs.getString("Name");
                String description = rs.getString("Description");
                topics.add(new Topic(id, name, description));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topics;
    }

    public Topic getTopicById(int id) {
        String sql = "SELECT * FROM Topics WHERE Id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("Name");
                String description = rs.getString("Description");
                return new Topic(id, name, description);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
