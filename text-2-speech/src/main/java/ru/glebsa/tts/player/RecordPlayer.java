package ru.glebsa.tts.player;

import com.google.inject.Inject;
import javazoom.jl.decoder.JavaLayerException;
import ru.glebsa.tts.cache.Cache;
import ru.glebsa.tts.synthesizer.Synthesizer;

import java.io.ByteArrayInputStream;

public class RecordPlayer implements Player {

    @Inject
    private Synthesizer synthesizer;

    @Inject
    private Cache cache;

    public void play(String text) {
        byte[] bytes;
        bytes = cache.retrieve(text);
        if (bytes == null) {
            bytes = synthesizer.synthesize(text);
            cache.store(bytes, text);
        }
        play(bytes);
    }

    private void play(byte[] bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try {
            javazoom.jl.player.Player player = new javazoom.jl.player.Player(bis);
            player.play();
        } catch (JavaLayerException e) {
            throw new IllegalStateException(e);
        }
    }


}
