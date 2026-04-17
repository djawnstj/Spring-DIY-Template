package com.diy.framework.web.mvc.servlet;

import com.diy.app.LectureCreateController;
import com.diy.app.LectureDeleteController;
import com.diy.app.LectureListController;
import com.diy.app.LectureUpdateController;
import com.diy.framework.web.beans.annotation.Component;
import com.diy.framework.web.beans.factory.BeanFactory;
import com.diy.framework.web.beans.factory.BeanScanner;
import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.annotation.GetMapping;
import com.diy.framework.web.mvc.controller.Controller;
import com.diy.framework.web.mvc.view.View;
import com.diy.framework.web.mvc.view.ViewResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private final Map<String, Controller> handlerMapping = new HashMap<>();
    private final ViewResolver viewResolver = new ViewResolver();

    private BeanFactory beanFactory;

    @Override
    public void init() {

        BeanScanner scanner = new BeanScanner("com.diy");
        Set<Class<?>> classes = scanner.scanClassesTypeAnnotatedWith(Component.class);

        this.beanFactory = new BeanFactory(classes);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("text/html; charset=UTF-8");

        String uri = req.getRequestURI();
        String method = req.getMethod();

        try {
            for (Object bean : beanFactory.getBeans().values()) {

                Class<?> clazz = bean.getClass();

                for (Method m : clazz.getDeclaredMethods()) {

                    if (method.equals("GET") && m.isAnnotationPresent(GetMapping.class)) {
                        GetMapping mapping = m.getAnnotation(GetMapping.class);

                        if (mapping.value().equals(uri)) {
                            ModelAndView mv = (ModelAndView) m.invoke(bean);
                            render(mv, req, resp);
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }

        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        resp.getWriter().write("404 NOT FOUND");
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
