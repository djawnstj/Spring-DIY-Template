package com.diy.framework.web.mvc.servlet;

import com.diy.app.LectureCreateController;
import com.diy.app.LectureDeleteController;
import com.diy.app.LectureListController;
import com.diy.app.LectureUpdateController;
import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.controller.Controller;
import com.diy.framework.web.mvc.view.View;
import com.diy.framework.web.mvc.view.ViewResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private final Map<String, Controller> handlerMapping = new HashMap<>();

    @Override
    public void init() {
        handlerMapping.put("GET:/lectures", new LectureListController());
        handlerMapping.put("POST:/lectures", new LectureCreateController());
        handlerMapping.put("PUT:/lectures", new LectureUpdateController());
        handlerMapping.put("DELETE:/lectures", new LectureDeleteController());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("text/html; charset=UTF-8");

        String uri = req.getRequestURI();

        String method = req.getMethod();
        String key = method + ":" + uri;

        Controller controller = handlerMapping.get(key);

        if (controller != null) {
            try {
                ModelAndView mv = controller.handleRequest(req, resp);
                render(mv, req, resp);
            } catch (Exception e) {
                throw new ServletException(e);
            }
            return;
        }

        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        resp.getWriter().write("404 NOT FOUND");
    }

    private void render(ModelAndView mv, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String viewName = mv.getViewName();

        if(viewName.startsWith("redirect:")) {
            String redirectUrl = viewName.substring("redirect:".length());
            resp.sendRedirect(redirectUrl);
        }

        for (Map.Entry<String, Object> entry : mv.getModel().entrySet()) {
            req.setAttribute(entry.getKey(), entry.getValue());
        }

        ViewResolver viewResolver = new ViewResolver();
        View view = viewResolver.resolveView(mv.getViewName());

        view.render(req, resp);
    }
}
