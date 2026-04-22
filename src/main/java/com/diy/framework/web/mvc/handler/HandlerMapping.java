package com.diy.framework.web.mvc.handler;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    Object getHandler(HttpServletRequest request);
}
