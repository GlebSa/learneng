package learneng.questionarie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@AllArgsConstructor
@Getter
final class QuestionImpl implements Question {

    @NonNull
    private final String value;

    @NonNull
    private final List<String> variants;

    @NonNull
    private final List<String> rightVariants;

}
