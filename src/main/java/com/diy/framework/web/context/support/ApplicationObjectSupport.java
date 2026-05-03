package com.diy.framework.web.context.support;

import com.diy.framework.web.context.ApplicationContext;

public abstract class ApplicationObjectSupport {

    protected abstract void initApplicationContext(final ApplicationContext context);

    public final void setApplicationContext(final ApplicationContext context) {
        this.initApplicationContext(context);
    }
}
