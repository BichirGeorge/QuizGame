package quizgame.builder;

import quizgame.model.Question;

public class QuestionBuilder {
    private int id = 0;
    private int topicId;
    private String questionText;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int correctAnswerIndex;

    public QuestionBuilder(int topicId, String questionText) {
        this.topicId = topicId;
        this.questionText = questionText;
    }

    public QuestionBuilder id(int id) {
        this.id = id;
        return this;
    }

    public QuestionBuilder option1(String option1) {
        this.option1 = option1;
        return this;
    }

    public QuestionBuilder option2(String option2) {
        this.option2 = option2;
        return this;
    }

    public QuestionBuilder option3(String option3) {
        this.option3 = option3;
        return this;
    }

    public QuestionBuilder option4(String option4) {
        this.option4 = option4;
        return this;
    }

    public QuestionBuilder correctAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
        return this;
    }

    public Question build() {
        return new Question(id, topicId, questionText, option1, option2, option3, option4, correctAnswerIndex);
    }
}