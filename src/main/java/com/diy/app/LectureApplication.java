package com.diy.app;

import com.diy.framework.web.context.ApplicationContext;

public class LectureApplication {
    public static void main(String[] args) {
        final ApplicationContext applicationContext = new ApplicationContext(LectureApplication.class.getPackageName());
        applicationContext.initialize();
    }
}
