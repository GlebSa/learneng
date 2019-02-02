package learneng.questionarie;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
final class DefaultQuestion implements Question {

    private String value;

    @JsonIgnore
    private List<String> variants;

    private List<String> rightVariants;

}
