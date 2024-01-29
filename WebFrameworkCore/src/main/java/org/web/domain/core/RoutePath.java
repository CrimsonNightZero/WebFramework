package org.web.domain.core;

import java.lang.reflect.Method;

public class RoutePath {
    private final HTTPPath httpPath;
    private final HTTPMethod httpMethod;
    private final Method method;

    public RoutePath(String path, HTTPMethod httpMethod, Class<?> controllerClass, String function) {
        this.httpPath = new HTTPPath(path);
        this.httpMethod = httpMethod;
        this.method = toControllerMethod(controllerClass, function);
    }

    public HTTPPath getHttpPath() {
        return httpPath;
    }

    public boolean compareRoutePath(HTTPPath httpPath, HTTPMethod httpMethod){
        return this.httpPath.compareHTTPPath(httpPath) && this.httpMethod.equals(httpMethod);
    }

    public Method getMethod(HTTPRequest httpRequest){
        if (compareRoutePath(httpRequest.getHttpPath(), httpRequest.getHttpMethod())){
            httpRequest.setHttpPath(httpPath);
            return method;
        }else{
            return null;
        }
    }

    public Method toControllerMethod(Class<?> controllerClass, String function){
        try {
            return controllerClass.getMethod(function, HTTPRequest.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
