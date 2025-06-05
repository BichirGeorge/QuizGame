package quizgame.service;

import quizgame.dao.TopicDAO;
import quizgame.model.Topic;

import java.sql.Connection;
import java.util.List;

public class TopicService {
    private TopicDAO topicDAO;

    public TopicService(Connection connection) {
        this.topicDAO = new TopicDAO(connection);
    }

    public List<Topic> getAllTopics() {
        return topicDAO.getAllTopics();
    }

    public Topic getTopicById(int id) {
        return topicDAO.getTopicById(id);
    }

    public boolean createTopic(String name, String description) {
        Topic topic = new Topic(0, name, description);
        return topicDAO.createTopic(topic);
    }
}
