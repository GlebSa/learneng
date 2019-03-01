package ru.glebsa.learn.persistence;

public interface Serializer {

    void serialize(Object object);

    <T> T deserialize(Class<T> clazz);

}
