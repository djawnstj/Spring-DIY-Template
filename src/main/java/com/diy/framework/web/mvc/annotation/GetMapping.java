package com.diy.framework.web.mvc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@RequestMapping(methods = RequestMethod.GET)
@Retention(RetentionPolicy.RUNTIME)
public @interface GetMapping {
    String value();
}
