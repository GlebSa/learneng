package ru.glebsa.learn.questionarie.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.util.*;
import java.util.stream.Collectors;

public class TextFileDictionarySource implements DictionarySource {

    private String dictionaryFilePath;

    public TextFileDictionarySource(String dictionaryFilePath) {
        this.dictionaryFilePath = Objects.requireNonNull(dictionaryFilePath);
    }

    @Override
    public Dictionary getDictionary() {
        return new DictionaryImpl(
                getValuesList(),
                getWrongVariants()
        );
    }

    private List<String> getWrongVariants() {
        try (BufferedReader r = getReader()) {
            return r.lines()
                    .map(this::getWordsOnly)
                    .collect(Collectors.toList())
                    .stream()
                    .flatMap(List::stream)
                    .distinct()
                    .collect(Collectors.toList());
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
