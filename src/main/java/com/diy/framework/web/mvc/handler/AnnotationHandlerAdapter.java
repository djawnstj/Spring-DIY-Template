package com.diy.framework.web.mvc.handler;

import com.diy.framework.web.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean checkAvailable(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView execute(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return ((HandlerExecution) handler).handle(request, response);
    }
}
