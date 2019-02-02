package learneng.questionarie;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, defaultImpl = DefaultAnswer.class)
public interface Answer {

    String getAnswer();

    Question getQuestion();

    boolean isRight();

}
