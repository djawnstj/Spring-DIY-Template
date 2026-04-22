package com.diy.framework.web.mvc.servlet;

import com.diy.framework.web.beans.annotation.Component;
import com.diy.framework.web.beans.factory.BeanFactory;
import com.diy.framework.web.beans.factory.BeanScanner;
import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.annotation.GetMapping;
import com.diy.framework.web.mvc.annotation.PostMapping;
import com.diy.framework.web.mvc.handler.AnnotationHandlerMapping;
import com.diy.framework.web.mvc.handler.HandlerExecution;
import com.diy.framework.web.mvc.view.View;
import com.diy.framework.web.mvc.view.ViewResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private final ViewResolver viewResolver = new ViewResolver();
    private AnnotationHandlerMapping handlerMapping;

    @Override
    public void init() {

        BeanScanner scanner = new BeanScanner("com.diy");
        Set<Class<?>> classes = scanner.scanClassesTypeAnnotatedWith(Component.class);
        BeanFactory beanFactory = new BeanFactory(classes);

        this.handlerMapping = new AnnotationHandlerMapping(beanFactory);
        this.handlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("text/html; charset=UTF-8");

        try {

            HandlerExecution handler = handlerMapping.getHandler(req);

            if (handler == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("404 NOT FOUND");
                return;
            }

            ModelAndView mv = handler.handle(req, resp);

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
}
