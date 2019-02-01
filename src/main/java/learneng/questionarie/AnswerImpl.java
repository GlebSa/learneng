package learneng.questionarie;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
final class AnswerImpl implements Answer {

    private String answer;

    private Question question;

    @Override
    public boolean isRight() {
        return question.getRightVariants().contains(answer);
    }
}
