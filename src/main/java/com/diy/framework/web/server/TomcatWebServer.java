package com.diy.framework.web.server;

import com.diy.framework.web.servlet.DispatcherServlet;
import com.diy.framework.web.servlet.ServletContextInitializer;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.logging.Level;

public class TomcatWebServer implements WebServer {
    private static final Logger log = LoggerFactory.getLogger(TomcatWebServer.class);

    private final ServletContextInitializer[] initializers;
    private final Servlet dispatcherServlet = new DispatcherServlet();
    private final Tomcat tomcat = new Tomcat();
    private int port = 8080;
    private final Object monitor = new Object();
    private boolean started = false;

    public TomcatWebServer(final ServletContextInitializer... initializers) {
        this.initializers = initializers;
    }

    @Override
    public void start() throws WebServerException {
        synchronized (this.monitor) {
            if (this.started) {
                return;
            }

            try {
                initialize();
                this.tomcat.setPort(port);
                this.tomcat.start();
            } catch (LifecycleException e) {
                throw new WebServerException("Unable to start embedded Tomcat", e);
            }
        }
    }

    private void initialize() {
        this.started = true;
        offTomcatLogger();
        setServerContext();
        startDaemonAwaitThread();
    }

    private void offTomcatLogger() {
        java.util.logging.Logger tomcatCoreLogger = java.util.logging.Logger.getLogger("org.apache");
        tomcatCoreLogger.setLevel(Level.OFF);
    }

    private void startDaemonAwaitThread() {
        Thread awaitThread = new Thread(() -> TomcatWebServer.this.tomcat.getServer().await());
        awaitThread.setContextClassLoader(getClass().getClassLoader());
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    private void setServerContext() {
        final String resourcesPath = Paths.get("src", "main", "resources").toString();
        final String absoluteResourcesPath = new File(resourcesPath).getAbsolutePath();
        log.info("resource path = {}", absoluteResourcesPath);

        final Context context = this.tomcat.addWebapp("/", absoluteResourcesPath);

        context.addServletContainerInitializer(new TomcatStarter(this.initializers), null);

        setServerResources(context);

        setDispatcherServlet(context);
    }

    private void setServerResources(final Context context) {
        final String classPath = getClassPath();
        log.info("current class path = {}", classPath);

        final StandardRoot resources = new StandardRoot(context);
        resources.addPostResources(new DirResourceSet(resources, "/WEB-INF/classes", classPath, "/"));

        context.setResources(resources);
    }

    private void setDispatcherServlet(final Context context) {
        final Wrapper sw = this.tomcat.addServlet(context.getPath(), "dispatcherServlet", dispatcherServlet);
        sw.addMapping("/");
    }

    private String getClassPath() {
        try {
            final CodeSource codeSource = this.getClass().getProtectionDomain().getCodeSource();
            final String classPath = new File(codeSource.getLocation().toURI()).getAbsolutePath();

            log.debug("found class path = {}", classPath);

            return classPath;
        } catch (URISyntaxException e) {
            throw new WebServerException("getClassPath failed.", e);
        }
    }

    @Override
    public void stop() {
        synchronized (this.monitor) {
            try {
                this.started = false;
                this.tomcat.stop();
                this.tomcat.destroy();
            } catch (LifecycleException e) {
                throw new WebServerException("stop failed.", e);
            }
        }
    }
}
