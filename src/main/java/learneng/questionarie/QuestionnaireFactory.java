package learneng.questionarie;

public class QuestionnaireFactory {

    private QuestionnaireFactory() {
    }

    public static Questionnaire create(String dictionaryFilePath) {
        Dictionary dictionary = new FileDictionarySource(dictionaryFilePath).getDictionary();
        System.out.println(dictionary);
//        return new QuestionnaireImpl(dictionaryFilePath);
        return null;
    }

}
