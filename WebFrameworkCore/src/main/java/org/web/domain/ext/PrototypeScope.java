package org.web.domain.ext;

import org.web.domain.core.Container;
import org.web.domain.core.ScopeAction;
import org.web.domain.core.WebApplicationScope;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class PrototypeScope extends ScopeAction {
    @Override
    protected boolean matchScope(WebApplicationScope scope) {
        return WebApplicationScope.GET_CONTAINER.equals(scope);
    }

    @Override
    protected void register(Class<?> httpController, Container container) {
        this.instance = getNewInstance(httpController, container);
    }

    private Object getNewInstance(Class<?> httpController, Container container){
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
