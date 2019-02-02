package learneng.persistence;

public interface Serializer {

    void serialize(Object object);

    <T> T deserialize(Class<T> clazz);

}
