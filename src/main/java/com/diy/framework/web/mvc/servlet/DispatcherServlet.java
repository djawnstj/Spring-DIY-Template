package com.diy.framework.web.mvc.servlet;

import com.diy.app.LectureCreateController;
import com.diy.app.LectureDeleteController;
import com.diy.app.LectureListController;
import com.diy.app.LectureUpdateController;
import com.diy.framework.web.mvc.controller.Controller;

import javax.servlet.ServletException;
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
        handlerMapping.put("POST:/lectures", new LectureCreateController());
        handlerMapping.put("PUT:/lectures", new LectureUpdateController());
        handlerMapping.put("DELETE:/lectures", new LectureDeleteController());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("text/html; charset=UTF-8");

        String method = req.getMethod();
        String uri = req.getRequestURI();

        String key = method + ":" + uri;

        Controller controller = handlerMapping.get(key);

        if (controller != null) {
            controller.handleRequest(req, resp);
            return;
        }

        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        resp.getWriter().write("404 NOT FOUND");
    }
}
