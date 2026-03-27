package com.diy.framework.web.beans.factory;

import com.diy.framework.web.beans.annotation.Autowired;

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
            Constructor<?> constructor = findConstructor(clazz);
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] args = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> paramType = parameterTypes[i];
                Object dependency = beans.get(paramType);

                if (dependency == null) {
                    dependency = createBean(paramType);
                }
                args[i] = dependency;
            }

            Object instance = constructor.newInstance(args);

            beans.put(clazz, instance);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object getBean(Class<?> clazz) {
        return beans.get(clazz);
    }

    private Constructor<?> findConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        for (Constructor<?> constructor : constructors) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                return constructor;
            }
        }

        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(clazz.getName() + "에 알맞은 생성자가 없음");
        }
    }
}
