package com.diy.framework.web.context;

import com.diy.framework.web.beans.factory.BeanFactory;
import com.diy.framework.web.beans.factory.BeanScanner;
import com.diy.framework.web.context.annotation.Autowired;
import com.diy.framework.web.context.annotation.Bean;
import com.diy.framework.web.context.annotation.Component;
import com.diy.framework.web.context.annotation.Configuration;
import com.diy.framework.web.context.support.ApplicationObjectSupport;
import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.server.WebServer;
import com.diy.framework.web.servlet.ServletContextInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApplicationContext implements BeanFactory {
    private static final Logger log = LoggerFactory.getLogger(ApplicationContext.class);
    public static String APPLICATION_CONTEXT_ATTRIBUTE = ApplicationContext.class.getName();

    private final BeanScanner scanner;
    private final Set<Class<?>> beanClasses = new HashSet<>();
    private List<String> beanDefinitionNames = new ArrayList<>(256);
    private final Map<Class<?>, Class<?>> beanMethodOwnerPair = new LinkedHashMap<>();
    private final Map<String, Object> beansMap = new LinkedHashMap<>();
    private final Map<Class<?>, Set<String>> allBeanNamesByType = new LinkedHashMap<>();

    public ApplicationContext(final String componentScanPackage) {
        this.scanner = new BeanScanner("com.diy.framework", componentScanPackage);

        initialize();

        createWebServer();
    }

    public void initialize() {
        beanClasses.addAll(scanner.scanClassesTypeAnnotatedWith(Component.class));
        log.debug("scanned types = {}", List.of(beanClasses));

        extractBeanMethodInConfiguration(beanClasses);
        log.debug("added bean method return type in configuration = {}", beanMethodOwnerPair.keySet());

        createBeansByClass(beanClasses);
        if (log.isDebugEnabled()) {
            log.debug("bean create done. created bean instances = {}", beansMap);

            allBeanNamesByType.forEach((type, beanNames) -> log.debug("bean names mapped by '" + type.getName() + "' type: {}", beanNames));
        }

        initApplicationObjectSupport();
        log.info("Initializing Spring ApplicationContext");
    }

    private void initApplicationObjectSupport() {
        final Map<String, ApplicationObjectSupport> supports = getBeansOfType(ApplicationObjectSupport.class);
        supports.values().forEach(support -> support.setApplicationContext(this));
    }

    private void createWebServer() {
        final WebServer webServer = new TomcatWebServer(getSelfInitializer());
        webServer.start();
    }

    private ServletContextInitializer getSelfInitializer() {
   		return this::selfInitialize;
   	}

    private void selfInitialize(final ServletContext servletContext) {
		prepareWebApplicationContext(servletContext);
	}

    private void prepareWebApplicationContext(final ServletContext servletContext) {
        servletContext.setAttribute(ApplicationContext.APPLICATION_CONTEXT_ATTRIBUTE, this);
    }

    private void extractBeanMethodInConfiguration(final Set<Class<?>> preInstantiatedClasses) {
        preInstantiatedClasses.stream()
                .filter(clazz -> clazz.isAnnotationPresent(Configuration.class))
                .forEach(clazz ->
                        Arrays.stream(clazz.getDeclaredMethods())
                                .filter(method -> method.isAnnotationPresent(Bean.class))
                                .forEach(method -> beanMethodOwnerPair.put(method.getReturnType(), clazz)));
    }

    private void createBeansByClass(final Set<Class<?>> preInstantiatedClasses) {
        for (final Class<?> clazz : preInstantiatedClasses) {
            if (isBeanInitialized(clazz.getName())) continue;

            createBeanInstance(clazz);
        }
    }

    private boolean isBeanInitialized(final String beanName) {
        return beansMap.containsKey(beanName);
    }

    private Object createBeanInstance(final Class<?> clazz) {
        final Constructor<?> constructor = findConstructor(clazz);
        final Method[] autowiredMethods = findAutowiredMethods(clazz);
        final Field[] autowiredFields = findAutowiredFields(clazz);

        try {
            final Object[] parameters = createParameters(constructor.getParameterTypes());

            constructor.setAccessible(true);

            final Object instance = constructor.newInstance(parameters);

            injectAutowiredMethod(autowiredMethods, instance);

            injectAutowiredFields(autowiredFields, instance);

            saveBean(clazz.getName(), instance);

            if (clazz.isAnnotationPresent(Configuration.class)) {
                createBeanInConfigurationAnnotatedClass(instance);
            }

            return instance;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            constructor.setAccessible(false);
        }
    }

    private Constructor<?> findConstructor(final Class<?> clazz) {
        final Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        if (constructors.length == 1) {
            return constructors[0];
        }

        return findAutowiredConstructor(constructors);
    }

    private Constructor<?> findAutowiredConstructor(final Constructor<?>[] constructors) {
        final Constructor<?>[] autowiredConstructors = Arrays.stream(constructors)
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .toArray(Constructor[]::new);

        if (autowiredConstructors.length != 1) {
            throw new RuntimeException("Autowire constructor not found");
        }

        return autowiredConstructors[0];
    }

    private Field[] findAutowiredFields(final Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Autowired.class))
                .toArray(Field[]::new);
    }

    private Method[] findAutowiredMethods(final Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Autowired.class))
                .toArray(Method[]::new);
    }

    private Object[] createParameters(final Class<?>[] parameterTypes) {
        final List<Class<?>> parameterTypesCollection = List.of(parameterTypes);

        if (parameterTypesCollection.isEmpty()) {
            return new Object[]{};
        }

        if (!beanClasses.containsAll(parameterTypesCollection) && !beanMethodOwnerPair.keySet().containsAll(parameterTypesCollection)) {
            log.error("parameter is not bean. parameterTypes = {}", parameterTypesCollection);
            throw new RuntimeException("parameter is not bean");
        }

        return Arrays.stream(parameterTypes).map(parameterType -> {
            if (isBeanInitialized(parameterType.getName())) {
                return getBean(parameterType.getSimpleName());
            }

            if (beanClasses.contains(parameterType)) {
                return createBeanInstance(parameterType);
            }

            final Class<?> configurationClazz = beanMethodOwnerPair.get(parameterType);

            if (!isBeanInitialized(configurationClazz.getName())) {
                createBeanInstance(configurationClazz);
            }

            return getBean(parameterType);
        }).toArray();
    }

    private void injectAutowiredMethod(final Method[] methods, final Object instance) {
        Arrays.stream(methods).forEach(method -> {
            try {
                method.setAccessible(true);

                final Object[] parameters = createParameters(method.getParameterTypes());

                method.invoke(instance, parameters);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            } finally {
                method.setAccessible(false);
            }
        });
    }

    private void injectAutowiredFields(final Field[] fields, final Object instance) throws IllegalAccessException {
        Arrays.stream(fields).forEach(field -> {
            try {
                field.setAccessible(true);

                if (!isBeanInitialized(field.getType().getSimpleName())) {
                    createBeanInstance(field.getType());
                }

                field.set(instance, getBean(field.getType()));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } finally {
                field.setAccessible(false);
            }
        });
    }

    private void saveBean(final String beanName, final Object bean) {
        this.beansMap.put(beanName, bean);
        this.beanDefinitionNames.add(beanName);
        mapToSuperTypes(bean.getClass())
                .forEach(clazz -> this.allBeanNamesByType.computeIfAbsent(clazz, beanType -> new HashSet<>())
                        .add(beanName));
    }

    private Set<Class<?>> mapToSuperTypes(final Class<?> clazz) {
        final HashSet<Class<?>> superTypes = new HashSet<>();
        Class<?> superClass = clazz;

        while (superClass != null) {
            final Class<?>[] interfaces = superClass.getInterfaces();
            superTypes.add(superClass);
            superTypes.addAll(List.of(interfaces));

            superClass = superClass.getSuperclass();
        }

        return superTypes;
    }

    private void createBeanInConfigurationAnnotatedClass(final Object configuration) {
        final Class<?> subclass = configuration.getClass();
        final Method[] methods = getBeanAnnotatedMethods(subclass);

        Arrays.stream(methods).forEach(method -> {
            try {
                method.setAccessible(true);
                final Object[] parameters = createParameters(method.getParameterTypes());
                final Object instance = method.invoke(configuration, parameters);
                final Bean annotation = method.getAnnotation(Bean.class);
                final String beanName = (annotation.value().isBlank()) ? method.getName() : annotation.value();
                saveBean(beanName, instance);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Method[] getBeanAnnotatedMethods(final Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .toArray(Method[]::new);
    }

    @Override
    public Object getBean(final String name) {
        return this.beansMap.get(name);
    }

    @Override
    public <T> T getBean(final Class<T> requiredType) {
        final Set<String> beanNames = this.allBeanNamesByType.get(requiredType);

        if (beanNames == null) {
            throw new RuntimeException("Bean not found '" + requiredType + "'");
        } else if (beanNames.size() != 1) {
            throw new RuntimeException("No qualifying bean of type '" + requiredType
                    + "' available: expected single matching bean but found " + beanNames.size() + ": " + String.join(", ", beanNames));
        }

        final String beanName = beanNames.stream().findFirst().get();

        return (T) this.beansMap.get(beanName);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(final Class<T> type) {
        final String[] beanNames = getBeanNamesForType(type);
        final LinkedHashMap<String, T> result = new LinkedHashMap<>(beanNames.length);

        Arrays.stream(beanNames)
                .forEach(beanName -> result.put(beanName, (T) getBean(beanName)));

        return result;
    }

    @Override
    public <T> String[] getBeanNamesForType(final Class<T> type) {
        final Set<String> beanNames = this.allBeanNamesByType.get(type);

        if (beanNames == null) {
            throw new RuntimeException("Bean names not found '" + type.getName() + "'");
        }

        return beanNames.toArray(String[]::new);
    }

    @Override
    public String[] getBeanNamesForAnnotation(final Class<? extends Annotation> annotationType) {
        List<String> result = new ArrayList<>();

        for (String beanName : this.beanDefinitionNames) {
            final Object bean = getBean(beanName);
            if (bean != null && findAnnotationOnBean(bean, annotationType) != null) {
                result.add(beanName);
            }
        }

        return result.toArray(new String[0]);
    }

    @Override
   	public <A extends Annotation> A findAnnotationOnBean(final Object bean, final Class<A> annotationType) {
        final Set<Class<?>> classes = mapToSuperTypes(bean.getClass());
        for (Class<?> clazz : classes) {
            final A found = findAnnotationIncludingMeta(clazz, annotationType, new HashSet<>());
            if (found != null) return found;
        }

        return null;
    }

    private <A extends Annotation> A findAnnotationIncludingMeta(final Class<?> element,
                                                                 final Class<A> target,
                                                                 final Set<Class<? extends Annotation>> visited) {
        final A direct = element.getAnnotation(target);
        if (direct != null) return direct;
        for (final Annotation ann : element.getAnnotations()) {
            final Class<? extends Annotation> annType = ann.annotationType();
            if (!visited.add(annType)) continue;
            final A found = findAnnotationIncludingMeta(annType, target, visited);
            if (found != null) return found;
        }
        return null;
    }
}
