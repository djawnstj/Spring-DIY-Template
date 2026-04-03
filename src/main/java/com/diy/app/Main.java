package com.diy.app;

import com.diy.framework.web.server.TomcatWebServer;
import org.apache.catalina.LifecycleException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Logger.getLogger("org.apache").setLevel(Level.WARNING);
        final TomcatWebServer tomcat = new TomcatWebServer();

        try {
            tomcat.start();
            System.out.println("서버 실행");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
