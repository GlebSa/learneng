package ru.glebsa.learn.config;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import ru.glebsa.learn.dictionary.DictionarySource;
import ru.glebsa.learn.dictionary.TextFileDictionarySource;
import ru.glebsa.learn.persistence.JacksonSerializer;
import ru.glebsa.learn.persistence.Serializer;
import ru.glebsa.learn.questionarie.CommonQuestionnaire;
import ru.glebsa.learn.questionarie.Questionnaire;
import ru.glebsa.learn.ui.Console;
import ru.glebsa.tts.config.TtsModule;
import ru.glebsa.tts.config.TtsParameters;

import java.util.Objects;

public class QuestionnaireModule extends AbstractModule {
    private final QuestionnaireParameters parameters;

    public QuestionnaireModule(QuestionnaireParameters parameters) {
        this.parameters = Objects.requireNonNull(parameters);
    }

    @Override
    protected void configure() {
        Questionnaire questionnaire = new CommonQuestionnaire();

        bind(Serializer.class).toInstance(new JacksonSerializer());
        bind(DictionarySource.class).toInstance(new TextFileDictionarySource());
        bind(Questionnaire.class).toInstance(questionnaire);
        bind(Console.class).toInstance(new Console());

        bind(Integer.class).annotatedWith(Names.named("variantsLimit")).toInstance(parameters.getVariantsLimit());
        bind(String.class).annotatedWith(Names.named("savePath")).toInstance(parameters.getSavePath());
        bind(String.class).annotatedWith(Names.named("saveFilename")).toInstance(getSaveFilename(questionnaire.getClass()));
        bind(String.class).annotatedWith(Names.named("dictionaryFilePath")).toInstance(parameters.getDictionaryFilePath());

        TtsParameters ttsParameters = TtsParameters.builder()
                .saveSoundPath("sounds/" + parameters.getSavePath())
                .build();
        install(new TtsModule(ttsParameters));
    }

    private String getSaveFilename(Class clazz) {
        return clazz.getSimpleName().toLowerCase() + "-save";
    }

}
