package org.web.domain.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTTPPath {
    private final String path;

    private final Map<String, Object> pathVariable;

    public HTTPPath(String path) {
        this.path = path;
        this.pathVariable = new HashMap<>();
        parsePathVariable();
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getPathVariable(){
        return pathVariable;
    }

    public boolean comparePath(String path) {
        return this.path.equals(path);
    }

    private void parsePathVariable(){
        String[] httpPathResources = this.path.split("/");
        for (String httpPathResource : httpPathResources) {
            if (isPathVariable(httpPathResource)){
                pathVariable.put(getHttpPathResource(httpPathResource), null);
            }
        }
    }

    private String getHttpPathResource(String httpPathResource){
        return httpPathResource.substring(1, httpPathResource.length() - 1);
    }

    public boolean compareHTTPPath(HTTPPath path){
        String[] pathSource = path.getPath().split("/");
        List<String> httpPathResources = Arrays.asList(this.path.split("/"));
        if (pathSource.length != httpPathResources.size()){
            return false;
        }

        for (String httpPathResource : httpPathResources) {
            if (isPathVariable(httpPathResource)){
                setPathVariable(httpPathResource, pathSource[httpPathResources.indexOf(httpPathResource)]);
                System.out.println(httpPathResource);
                continue;
            }
            if (!httpPathResource.equals(pathSource[httpPathResources.indexOf(httpPathResource)])){
                return false;
            }
        }
        return true;
    }

    private void setPathVariable(String key, Object value){
        pathVariable.put(getHttpPathResource(key), value);
    }

    private boolean isPathVariable(String path){
        return path.startsWith("{") && path.endsWith("}");
    }

    @Override
    public String toString() {
        return path;
    }
}
