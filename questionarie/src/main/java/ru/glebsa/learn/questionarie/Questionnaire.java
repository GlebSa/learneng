package ru.glebsa.learn.questionarie;

public interface Questionnaire {

    boolean hasQuestions();

    Question getQuestion();

    boolean answer(String userAnswer);

    void save();

    int rightAnswered();

    int wrongAnswered();

    int notAnswered();

    int leftToAnswer();

}
