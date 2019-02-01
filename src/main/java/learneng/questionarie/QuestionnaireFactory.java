package learneng.questionarie;

public class QuestionnaireFactory {

    private QuestionnaireFactory() {
    }

    public static Questionnaire create(String dictionaryFilePath, int variantsLimit) {
        Dictionary dictionary = new TextFileDictionarySource(dictionaryFilePath).getDictionary();
        return new CommonQuestionnaire(dictionary, variantsLimit);
    }

}
