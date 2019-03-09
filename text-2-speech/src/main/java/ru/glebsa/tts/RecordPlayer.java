package ru.glebsa.tts;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class RecordPlayer {
    private final Synthesizer synthesizer;

    public RecordPlayer(Synthesizer synthesizer) {
        this.synthesizer = synthesizer;
    }

    public void play(InputStream is) {
        try {
            Player player = new Player(is);
            player.play();
        } catch (JavaLayerException e) {
            throw new IllegalStateException(e);
        }
    }

    public void record(InputStream is) {
        try {
            Files.copy(is, new File("test.mp3").toPath());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
