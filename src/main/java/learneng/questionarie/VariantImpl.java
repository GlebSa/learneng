package learneng.questionarie;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
class VariantImpl implements Variant {

    private boolean rightAnswer;
    private int index;
    private String word;

}
