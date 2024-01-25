package org.web.domain.core;

import java.util.HashMap;
import java.util.Map;

public class Container {
    private final Map<Class<?>, ScopeAction> instances;
    public Container() {
        this.instances = new HashMap<>();
    }

    public void register(Class<?> dependency, ScopeAction scopeAction){
        instances.put(dependency, scopeAction);
    }

    public <T> T get(Class<?> dependency){
        refresh(WebApplicationScope.GET_CONTAINER, dependency);
        return instances.containsKey(dependency) ? (T)instances.get(dependency).getInstance(): null;
    }

    public void refresh(WebApplicationScope scope,Class<?>  dependency){
        instances.get(dependency).execute(scope, dependency, this);
    }

    public void refresh(WebApplicationScope scope){
        for (Map.Entry<Class<?>, ScopeAction> map: instances.entrySet()){
            map.getValue().execute(scope, map.getKey(), this);
        }
    }
}
