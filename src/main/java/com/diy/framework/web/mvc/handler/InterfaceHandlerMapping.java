package com.diy.framework.web.mvc.handler;

import com.diy.framework.web.mvc.controller.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class InterfaceHandlerMapping implements HandlerMapping{

    private final Map<String, Controller> handlerMap = new HashMap<>();

    public void addHandler(String url, Controller controller) {
        handlerMap.put(url, controller);
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return handlerMap.get(request.getRequestURI());
    }
}
