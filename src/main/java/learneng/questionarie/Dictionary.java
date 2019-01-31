package learneng.questionarie;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Dictionary {

    Map<String, List<String>> getValues();
    Set<String> getWrongAnswers();

}
