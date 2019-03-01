package ru.glebsa.learn.questionarie;

public interface Answer {

    String getAnswer();

    Question getQuestion();

    boolean isRight();

}
