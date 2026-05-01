package com.diy.framework.web.mvc.handler;

import com.diy.framework.web.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean checkAvailable(Object handler);

    ModelAndView execute(Object handler,
                         HttpServletRequest request,
                         HttpServletResponse response) throws Exception;
}
