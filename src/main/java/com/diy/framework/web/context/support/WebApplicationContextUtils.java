package com.diy.framework.web.context.support;

import com.diy.framework.web.context.ApplicationContext;

import javax.servlet.ServletContext;

public abstract class WebApplicationContextUtils {
    public static ApplicationContext getWebApplicationContext(final ServletContext sc, final String attrName) {
        final Object attr = sc.getAttribute(attrName);

        if (!(attr instanceof ApplicationContext)) {
            throw new IllegalStateException("Context attribute is not of type ApplicationContext: " + attr);
        }

        return (ApplicationContext) attr;
    }
}
