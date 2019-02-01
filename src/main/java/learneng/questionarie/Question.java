package learneng.questionarie;

import java.util.List;

public interface Question {

    String getValue();

    List<String> getVariants();

    List<String> getRightVariants();

}
