package ru.glebsa.tts;

import com.google.common.io.ByteStreams;
import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voices;
import com.ibm.watson.developer_cloud.text_to_speech.v1.websocket.BaseSynthesizeCallback;

import java.io.*;
import java.nio.file.Files;

import javazoom.jl.player.Player;
import sun.misc.IOUtils;

public class Test {

    public static void main(String[] args) throws Exception {
//        TextToSpeech service = new TextToSpeech();
//        service.setApiKey("");
//        service.setEndPoint("https://gateway-wdc.watsonplatform.net/text-to-speech/api");
        TextToSpeech service = new TextToSpeech(new IamOptions.Builder()
                .apiKey("")
                .build());
        service.setEndPoint("https://gateway-wdc.watsonplatform.net/text-to-speech/api");

        //Voices voices = service.listVoices().execute();
        //System.out.println(voices);
        String text = "Test learn";
        SynthesizeOptions synthesizeOptions = new SynthesizeOptions.Builder()
                .text(text)
                .accept(SynthesizeOptions.Accept.AUDIO_MP3)
                .build();

        ServiceCall<InputStream> serviceCall = service.synthesize(synthesizeOptions);

        InputStream is = serviceCall.execute();
        byte[] buffer = ByteStreams.toByteArray(is);


        ByteArrayInputStream bis = new ByteArrayInputStream(buffer);

        Files.copy(bis, new File("test.mp3").toPath());
        bis.reset();
        Player player = new Player(bis);
        player.play();

        // a callback is defined to handle certain events, like an audio transmission or a timing marker
        // in this case, we'll build up a byte array of all the received bytes to build the resulting file
        /*final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        service.synthesizeUsingWebSocket(synthesizeOptions, new BaseSynthesizeCallback() {
            @Override
            public void onAudioStream(byte[] bytes) {
                // append to our byte array
                try {
                    byteArrayOutputStream.write(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // quick way to wait for synthesis to complete, since synthesizeUsingWebSocket() runs asynchronously
        Thread.sleep(5000);

        // create file with audio data
        String filename = "synthesize_websocket_test.ogg";
        OutputStream fileOutputStream = new FileOutputStream(filename);
        byteArrayOutputStream.writeTo(fileOutputStream);

        // clean up
        byteArrayOutputStream.close();
        fileOutputStream.close();*/

    }

}
