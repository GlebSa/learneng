package ru.glebsa.tts.config;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TtsParameters {

    private final String saveSoundPath;

}
