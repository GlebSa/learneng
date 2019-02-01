package learneng.questionarie;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
final class QuestionImpl implements Question {

    private String value;

    private transient List<String> variants;

    private List<String> rightVariants;

}
