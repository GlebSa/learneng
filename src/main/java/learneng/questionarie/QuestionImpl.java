package learneng.questionarie;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
class QuestionImpl implements Question {

    private String word;
    private List<Variant> variants;

}
