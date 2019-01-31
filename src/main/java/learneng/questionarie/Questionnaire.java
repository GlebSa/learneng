package learneng.questionarie;

public interface Questionnaire {

    Question getQuestion();

    void answer(int userAnswer);

}
