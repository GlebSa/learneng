package ru.glebsa.tts.cache;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileCache implements Cache {

    private static final String FORMAT = ".mp3";

    @Inject
    @Named("saveSoundPath")
    private String savePath;

    @Override
    public void store(byte[] bytes, String filename) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try {
            Files.copy(bis, new File(savePath + filename + FORMAT).toPath());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public byte[] retrieve(String filename) {
        File file = new File(savePath + filename + FORMAT);
        if (!file.exists()) return null;

        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
