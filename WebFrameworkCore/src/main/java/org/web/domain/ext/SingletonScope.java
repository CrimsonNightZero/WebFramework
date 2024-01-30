package org.web.domain.ext;

import org.web.domain.core.Container;
import org.web.domain.core.ScopeAction;
import org.web.domain.core.WebApplicationScope;
import java.util.Objects;

public class SingletonScope extends ScopeAction {

    @Override
    protected boolean matchScope(WebApplicationScope scope) {
        return WebApplicationScope.GET_CONTAINER.equals(scope);
    }

    @Override
    protected void register(Class<?> httpController, Container container) {
        this.instance = Objects.isNull(instance)? getNewInstance(httpController, container): instance;
    }
}
