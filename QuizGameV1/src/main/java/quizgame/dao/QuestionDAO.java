package quizgame.dao;

import quizgame.model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {
    private Connection connection;

    public QuestionDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean deleteQuestion(int questionId) {
        String sql = "DELETE FROM Questions WHERE Id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, questionId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createQuestion(Question question) {
        String sql = "INSERT INTO Questions (TopicId, QuestionText, Option1, Option2, Option3, Option4, CorrectAnswerIndex) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, question.getTopicId());
            stmt.setString(2, question.getQuestionText());
            stmt.setString(3, question.getOption1());
            stmt.setString(4, question.getOption2());
            stmt.setString(5, question.getOption3());
            stmt.setString(6, question.getOption4());
            stmt.setInt(7, question.getCorrectAnswerIndex());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Question> getQuestionsByTopic(int topicId) {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM Questions WHERE TopicId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, topicId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("Id");
                String questionText = rs.getString("QuestionText");
                String option1 = rs.getString("Option1");
                String option2 = rs.getString("Option2");
                String option3 = rs.getString("Option3");
                String option4 = rs.getString("Option4");
                int correctAnswerIndex = rs.getInt("CorrectAnswerIndex");
                questions.add(Question.builder(topicId, questionText)
                        .id(id)
                        .option1(option1)
                        .option2(option2)
                        .option3(option3)
                        .option4(option4)
                        .correctAnswerIndex(correctAnswerIndex)
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }
}
