package org.web.domain.core;

import org.web.infrastructure.exceptions.NotAllowedMethodException;
import org.web.infrastructure.exceptions.NotFindPathException;

import java.lang.reflect.Method;
import java.util.*;

public class Router {
    private final List<RoutePath> route;
    private Container container;

    public Router() {
        this.route = new ArrayList<>();
    }

    public void setWebApplication(WebApplication webApplication) {
        this.container = webApplication.getContainer();
    }

    public HTTPResponse execute(HTTPRequest httpRequest) throws Throwable {
        container.refresh(WebApplicationScope.HTTP_REQUEST);
        if(illegalHTTPPath(httpRequest.getHttpPath())){
            throw new NotFindPathException();
        }
        else if(illegalHTTPMethod(httpRequest.getHttpPath(), httpRequest.getHttpMethod())){
            throw new NotAllowedMethodException();
        }

        return invokeControllerMethod(httpRequest);
    }

    private boolean illegalHTTPPath(HTTPPath path){
        for (RoutePath routePath : route){
            if(routePath.getHttpPath().compareHTTPPath(path)){
                return false;
            }
        }
        return true;
    }

    private boolean illegalHTTPMethod(HTTPPath path, HTTPMethod method){
        for (RoutePath routePath : route){
            if(routePath.compareRoutePath(path, method)){
                return false;
            }
        }
        return true;
    }

    private HTTPResponse invokeControllerMethod(HTTPRequest httpRequest) throws Throwable {
        Method method = route.stream().map(routePath -> routePath.getMethod(httpRequest)).filter(Objects::nonNull).findFirst().get();

        Object object = container.get(method.getDeclaringClass());
        try{
            return (HTTPResponse) method.invoke(object, httpRequest);
        }catch (Exception ex){
            throw ex.getCause();
        }
    }

    private void addRoute(HTTPMethod httpMethod, String path, Class<?> controllerClass, String function){
        route.add(new RoutePath(path, httpMethod, controllerClass, function));
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
