package ru.glebsa.tts.config;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import ru.glebsa.tts.player.Player;
import ru.glebsa.tts.player.RecordPlayer;
import ru.glebsa.tts.cache.Cache;
import ru.glebsa.tts.cache.FileCache;
import ru.glebsa.tts.synthesizer.Synthesizer;
import ru.glebsa.tts.synthesizer.WatsonSynthesizer;
import ru.glebsa.tts.synthesizer.dto.WatsonProperties;

import java.io.InputStream;

public class TtsModule extends AbstractModule {
    private final TtsParameters parameters;

    public TtsModule(TtsParameters parameters){
        this.parameters = parameters;
    }

    @Override
    protected void configure() {
        WatsonProperties watsonProperies = getWatsonProperies();

        bind(Synthesizer.class).toInstance(new WatsonSynthesizer(watsonProperies));
        bind(Cache.class).toInstance(new FileCache());
        bind(Player.class).toInstance(new RecordPlayer());

        bind(String.class).annotatedWith(Names.named("saveSoundPath")).toInstance(parameters.getSaveSoundPath());
    }

    private WatsonProperties getWatsonProperies() {
        Yaml yaml = new Yaml(new Constructor(WatsonProperties.class));
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("watson.yml");
        return yaml.load(inputStream);
    }

}
