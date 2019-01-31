package learneng.questionarie;

import java.util.*;
import java.util.stream.Collectors;

public class CommonQuestionnaire implements Questionnaire {

    private Queue<Question> questions;
    private Deque<Question> buffer;
    private List<Answer> rightAnswers;
    private List<Answer> wrongAnswers;

    public CommonQuestionnaire(Dictionary dictionary) {
        setQuestions(dictionary);
        buffer = new LinkedList<>();
        rightAnswers = new ArrayList<>();
        wrongAnswers = new ArrayList<>();
    }

    private void setQuestions(Dictionary dictionary) {
        Random random = new Random(47);
        LinkedList<Question> list = dictionary.getValues().entrySet().stream()
                .map(entry -> createQuestion(entry.getKey(), entry.getValue(), dictionary.getWrongVariants(), random))
                .collect(Collectors.toCollection(LinkedList::new));
        Collections.shuffle(list);
        questions = list;
    }

    private Question createQuestion(String questionWord, List<String> rightVariants, List<String> wrongVariants, Random random) {
        List<String> variants = new ArrayList<>();
        variants.add(rightVariants.get(random.nextInt(rightVariants.size())));

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 5; j++) {
                String wrongVariant = wrongVariants.get(random.nextInt(wrongVariants.size()));

                if (!rightVariants.contains(wrongVariant) && !variants.contains(wrongVariant)) {
                    variants.add(wrongVariant);
                    break;
                }
            }
        }

        Collections.shuffle(variants);

        return new QuestionImpl(questionWord, variants, rightVariants);
    }

    @Override
    public boolean hasQuestions() {
        return !questions.isEmpty();
    }

    @Override
    public Question getQuestion() {
        Question question = questions.poll();
        buffer.push(question);
        return question;
    }

    @Override
    public boolean answer(String userAnswer) {
        Answer answer = new AnswerImpl(userAnswer, buffer.poll());
        if (answer.isRight()) {
            rightAnswers.add(answer);
            return true;
        } else {
            wrongAnswers.add(answer);
            return false;
        }
    }
}
