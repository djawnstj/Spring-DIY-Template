package com.diy.framework.web.mvc.handler;

import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.controller.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InterfaceHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean checkAvailable(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView execute(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {

        return ((Controller) handler).handle(request, response);
    }
}
