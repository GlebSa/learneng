package learneng.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class JacksonSerializer implements Serializer {
    private ObjectMapper mapper;
    private String saveDirectory;
    private String filename;

    public JacksonSerializer(String saveDirectory, String filename) {
        this.mapper = new ObjectMapper();
        this.saveDirectory = saveDirectory;
        this.filename = filename;
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
                                new File(saveDirectory + filename + ".json").toPath())));
    }

    @Override
    public <T> T deserialize(Class<T> clazz) {
        File file = new File(saveDirectory + filename + ".json");
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
