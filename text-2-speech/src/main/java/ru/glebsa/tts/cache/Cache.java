package ru.glebsa.tts.cache;

public interface Cache {

    void store(byte[] bytes, String filename);

    byte[] retrieve(String filename);

}
