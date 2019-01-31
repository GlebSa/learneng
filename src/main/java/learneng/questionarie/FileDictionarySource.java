package learneng.questionarie;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.util.*;
import java.util.stream.Collectors;

class FileDictionarySource implements DictionarySource {

    private String dictionaryFilePath;

    FileDictionarySource(String dictionaryFilePath) {
        this.dictionaryFilePath = dictionaryFilePath;
    }

    @Override
    public List<Question> getQuestions() {
        return new QuestionsGenerator().getQuestions();
    }

    private class QuestionsGenerator {
        private File dictionarySource;
        private List<Question> questions;
        private Set<String> wrongAnswers;

        private QuestionsGenerator() {
            dictionarySource = new File(dictionaryFilePath);
            questions = new ArrayList<>();
        }

        private List<Question> getQuestions() {
            collectWrongAnswers();
            fillQuestionList();
            return questions;
        }

        private BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(Files.newInputStream(dictionarySource.toPath()), StandardCharsets.UTF_8.name()));
        }

        private void collectWrongAnswers() {
            try (BufferedReader r = getReader()) {
                wrongAnswers = r.lines()
                        .map(this::getWordsOnly)
                        .collect(Collectors.toList())
                        .stream()
                        .flatMap(List::stream)
                        .collect(Collectors.toSet());
            } catch (InvalidPathException | IOException e) {
                throw new IllegalStateException(e);
            }
        }

        private List<String> getWordsOnly(String line) {
            String[] pair = line.split("=");
            if (pair.length < 2) return Collections.emptyList();
            return Arrays.stream(
                    pair[1].split(";"))
                    .map(String::trim)
                    .collect(Collectors.toList());
        }

        private void fillQuestionList() {
            try (BufferedReader r = getReader()) {
                String line = r.readLine();
                if (line == null) throw new IllegalStateException("Empty source!");
                if ((int) line.charAt(0) == 65279) {
                    line = line.substring(1);
                }

                do {
                    if (line.trim().length() == 0) continue;
                    addQuestion(line);
                } while ((line = r.readLine()) != null);
            } catch (InvalidPathException | IOException e) {
                throw new IllegalStateException(e);
            }
        }

        private void addQuestion(String line) {
            String[] pair = line.split("=");
            if (pair.length < 2) return;

            String word = pair[0].trim();

            List<String> rightAnswers = Arrays.stream(pair[1].split(";"))
                    .map(String::trim)
                    .collect(Collectors.toList());


            return;
        }
    }

}
