package learneng.questionarie;

public interface Questionnaire {

    boolean hasQuestions();

    Question getQuestion();

    boolean answer(String userAnswer);

    Object save();

}
