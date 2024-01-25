package org.web.domain.core;

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
}
