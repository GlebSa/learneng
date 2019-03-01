package ru.glebsa.learn.questionarie;

import ru.glebsa.learn.persistence.JacksonSerializer;
import ru.glebsa.learn.persistence.Serializer;
import ru.glebsa.learn.questionarie.dictionary.Dictionary;
import ru.glebsa.learn.questionarie.dictionary.TextFileDictionarySource;

import static ru.glebsa.learn.questionarie.CommonQuestionnaire.*;

public class QuestionnaireService {

    private QuestionnaireService() {
    }

    public static Questionnaire createCommon(String dictionaryFilePath, String savePath, int variantsLimit) {
        Serializer serializer = new JacksonSerializer(savePath, getSaveFilename(CommonQuestionnaire.class));
        Memento memento = serializer.deserialize(Memento.class);
        Dictionary dictionary = new TextFileDictionarySource(dictionaryFilePath).getDictionary();
        return new CommonQuestionnaire(dictionary, memento, variantsLimit);
    }

    public static void save(Questionnaire questionnaire, String savePath) {
        Serializer serializer = new JacksonSerializer(savePath, getSaveFilename(questionnaire.getClass()));
        serializer.serialize(questionnaire.save());
    }

    private static String getSaveFilename(Class clazz) {
        return clazz.getSimpleName().toLowerCase() + "-save";
    }

}
