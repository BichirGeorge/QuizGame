package quizgame.model;

public class NoQuestionsInTopicException extends Exception {
    public NoQuestionsInTopicException(String topicName) {
        super("Topicul \"" + topicName + "\" nu contine nicio intrebare.");
    }
}