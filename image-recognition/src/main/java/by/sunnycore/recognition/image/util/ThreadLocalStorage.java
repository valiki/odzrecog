package by.sunnycore.recognition.image.util;

import java.util.HashMap;
import java.util.Map;

/**
 * A storage that stores hashMap of the valuse in thread local and gives us
 * access to this variables
 * 
 * @author Valiantsin Shukaila
 * 
 */
public class ThreadLocalStorage {
    
    private static class ThreadLocalMap extends ThreadLocal<Map<String, Object>> {
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<String, Object>();
        }
    }

    private static ThreadLocalMap map = new ThreadLocalMap();

    /**
     * Stores object by key in thread local
     * 
     * @param key
     * @param value
     */
    public static void store(String key, Object value) {
        map.get().put(key, value);
    }

    /**
     * Gets object by key from thread local
     * 
     * @param key
     * @return
     */
    public static Object get(String key) {
        return map.get().get(key);
    }

}
