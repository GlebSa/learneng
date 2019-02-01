package learneng.questionarie;

import learneng.questionarie.dictionary.Dictionary;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommonQuestionnaire implements Questionnaire {

    private Queue<Question> questions;
    private Deque<Question> buffer;
    private List<Answer> rightAnswers;
    private List<Answer> wrongAnswers;

    /**
     * @param dictionary    {@link Dictionary}
     * @param variantsLimit limit of variants for a question
     */
    public CommonQuestionnaire(Dictionary dictionary, int variantsLimit) {
        setQuestions(dictionary, variantsLimit);
        this.buffer = new LinkedList<>();
        this.rightAnswers = new ArrayList<>();
        this.wrongAnswers = new ArrayList<>();
    }

    /**
     * @param memento       {@link QuestionnaireMemento}
     * @param variantsLimit limit of variants for a question
     */
    public CommonQuestionnaire(QuestionnaireMemento memento, Dictionary dictionary, int variantsLimit) {
        restoreQuestions(memento, dictionary, variantsLimit);
        this.rightAnswers = memento.getRightAnswers();
    }

    private void setQuestions(Dictionary dictionary, int variantsLimit) {
        if (variantsLimit < 2) throw new IllegalArgumentException("Variants limit is less than 2!");
        Random random = new Random(47);
        LinkedList<Question> list = dictionary.getValues().entrySet().stream()
                .map(entry -> createQuestion(entry.getKey(), entry.getValue(), dictionary.getWrongVariants(), random, variantsLimit))
                .collect(Collectors.toCollection(LinkedList::new));
        Collections.shuffle(list);
        this.questions = list;
    }

    private void restoreQuestions(QuestionnaireMemento memento, Dictionary dictionary, int variantsLimit) {
        Random random = new Random(47);
        Stream<Question> questionStream = memento.getQuestions().stream()
                .map(question -> createQuestion(question.getValue(), question.getRightVariants(), dictionary.getWrongVariants(), random, variantsLimit));
        Stream<Question> skippedStream = memento.getSkipped().stream()
                .map(question -> createQuestion(question.getValue(), question.getRightVariants(), dictionary.getWrongVariants(), random, variantsLimit));
        Stream<Question> wrongAnswersStream = memento.getWrongAnswers().stream()
                .map(answer -> createQuestion(answer.getQuestion().getValue(), answer.getQuestion().getRightVariants(), dictionary.getWrongVariants(), random, variantsLimit));

        this.questions = Stream.of(wrongAnswersStream, skippedStream, questionStream)
                .flatMap(s -> s)
                .collect(Collectors.toCollection(LinkedList::new));

    }

    private Question createQuestion(String questionValue, List<String> rightVariants, List<String> wrongVariants, Random random, int variantsLimit) {
        List<String> variants = new ArrayList<>();
        variants.add(rightVariants.get(random.nextInt(rightVariants.size())));

        for (int i = 0; i < variantsLimit - 1; i++) {
            for (int j = 0; j < 5; j++) {
                String wrongVariant = wrongVariants.get(random.nextInt(wrongVariants.size()));

                if (!rightVariants.contains(wrongVariant) && !variants.contains(wrongVariant)) {
                    variants.add(wrongVariant);
                    break;
                }
            }
        }

        Collections.shuffle(variants);

        return new QuestionImpl()
                .setValue(questionValue)
                .setVariants(variants)
                .setRightVariants(rightVariants);
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
        Answer answer = new AnswerImpl()
                .setAnswer(userAnswer)
                .setQuestion(buffer.poll());
        if (answer.isRight()) {
            rightAnswers.add(answer);
            return true;
        } else {
            wrongAnswers.add(answer);
            return false;
        }
    }

    public QuestionnaireMemento save() {
        return new QuestionnaireMementoImpl()
                .setQuestions(new ArrayList<>(this.questions))
                .setSkipped(new ArrayList<>(this.buffer))
                .setRightAnswers(new ArrayList<>(this.rightAnswers))
                .setWrongAnswers(new ArrayList<>(this.wrongAnswers));
    }

    @Data
    @Accessors(chain = true)
    private final class QuestionnaireMementoImpl implements QuestionnaireMemento, Serializable {
        private List<Question> questions;
        private List<Question> skipped;
        private List<Answer> rightAnswers;
        private List<Answer> wrongAnswers;
    }

}
