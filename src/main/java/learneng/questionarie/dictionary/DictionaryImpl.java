package learneng.questionarie.dictionary;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
final class DictionaryImpl implements Dictionary {

    private Map<String, List<String>> values;
    private List<String> wrongVariants;

}
