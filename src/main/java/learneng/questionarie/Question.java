package learneng.questionarie;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, defaultImpl = DefaultQuestion.class)
public interface Question {

    String getValue();

    List<String> getVariants();

    List<String> getRightVariants();

}
