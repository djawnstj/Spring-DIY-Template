package com.diy.framework.web.mvc.handler;

import com.diy.framework.web.mvc.annotation.RequestMapping;
import com.diy.framework.web.mvc.controller.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class InterfaceHandlerMapping implements HandlerMapping{

    private final Map<String, Controller> handlerMap = new HashMap<>();

    public void initialize(Map<Class<?>, Object> beans) {
        for (Map.Entry<Class<?>, Object> entry : beans.entrySet()) {
            Object bean = entry.getValue();

            if (bean instanceof Controller) {

                RequestMapping requestMapping = bean.getClass().getAnnotation(RequestMapping.class);

                if (requestMapping != null) {
                    handlerMap.put(requestMapping.value(), (Controller) bean);
                }
            }
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return handlerMap.get(request.getRequestURI());
    }
}
