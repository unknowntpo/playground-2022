package org.example;

import java.lang.reflect.Proxy;

public class CacheFactory {
    @SuppressWarnings("unchecked")
    public static <T> T createCachedProxy(T instance, Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new CacheInvocationHandler(instance));
    }

}
