package ru.glebsa.tts.synthesizer;

import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.SynthesizeOptions;
import org.apache.commons.io.IOUtils;
import ru.glebsa.tts.synthesizer.dto.WatsonProperties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class WatsonSynthesizer implements Synthesizer{
    private static final String FORMAT = SynthesizeOptions.Accept.AUDIO_MP3;

    private final TextToSpeech service;

    public WatsonSynthesizer(WatsonProperties properties) {
        Objects.requireNonNull(properties.getApikey());
        Objects.requireNonNull(properties.getEndpoint());

        this.service = new TextToSpeech(new IamOptions.Builder()
                .apiKey(properties.getApikey())
                .build());
        service.setEndPoint(properties.getEndpoint());
    }

    @Override
    public byte[] synthesize(String text) {
        SynthesizeOptions synthesizeOptions = new SynthesizeOptions.Builder()
                .text(text)
                .accept(FORMAT)
                .build();

        ServiceCall<InputStream> serviceCall = service.synthesize(synthesizeOptions);

        InputStream is = serviceCall.execute();
        try {
            return IOUtils.toByteArray(is);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
