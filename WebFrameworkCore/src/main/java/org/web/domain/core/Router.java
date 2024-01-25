package org.web.domain.core;

import org.web.infrastructure.exceptions.NotAllowedMethodException;
import org.web.infrastructure.exceptions.NotFindPathException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Router {
    private final Map<String, Map<HTTPMethod, Method>> route;
    private Container container;

    public Router() {
        this.route = new HashMap<>();
    }

    public void setWebApplication(WebApplication webApplication) {
        this.container = webApplication.getContainer();
    }

    public HTTPResponse execute(HTTPRequest httpRequest) {
        container.refresh(WebApplicationScope.HTTP_REQUEST);
        if(!route.containsKey(httpRequest.getHttpPath())){
            throw new NotFindPathException();
        }
        else if(!route.get(httpRequest.getHttpPath()).containsKey(httpRequest.getHttpMethod())){
            throw new NotAllowedMethodException();
        }

        return invokeControllerMethod(httpRequest);
    }

    private HTTPResponse invokeControllerMethod(HTTPRequest httpRequest){
        Method method = route.get(httpRequest.getHttpPath()).get(httpRequest.getHttpMethod());
        try {

            Object object = container.get(method.getDeclaringClass());
            return (HTTPResponse) method.invoke(object, httpRequest);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void addRoute(HTTPMethod httpMethod, String path, Class<?> controllerClass, String function){
        Method method = toControllerMethod(controllerClass, function);

        if (route.containsKey(path)){
            route.get(path).put(httpMethod, method);
        }
        else {
            Map<HTTPMethod, Method> httpMethodMap = new HashMap<>();
            httpMethodMap.put(httpMethod, method);
            route.put(path, httpMethodMap);
        }
    }

    private Method toControllerMethod(Class<?> controllerClass, String function){
        try {
            return controllerClass.getMethod(function, HTTPRequest.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void post(String path, Class<?> controllerClass, String function) {
        addRoute(HTTPMethod.POST, path, controllerClass, function);
    }

    public void patch(String path, Class<?> controllerClass, String function){
        addRoute(HTTPMethod.PATCH, path, controllerClass, function);
    }

    public void get(String path, Class<?> controllerClass, String function){
        addRoute(HTTPMethod.GET, path, controllerClass, function);
    }
}
