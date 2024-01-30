package org.web.domain.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public abstract class ScopeAction {
    protected Object instance;

    public Object getInstance() {
        return instance;
    }

    public void execute(WebApplicationScope scope, Class<?> httpController, Container container){
        if (matchScope(scope)){
            register(httpController, container);
        }
    }

    protected abstract boolean matchScope(WebApplicationScope scope);
    protected abstract void register(Class<?> httpController, Container container);

    protected Object getNewInstance(Class<?> httpController, Container container){
        List<Object> constructorArgs = getConstructorArgs(httpController, container);
        Object[] initArgs = constructorArgs.isEmpty()? null: constructorArgs.toArray(Object[]::new);
        try {
            return httpController.getDeclaredConstructor(toClassArray(constructorArgs)).newInstance(initArgs);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Object> getConstructorArgs(Class<?> dependency, Container container){
        List<Object> objects = new ArrayList<>();
        for (Constructor<?> constructor : dependency.getDeclaredConstructors()) {
            Parameter[] parameters = constructor.getParameters();
            for (Parameter parameter : parameters) {
                objects.add(container.get(parameter.getType()));
            }
        }
        return objects;
    }

    private Class<?>[] toClassArray(List<Object> constructorArgs){
        return constructorArgs.stream().map(Object::getClass).toArray(Class<?>[]::new);
    }
}
