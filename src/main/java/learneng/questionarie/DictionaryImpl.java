package learneng.questionarie;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@Getter
final class DictionaryImpl implements Dictionary {

    private Map<String, List<String>> values;
    private List<String> wrongVariants;

}
