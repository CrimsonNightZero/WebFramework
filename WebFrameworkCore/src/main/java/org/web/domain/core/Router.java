package org.web.domain.core;

import org.web.domain.core.exceptions.NotAllowedMethodException;
import org.web.domain.core.exceptions.NotFindPathException;

import java.lang.reflect.*;
import java.util.*;

public class Router extends HTTPHandler{
    private final List<MethodController> methodControllers;
    private Container container;
    private ExceptionHandler<?> exceptionHandler;
    private TransformBodyTypeHandler transformBodyTypeHandler;

    public Router() {
        this.methodControllers = new ArrayList<>();
    }

    public void setWebApplication(WebApplication webApplication) {
        this.container = webApplication.getContainer();
    }

    public void registerException(ExceptionHandler<?> exceptionHandler){
        exceptionHandler.setNext(this.exceptionHandler);
        this.exceptionHandler = exceptionHandler;
    }

    public void registerTransferDataType(TransformBodyTypeHandler transformBodyTypeHandler) {
        transformBodyTypeHandler.setNext(this.transformBodyTypeHandler);
        this.transformBodyTypeHandler = transformBodyTypeHandler;
    }

    private void addRoute(HTTPMethod httpMethod, String path, Class<?> controllerClass, String function){
        methodControllers.add(new MethodController(path, httpMethod, controllerClass, function));
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

    protected HTTPResponse handle(HTTPRequest httpRequest){
        try{
            container.refresh(WebApplicationScope.HTTP_REQUEST);

            if(illegalHTTPPath(httpRequest.getHttpPath())){
                throw new NotFindPathException();
            }
            else if(illegalHTTPMethod(httpRequest.getHttpPath(), httpRequest.getHttpMethod())){
                throw new NotAllowedMethodException();
            }

            Method method = methodControllers.stream().map(methodController -> methodController.getMethod(httpRequest)).filter(Objects::nonNull).findFirst().get();
            Object object = container.get(method.getDeclaringClass());
            try{
                return toHTTPResponse(method.invoke(object, getMethodArgs(method, httpRequest)));
            }catch (InvocationTargetException ex){
                throw ex.getCause();
            }
        }catch (Throwable ex){
            ex.printStackTrace();
            HTTPResponse httpResponse = exceptionHandler.handle(httpRequest, ex);
            httpResponse.setBody(deserializeHTTPResponse(httpResponse, httpResponse.getBody()));
            return httpResponse;
        }
    }

    private boolean illegalHTTPPath(HTTPPath path){
        for (MethodController methodController : methodControllers){
            if(methodController.getHttpPath().compareHTTPPath(path)){
                return false;
            }
        }
        return true;
    }

    private boolean illegalHTTPMethod(HTTPPath path, HTTPMethod method){
        for (MethodController methodController : methodControllers){
            if(methodController.compareMethodController(path, method)){
                return false;
            }
        }
        return true;
    }

    private HTTPResponse toHTTPResponse(Object httpResponseObject){
        HTTPResponse httpResponse = response();
        if (httpResponseObject instanceof HTTPResponse response){
            if (hasHTTPHeaders(response)){
                httpResponse.setHttpHeaders(response.getHttpHeaders());
            }
            httpResponse.setHttpStatusCode(response.getHttpStatusCode());
            httpResponseObject = response.getBody();
        }
        httpResponse.setBody(deserializeHTTPResponse(httpResponse, httpResponseObject));
        return httpResponse;
    }

    private HTTPResponse response() {
        HTTPResponse httpResponse = new HTTPResponse(200);
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("content-encoding", "UTF-8");
        httpResponse.setHttpHeaders(headers);
        return httpResponse;
    }

    private boolean hasHTTPHeaders(HTTPResponse response){
        return !response.getHttpHeaders().isEmpty();
    }

    private Object[] getMethodArgs(Method method, HTTPRequest httpRequest){
        Object[] objects = new Object[method.getParameterTypes().length];
        for (int i = 0; i < method.getParameterTypes().length; i++) {
            Class<?> parameterType = method.getParameterTypes()[i];

            if (parameterType.equals(httpRequest.getClass())){
                objects[i] = httpRequest;
                continue;
            }

            Object args = serializeHTTPRequest(method, parameterType, httpRequest);
            if (parameterType.equals(args.getClass())){
                objects[i] = args;
            }
        }

        if (Arrays.stream(objects).anyMatch(Objects::isNull)){
            throw new IllegalArgumentException();
        }
        return objects;
    }

    private Object serializeHTTPRequest(Method method, Class<?> parameterType, HTTPRequest httpRequest){
        try{
            if (method.getParameters().length > 0){
                return transformBodyTypeHandler.serialize(httpRequest.getHttpHeaders().get("content-type"), httpRequest.getBody(), parameterType);
            }
        }catch (RuntimeException ignored){}
        return new Object();
    }

    private String deserializeHTTPResponse(HTTPResponse httpResponse, Object responseBody){
        return transformBodyTypeHandler.deserialize(httpResponse.getHttpHeaders().get("content-type"), responseBody);
    }
}
