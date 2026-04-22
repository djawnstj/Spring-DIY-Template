package com.diy.framework.web.mvc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@RequestMapping(method = RequestMethod.GET)
@Retention(RetentionPolicy.RUNTIME)
public @interface GetMapping {
    String value();
}
