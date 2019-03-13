package ru.glebsa.learn.config;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class QuestionnaireParameters {

    private final String dictionaryFilePath;
    private final String savePath;

    /**
     * variantsLimit limit of variants for a question
     */
    private final int variantsLimit;

}
