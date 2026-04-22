package com.diy.framework.web.mvc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@RequestMapping(methods = RequestMethod.POST)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostMapping {
    String value();
}
