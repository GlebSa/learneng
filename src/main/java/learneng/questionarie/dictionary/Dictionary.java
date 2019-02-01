package learneng.questionarie.dictionary;

import java.util.List;
import java.util.Map;

public interface Dictionary {

    Map<String, List<String>> getValues();

    List<String> getWrongVariants();

}
