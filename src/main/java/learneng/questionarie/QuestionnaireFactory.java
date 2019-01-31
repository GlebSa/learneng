package learneng.questionarie;

public class QuestionnaireFactory {

    private QuestionnaireFactory() {
    }

    public static Questionnaire create(String dictionaryFilePath) {
        new FileDictionarySource(dictionaryFilePath).getQuestions();
//        return new QuestionnaireImpl(dictionaryFilePath);
        return null;
    }

}
