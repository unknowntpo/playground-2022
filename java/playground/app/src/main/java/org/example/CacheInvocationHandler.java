package org.example;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheInvocationHandler implements InvocationHandler {

    private static final Logger log = LoggerFactory.getLogger(CacheInvocationHandler.class);
    private final Object target;

    public CacheInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // FIXME: what does 'target' do ?
        Method targetMethod = null;
        try {
            targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            // Method not found in target, execute normally
            log.debug("Method not found in target: " + method.getName());
            return method.invoke(target, args);
        }

        log.debug("Checking method: " + method.getName() + ", has @Cache: " + targetMethod.isAnnotationPresent(Cache.class));
        if (targetMethod.isAnnotationPresent(Cache.class)) {
            // get method and arg
            String methodKey = target.getClass().getSimpleName() + "." + method.getName();
            String paramKey = args != null ? Arrays.toString(args) : "()";

            if (CacheStorage.contains(methodKey, paramKey)) {
                log.debug("Cache HIT for " + methodKey + "(" + paramKey + ")");
                return CacheStorage.get(methodKey, paramKey);
            }

            // Execute method and cache result
            log.debug("Cache MISS for " + methodKey + "(" + paramKey + ") - executing method");
            Object result = method.invoke(target, args);
            CacheStorage.put(methodKey, paramKey, result);
            return result;
        }

        log.debug("No @Cache annotation, executing method normally: " + method.getName());
        return method.invoke(target, args);
    }
}
