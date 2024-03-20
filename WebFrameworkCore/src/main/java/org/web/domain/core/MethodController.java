package org.web.domain.core;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

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

    public boolean compareMethodController(HTTPPath httpPath, HTTPMethod httpMethod) {
        return this.httpPath.compareHTTPPath(httpPath) && this.httpMethod.equals(httpMethod);
    }

    public Method getMethod(HTTPRequest httpRequest) {
        if (compareMethodController(httpRequest.getHttpPath(), httpRequest.getHttpMethod())) {
            httpRequest.setHttpPath(httpPath);
            return method;
        } else {
            return null;
        }
    }

    public Method toMethod(Class<?> controllerClass, String functionName) {
        try {
            return controllerClass.getDeclaredMethod(functionName,
                    getMethodParameterTypes(controllerClass, functionName));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Class<?>[] getMethodParameterTypes(Class<?> controllerClass, String functionName) {
        Parameter[] parameters = Arrays.stream(controllerClass.getMethods())
                .filter(method -> method.getName().equals(functionName)).findFirst().get().getParameters();
        return Arrays.stream(parameters).map(Parameter::getType).toArray(Class[]::new);
    }
}
