package learneng.questionarie;

import java.util.List;

public interface Question {

    String getWord();

    List<String> getVariants();

    List<String> getRightVariants();

}
