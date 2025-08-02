package ex.sample.global.inmemory;

import java.time.Duration;
import java.util.Optional;

public interface InMemoryStore {
    
    <T> void put(String key, T value);

    <T> void put(String key, T value, Duration ttl);

    <T> Optional<T> get(String key, Class<T> clazz);

    boolean containsKey(String key);

    void remove(String key);

    void clear();
}
