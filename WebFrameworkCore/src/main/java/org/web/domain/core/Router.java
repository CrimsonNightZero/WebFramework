package org.web.domain.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.web.infrastructure.exceptions.NotAllowedMethodException;
import org.web.infrastructure.exceptions.NotFindPathException;

public class Router {
    private final List<MethodController> methodControllers;
    private Container container;

    public Router() {
        this.methodControllers = new ArrayList<>();
    }

    public void setWebApplication(WebApplication webApplication) {
        this.container = webApplication.getContainer();
    }

    public HTTPResponse execute(HTTPRequest httpRequest) throws Throwable {
        container.refresh(WebApplicationScope.HTTP_REQUEST);
        if (illegalHTTPPath(httpRequest.getHttpPath())) {
            throw new NotFindPathException();
        } else if (illegalHTTPMethod(httpRequest.getHttpPath(), httpRequest.getHttpMethod())) {
            throw new NotAllowedMethodException();
        }

        return invokeControllerMethod(httpRequest);
    }

    private boolean illegalHTTPPath(HTTPPath path) {
        for (MethodController methodController : methodControllers) {
            if (methodController.getHttpPath().compareHTTPPath(path)) {
                return false;
            }
        }
        return true;
    }

    private boolean illegalHTTPMethod(HTTPPath path, HTTPMethod method) {
        for (MethodController methodController : methodControllers) {
            if (methodController.compareMethodController(path, method)) {
                return false;
            }
        }
        return true;
    }

    private HTTPResponse invokeControllerMethod(HTTPRequest httpRequest) throws Throwable {
        Method method = methodControllers.stream().map(methodController -> methodController.getMethod(httpRequest))
                .filter(Objects::nonNull).findFirst().get();
        Object object = container.get(method.getDeclaringClass());
        try {
            return (HTTPResponse) method.invoke(object, httpRequest);
        } catch (Exception ex) {
            throw ex.getCause();
        }
    }

    private void addRoute(HTTPMethod httpMethod, String path, Class<?> controllerClass, String function) {
        methodControllers.add(new MethodController(path, httpMethod, controllerClass, function));
    }

    public void post(String path, Class<?> controllerClass, String function) {
        addRoute(HTTPMethod.POST, path, controllerClass, function);
    }

    public void patch(String path, Class<?> controllerClass, String function) {
        addRoute(HTTPMethod.PATCH, path, controllerClass, function);
    }

    public void get(String path, Class<?> controllerClass, String function) {
        addRoute(HTTPMethod.GET, path, controllerClass, function);
    }
}
