package org.web.domain.core;

import org.web.infrastructure.exceptions.NotAllowedMethodException;
import org.web.infrastructure.exceptions.NotFindPathException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Router {
    private final Map<String, Map<HTTPMethod, Function<HTTPRequest, HTTPResponse>>> route;

    public Router() {
        this.route = new HashMap<>();
    }

    public HTTPResponse execute(HTTPRequest httpRequest) {
        if(!route.containsKey(httpRequest.getHttpPath())){
            throw new NotFindPathException();
        }
        else if(!route.get(httpRequest.getHttpPath()).containsKey(httpRequest.getHttpMethod())){
            throw new NotAllowedMethodException();
        }
        return route.get(httpRequest.getHttpPath()).get(httpRequest.getHttpMethod()).apply(httpRequest);
    }

    private void addRoute(HTTPMethod httpMethod, String path, Function<HTTPRequest, HTTPResponse> function){
        if (route.containsKey(path)){
            route.get(path).put(httpMethod, function);
        }
        else {
            Map<HTTPMethod, Function<HTTPRequest, HTTPResponse>> httpMethodMap = new HashMap<>();
            httpMethodMap.put(httpMethod, function);
            route.put(path, httpMethodMap);
        }
    }

    public void post(String path, Function<HTTPRequest, HTTPResponse> function){
        addRoute(HTTPMethod.POST, path, function);
    }

    public void patch(String path, Function<HTTPRequest, HTTPResponse> function){
        addRoute(HTTPMethod.PATCH, path, function);
    }

    public void get(String path, Function<HTTPRequest, HTTPResponse> function){
        addRoute(HTTPMethod.GET, path, function);
    }
}
