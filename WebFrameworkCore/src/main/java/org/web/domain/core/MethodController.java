package org.web.domain.core;

import java.lang.reflect.Method;

public class MethodController {
    private final HTTPPath httpPath;
    private final HTTPMethod httpMethod;
    private final Method method;

    public MethodController(String path, HTTPMethod httpMethod, Class<?> controllerClass, String functionName) {
        this.httpPath = new HTTPPath(path);
        this.httpMethod = httpMethod;
        this.method = toMethod(controllerClass, functionName);
    }

    public HTTPPath getHttpPath() {
        return httpPath;
    }

    public boolean compareMethodController(HTTPPath httpPath, HTTPMethod httpMethod){
        return this.httpPath.compareHTTPPath(httpPath) && this.httpMethod.equals(httpMethod);
    }

    public Method getMethod(HTTPRequest httpRequest){
        if (compareMethodController(httpRequest.getHttpPath(), httpRequest.getHttpMethod())){
            httpRequest.setHttpPath(httpPath);
            return method;
        }else{
            return null;
        }
    }

    public Method toMethod(Class<?> controllerClass, String functionName){
        try {
            return controllerClass.getMethod(functionName, HTTPRequest.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
