package learneng.questionarie;

import learneng.questionarie.dictionary.Dictionary;
import learneng.questionarie.dictionary.TextFileDictionarySource;

public class QuestionnaireFactory {

    private QuestionnaireFactory() {
    }

    public static Questionnaire create(String dictionaryFilePath, int variantsLimit) {
        Dictionary dictionary = new TextFileDictionarySource(dictionaryFilePath).getDictionary();
        return new CommonQuestionnaire(dictionary, variantsLimit);
    }

}
