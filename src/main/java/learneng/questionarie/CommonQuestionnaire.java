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
    private List<String> wrongVariants;

    /**
     * @param dictionary    {@link Dictionary}
     * @param variantsLimit limit of variants for a question
     */
    public CommonQuestionnaire(Dictionary dictionary, int variantsLimit) {
        setQuestions(dictionary, variantsLimit);
    }

    /**
     * @param dictionary    {@link Dictionary}
     * @param memento       {@link Memento}
     * @param variantsLimit limit of variants for a question
     */
    public CommonQuestionnaire(Dictionary dictionary, Memento memento, int variantsLimit) {
        if (memento != null) {
            restoreQuestions(memento, dictionary, variantsLimit);
        } else {
            setQuestions(dictionary, variantsLimit);
        }
    }

    private void setQuestions(Dictionary dictionary, int variantsLimit) {
        if (variantsLimit < 2) throw new IllegalArgumentException("Variants limit is less than 2!");
        this.wrongVariants = dictionary.getWrongVariants();
        this.buffer = new LinkedList<>();
        this.rightAnswers = new ArrayList<>();
        this.wrongAnswers = new ArrayList<>();

        Random random = new Random(47);
        LinkedList<Question> list = dictionary.getValues().entrySet().stream()
                .map(entry -> createQuestion(entry.getKey(), entry.getValue(), dictionary.getWrongVariants(), random, variantsLimit))
                .collect(Collectors.toCollection(LinkedList::new));
        Collections.shuffle(list);
        this.questions = list;
    }

    private void restoreQuestions(Memento memento, Dictionary dictionary, int variantsLimit) {
        if (variantsLimit < 2) throw new IllegalArgumentException("Variants limit is less than 2!");
        this.wrongVariants = dictionary != null ? dictionary.getWrongVariants() : memento.getWrongVariants();
        this.rightAnswers = memento.getRightAnswers();
        this.buffer = new LinkedList<>();
        this.wrongAnswers = new ArrayList<>();

        Random random = new Random(47);
        Stream<Question> questionStream = memento.getQuestions().stream()
                .map(question -> createQuestion(question.getValue(), question.getRightVariants(), wrongVariants, random, variantsLimit));
        Stream<Question> skippedStream = memento.getSkipped().stream()
                .map(question -> createQuestion(question.getValue(), question.getRightVariants(), wrongVariants, random, variantsLimit));
        Stream<Question> wrongAnswersStream = memento.getWrongAnswers().stream()
                .map(answer -> createQuestion(answer.getQuestion().getValue(), answer.getQuestion().getRightVariants(), wrongVariants, random, variantsLimit));
        Stream<Question> newQuestions = getNewQuestions(memento, dictionary, random, variantsLimit);

        this.questions = Stream.of(newQuestions, wrongAnswersStream, skippedStream, questionStream)
                .flatMap(s -> s)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private Stream<Question> getNewQuestions(Memento memento, Dictionary dictionary, Random random, int variantsLimit) {
        if (dictionary == null) return Stream.empty();

        Stream<String> questionStream = memento.getQuestions().stream()
                .map(Question::getValue);
        Stream<String> skippedStream = memento.getSkipped().stream()
                .map(Question::getValue);
        Stream<String> wrongAnswersStream = memento.getWrongAnswers().stream()
                .map(answer -> answer.getQuestion().getValue());
        Stream<String> rightAnswersStream = memento.getRightAnswers().stream()
                .map(answer -> answer.getQuestion().getValue());

        Set<String> values = Stream.of(questionStream, skippedStream, wrongAnswersStream, rightAnswersStream)
                .flatMap(s -> s)
                .collect(Collectors.toSet());

        return dictionary.getValues().entrySet().stream()
                .filter(entry -> !values.contains(entry.getKey()))
                .map(entry -> createQuestion(entry.getKey(), entry.getValue(), wrongVariants, random, variantsLimit));
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

        return new DefaultQuestion()
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
        Answer answer = new DefaultAnswer()
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

    @Override
    public Memento save() {
        return new Memento()
                .setQuestions(new ArrayList<>(this.questions))
                .setSkipped(new ArrayList<>(this.buffer))
                .setRightAnswers(new ArrayList<>(this.rightAnswers))
                .setWrongAnswers(new ArrayList<>(this.wrongAnswers))
                .setWrongVariants(new ArrayList<>(this.wrongVariants));
    }

    @Override
    public int rightAnswered() {
        return rightAnswers.size();
    }

    @Override
    public int wrongAnswered() {
        return wrongAnswers.size();
    }

    @Override
    public int notAnswered() {
        return buffer.size();
    }

    @Override
    public int leftToAnswer() {
        return questions.size();
    }

    @Data
    @Accessors(chain = true)
    public static final class Memento implements Serializable {
        private List<Question> questions;
        private List<Question> skipped;
        private List<Answer> rightAnswers;
        private List<Answer> wrongAnswers;
        private List<String> wrongVariants;
    }

}
