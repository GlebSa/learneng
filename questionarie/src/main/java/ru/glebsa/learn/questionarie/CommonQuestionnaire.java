package ru.glebsa.learn.questionarie;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import ru.glebsa.learn.dictionary.Dictionary;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.glebsa.learn.dictionary.DictionarySource;
import ru.glebsa.learn.persistence.Serializer;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommonQuestionnaire implements Questionnaire {

    private Queue<DefaultQuestion> questions;
    private Deque<DefaultQuestion> buffer;
    private List<DefaultAnswer> rightAnswers;
    private List<DefaultAnswer> wrongAnswers;
    private List<String> wrongVariants;

    @Inject
    private DictionarySource dictionarySource;

    @Inject
    private Serializer serializer;

    @Inject
    @Named("variantsLimit")
    private int variantsLimit;

    public CommonQuestionnaire() {
    }

    @Inject
    public void init() {
        Dictionary dictionary = dictionarySource.getDictionary();
        Memento memento = serializer.deserialize(Memento.class);

        if (memento != null) {
            restoreQuestions(memento, dictionary);
        } else {
            setQuestions(dictionary);
        }
    }

    private void setQuestions(Dictionary dictionary) {
        if (variantsLimit < 2) throw new IllegalArgumentException("Variants limit is less than 2!");
        this.wrongVariants = dictionary.getWrongVariants();
        this.buffer = new LinkedList<>();
        this.rightAnswers = new ArrayList<>();
        this.wrongAnswers = new ArrayList<>();

        Random random = new Random(47);
        LinkedList<DefaultQuestion> list = dictionary.getValues().entrySet().stream()
                .map(entry -> createQuestion(entry.getKey(), entry.getValue(), dictionary.getWrongVariants(), random))
                .collect(Collectors.toCollection(LinkedList::new));
        Collections.shuffle(list);
        this.questions = list;
    }

    private void restoreQuestions(Memento memento, Dictionary dictionary) {
        if (variantsLimit < 2) throw new IllegalArgumentException("Variants limit is less than 2!");
        this.wrongVariants = dictionary != null ? dictionary.getWrongVariants() : memento.getWrongVariants();
        this.rightAnswers = memento.getRightAnswers();
        this.buffer = new LinkedList<>();
        this.wrongAnswers = new ArrayList<>();

        Random random = new Random(47);
        Stream<DefaultQuestion> questionStream = memento.getQuestions().stream()
                .map(question -> createQuestion(question.getValue(), question.getRightVariants(), wrongVariants, random));
        Stream<DefaultQuestion> skippedStream = memento.getSkipped().stream()
                .map(question -> createQuestion(question.getValue(), question.getRightVariants(), wrongVariants, random));
        Stream<DefaultQuestion> wrongAnswersStream = memento.getWrongAnswers().stream()
                .map(answer -> createQuestion(answer.getQuestion().getValue(), answer.getQuestion().getRightVariants(), wrongVariants, random));
        Stream<DefaultQuestion> newQuestions = getNewQuestions(memento, dictionary, random);

        this.questions = Stream.of(newQuestions, wrongAnswersStream, skippedStream, questionStream)
                .flatMap(s -> s)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private Stream<DefaultQuestion> getNewQuestions(Memento memento, Dictionary dictionary, Random random) {
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
                .map(entry -> createQuestion(entry.getKey(), entry.getValue(), wrongVariants, random));
    }

    private DefaultQuestion createQuestion(String questionValue, List<String> rightVariants, List<String> wrongVariants, Random random) {
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
        DefaultQuestion question = questions.poll();
        buffer.push(question);
        return question;
    }

    @Override
    public boolean answer(String userAnswer) {
        DefaultAnswer answer = new DefaultAnswer()
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
    public void save() {
        Memento memento = new Memento()
                .setQuestions(new ArrayList<>(this.questions))
                .setSkipped(new ArrayList<>(this.buffer))
                .setRightAnswers(new ArrayList<>(this.rightAnswers))
                .setWrongAnswers(new ArrayList<>(this.wrongAnswers))
                .setWrongVariants(new ArrayList<>(this.wrongVariants));
        serializer.serialize(memento);
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
    private static final class Memento implements Serializable {
        private List<DefaultQuestion> questions;
        private List<DefaultQuestion> skipped;
        private List<DefaultAnswer> rightAnswers;
        private List<DefaultAnswer> wrongAnswers;
        private List<String> wrongVariants;
    }

}
