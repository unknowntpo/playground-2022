package org.example;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheStorage {
    private static final Logger log = LoggerFactory.getLogger(CacheStorage.class);
    private static final Map<String, Map<String, Object>> caches = new ConcurrentHashMap();

    @SuppressWarnings("unchecked")
    public static <T> T get(String methodKey, String paramKey) {
        var methodCache = caches.get(methodKey);
        T result = methodCache != null ? (T) methodCache.get(paramKey) : null;
        log.debug("Cache GET: " + methodKey + "(" + paramKey + ") = " + (result != null ? "found" : "not found"));
        return result;
    }

    public static void put(String methodKey, String paramKey, Object value) {
        caches.computeIfAbsent(methodKey, key -> new ConcurrentHashMap<>()).put(paramKey, value);
        log.debug("Cache PUT: " + methodKey + "(" + paramKey + ") = " + value);
    }

    public static boolean contains(String methodKey, String paramKey) {
        Map<String, Object> methodCache = caches.get(methodKey);
        boolean found = methodCache != null && methodCache.containsKey(paramKey);
        log.debug("Cache CONTAINS: " + methodKey + "(" + paramKey + ") = " + found);
        return found;
    }

    public static void clear() {
        caches.clear();
    }

    public static void printStats() {
        System.out.println("=== Cache Stats ===");
        caches.forEach((method, cache) ->
                System.out.println(method + ": " + cache.size() + " entries"));
    }
}
