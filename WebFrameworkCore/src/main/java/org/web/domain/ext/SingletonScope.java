package org.web.domain.ext;

import org.web.domain.core.Container;
import org.web.domain.core.ScopeAction;
import org.web.domain.core.WebApplicationScope;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SingletonScope extends ScopeAction {

    @Override
    protected boolean matchScope(WebApplicationScope scope) {
        return WebApplicationScope.GET_CONTAINER.equals(scope);
    }

    @Override
    protected void register(Class<?> httpController, Container container) {
        getInstance(httpController, container);
    }

    private void getInstance(Class<?> dependency, Container container){
        try {
            if (Objects.isNull(instance)){
                List<Object> constructorArgs = getConstructorArgs(dependency, container);
                Object[] initArgs = constructorArgs.isEmpty()? null: constructorArgs.toArray(Object[]::new);
                this.instance = dependency.getDeclaredConstructor(toClassArray(constructorArgs)).newInstance(initArgs);
            } else {
                substituteConstructorArgs(dependency, container);
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
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

    private void substituteConstructorArgs(Class<?> dependency, Container container){
        for (Constructor<?> constructor : dependency.getDeclaredConstructors()) {
            Parameter[] parameters = constructor.getParameters();
            for (Parameter parameter : parameters) {
                setInstanceField(container, parameter);
            }
        }
    }

    private void setInstanceField(Container container, Parameter parameter){
        Field[] fields = instance.getClass().getFields();
        try {
            for (Field field: fields){
                field.setAccessible(true);
                field.set(instance, container.get(parameter.getType()));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
