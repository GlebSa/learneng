package ru.glebsa.tts;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.lang3.StringUtils;
import ru.glebsa.tts.config.TtsModule;
import ru.glebsa.tts.config.TtsParameters;
import ru.glebsa.tts.player.Player;

public class Application {

    public static void main(String[] args) {
        if (args.length == 0) return;

        TtsParameters parameters = TtsParameters.builder()
                .saveSoundPath("")
                .build();

        Injector injector = Guice.createInjector(new TtsModule(parameters));
        Player player = injector.getInstance(Player.class);
        player.play(StringUtils.join(args, " "));
    }

}
