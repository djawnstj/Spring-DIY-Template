package com.diy.framework.web;

import com.diy.app.LectureListController;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/*")
public class DispatcherServlet extends HttpServlet {

    private final Map<String, Controller> handlerMapping = new HashMap<>();

    @Override
    public void init() {
        handlerMapping.put("GET:/lectures", new LectureListController());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");

        String method = req.getMethod();
        String uri = req.getRequestURI();

        String key = method + ":" + uri;

        Controller controller = handlerMapping.get(key);

        if (controller != null) {
            controller.handle(req, resp);
            return;
        }

        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        resp.getWriter().write("404 NOT FOUND");
    }
}
