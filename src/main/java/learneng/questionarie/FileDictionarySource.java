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
    public Dictionary getDictionary() {
        return new FileDictionary(
                getValuesList(),
                getWrongAnswers()
        );
    }

    private Set<String> getWrongAnswers() {
        try (BufferedReader r = getReader()) {
            return r.lines()
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

    private Map<String, List<String>> getValuesList() {
        try (BufferedReader r = getReader()) {
            String line = r.readLine();
            if (line == null) throw new IllegalStateException("Empty source!");
            if ((int) line.charAt(0) == 65279) {
                line = line.substring(1);
            }

            Map<String, List<String>> values = new HashMap<>();

            do {
                if (line.trim().length() == 0) continue;

                String[] pair = line.split("=");
                if (pair.length < 2) continue;

                values.put(
                        pair[0].trim(),
                        Arrays.stream(pair[1].split(";"))
                                .map(String::trim)
                                .collect(Collectors.toList()));


            } while ((line = r.readLine()) != null);

            return values;
        } catch (InvalidPathException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private BufferedReader getReader() throws IOException {
        return new BufferedReader(
                new InputStreamReader(
                        Files.newInputStream(
                                new File(dictionaryFilePath).toPath()), StandardCharsets.UTF_8.name()));
    }
}
