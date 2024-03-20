package org.web;

import org.web.domain.core.WebApplication;

public class Main {
    public static void main(String[] args) {
        WebApplication webApplication = new WebApplication(8080);
        webApplication.launch();
    }
}
