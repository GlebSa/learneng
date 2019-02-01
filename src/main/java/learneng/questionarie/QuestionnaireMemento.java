package learneng.questionarie;

import java.util.List;

public interface QuestionnaireMemento {

    List<Question> getQuestions();

    List<Question> getSkipped();

    List<Answer> getRightAnswers();

    List<Answer> getWrongAnswers();

}
