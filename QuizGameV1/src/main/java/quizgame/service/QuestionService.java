package quizgame.service;

import quizgame.dao.QuestionDAO;
import quizgame.model.NoQuestionsInTopicException;
import quizgame.model.Question;
import quizgame.model.Topic;

import java.sql.Connection;
import java.util.List;

public class QuestionService {
    private QuestionDAO questionDAO;
    private TopicService topicService;

    public QuestionService(Connection connection) {
        this.questionDAO = new QuestionDAO(connection);
        this.topicService = new TopicService(connection);
    }

    public boolean deleteQuestion(int questionId) {
        return questionDAO.deleteQuestion(questionId);
    }

    public List<Question> getQuestionsByTopic(int topicId) throws NoQuestionsInTopicException {
        List<Question> questions = questionDAO.getQuestionsByTopic(topicId);
        if (questions == null || questions.isEmpty()) {
            Topic topic = topicService.getTopicById(topicId);
            String topicName = (topic != null) ? topic.getName() : "Unknown";
            throw new NoQuestionsInTopicException(topicName);
        }
        return questions;
    }

    public boolean createQuestion(int topicId, String questionText, String option1, String option2, String option3, String option4, int correctAnswerIndex) {
        Question question = Question.builder(topicId, questionText)
                .option1(option1)
                .option2(option2)
                .option3(option3)
                .option4(option4)
                .correctAnswerIndex(correctAnswerIndex)
                .build();
        return questionDAO.createQuestion(question);
    }
}
