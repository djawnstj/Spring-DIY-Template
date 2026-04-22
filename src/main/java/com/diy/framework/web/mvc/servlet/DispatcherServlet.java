package com.diy.framework.web.mvc.servlet;

import com.diy.framework.web.beans.annotation.Component;
import com.diy.framework.web.beans.factory.BeanFactory;
import com.diy.framework.web.beans.factory.BeanScanner;
import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.controller.Controller;
import com.diy.framework.web.mvc.handler.AnnotationHandlerMapping;
import com.diy.framework.web.mvc.handler.HandlerExecution;
import com.diy.framework.web.mvc.handler.HandlerMapping;
import com.diy.framework.web.mvc.handler.InterfaceHandlerMapping;
import com.diy.framework.web.mvc.view.View;
import com.diy.framework.web.mvc.view.ViewResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private final ViewResolver viewResolver = new ViewResolver();
    private List<HandlerMapping> handlerMappings;

    @Override
    public void init() {

        BeanScanner scanner = new BeanScanner("com.diy");
        Set<Class<?>> classes = scanner.scanClassesTypeAnnotatedWith(Component.class);
        BeanFactory beanFactory = new BeanFactory(classes);

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(beanFactory);
        annotationHandlerMapping.initialize();

        this.handlerMappings = List.of(
                annotationHandlerMapping,
                new InterfaceHandlerMapping()
        );
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("text/html; charset=UTF-8");

        try {

            Object handler = null;
            for (HandlerMapping handlerMapping : handlerMappings) {
                handler = handlerMapping.getHandler(req);
                if (handler != null) break;
            }

            if (handler == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("404 NOT FOUND");
                return;
            }

            ModelAndView mv = execute(handler, req, resp);
            render(mv, req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void render(ModelAndView mv, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String viewName = mv.getViewName();

        if(viewName.startsWith("redirect:")) {
            String redirectUrl = viewName.substring("redirect:".length());
            resp.sendRedirect(redirectUrl);
            return;
        }

        for (Map.Entry<String, Object> entry : mv.getModel().entrySet()) {
            req.setAttribute(entry.getKey(), entry.getValue());
        }

        View view = viewResolver.resolveView(mv.getViewName());

        view.render(req, resp);
    }

    private ModelAndView execute(Object handler, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (handler instanceof HandlerExecution) {
            return ((HandlerExecution) handler).handle(req, resp);
        }
        if (handler instanceof Controller) {
            return ((Controller) handler).handle(req, resp);
        }
        throw new RuntimeException("지원하지 않는 핸들러 타입");
    }
}
