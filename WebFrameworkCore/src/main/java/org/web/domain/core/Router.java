package org.web.domain.core;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Router {
    private final Map<String, Function<HTTPRequest, HTTPResponse>> route;

    public Router() {
        this.route = new HashMap<>();
    }

    public HTTPResponse execute(HTTPRequest httpRequest) {
        String path = getPath(httpRequest.getHttpMethod(), httpRequest.getHttpPath());
        return route.get(path).apply(httpRequest);
    }

    private String getPath(HTTPMethod httpMethod, String path){
        return String.format("[%s]%s",httpMethod.name(),  path);
    }

    private void addRoute(HTTPMethod httpMethod, String path, Function<HTTPRequest, HTTPResponse> function){
        route.put(getPath(httpMethod, path), function);
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
