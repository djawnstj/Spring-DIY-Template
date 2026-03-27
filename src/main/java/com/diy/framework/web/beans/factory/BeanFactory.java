package com.diy.framework.web.beans.factory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanFactory {
    private final Map<Class<?>, Object> beans = new HashMap<>();

    public BeanFactory(Set<Class<?>> beanClasses) {
        initialize(beanClasses);
    }

    private void initialize(Set<Class<?>> beanClasses) {
        for (Class<?> clazz : beanClasses) {
            createBean(clazz);
        }
    }

    private Object createBean(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
            Object instance = constructor.newInstance();

            beans.put(clazz, instance);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object getBean(Class<?> clazz) {
        return beans.get(clazz);
    }
}
