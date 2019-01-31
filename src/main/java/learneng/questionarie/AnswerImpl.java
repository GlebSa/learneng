package learneng.questionarie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
final class AnswerImpl implements Answer {

    @NonNull
    private final String answer;

    @NonNull
    private final Question question;

    @Override
    public boolean isRight() {
        return question.getRightVariants().contains(answer);
    }
}
