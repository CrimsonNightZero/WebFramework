package org.web.domain.core;

public abstract class ExceptionHandler {
    private ExceptionHandler next;

    public ExceptionHandler() {
        this.next = null;
    }

    public void setNext(ExceptionHandler next) {
        this.next = next;
    }

    public HTTPResponse handle(HTTPRequest httpRequest, Throwable throwable){
        if (matchThrownException(throwable)){
            return response(httpRequest, throwable);
        }
        else {
            return next.handle(httpRequest, throwable);
        }
    }

    protected abstract boolean matchThrownException(Throwable throwable);

    protected abstract HTTPResponse response(HTTPRequest httpRequest, Throwable throwable);
}
