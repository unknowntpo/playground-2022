package org.example;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class CacheInvocationHandler implements InvocationHandler {

    private final Object target;

    public CacheInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Cache.class)) {
            // get method and arg
            String methodKey = target.getClass().getSimpleName() + "." + method.getName();
        }
        return null;
    }
}
