package ru.glebsa.tts;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import ru.glebsa.tts.dto.WatsonProperties;

import java.io.InputStream;

public class TestYaml {

    public static void main(String[] args) {
        new TestYaml().start();
    }

    private void start() {
        Yaml yaml = new Yaml(new Constructor(WatsonProperties.class));
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("watson.yml");
        WatsonProperties watsonProperties = yaml.load(inputStream);
        System.out.println(watsonProperties);
    }

}
