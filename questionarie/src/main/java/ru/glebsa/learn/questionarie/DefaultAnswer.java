package ru.glebsa.learn.questionarie;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
final class DefaultAnswer implements Answer {

    private String answer;

    private DefaultQuestion question;

    @JsonIgnore
    @Override
    public boolean isRight() {
        return question.getRightVariants().contains(answer);
    }
}
