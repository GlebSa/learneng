package ru.glebsa.learn.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class JacksonSerializer implements Serializer {
    private final ObjectMapper mapper;

    @Inject
    @Named("savePath")
    private String savePath;

    @Inject
    @Named("saveFilename")
    private String filename;

    public JacksonSerializer() {
        this.mapper = new ObjectMapper();
    }

    @Override
    public void serialize(Object object) {
        try (BufferedWriter w = getWriter()) {
            this.mapper.writeValue(w, object);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private BufferedWriter getWriter() throws IOException {
        return new BufferedWriter(
                new OutputStreamWriter(
                        Files.newOutputStream(
                                new File(savePath + filename + ".json").toPath())));
    }

    @Override
    public <T> T deserialize(Class<T> clazz) {
        File file = new File(savePath + filename + ".json");
        if (!file.exists()) return null;

        try (BufferedReader r = getReader(file)) {
            return this.mapper.readValue(r, clazz);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private BufferedReader getReader(File file) throws IOException {
        return new BufferedReader(
                new InputStreamReader(
                        Files.newInputStream(
                                file.toPath()), StandardCharsets.UTF_8.name()));
    }

}
