package com.diy.framework.web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/*")
public class DispatcherServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");

        String method = req.getMethod();
        String uri = req.getRequestURI();

        System.out.println("요청: " + method + " " + uri);

        resp.getWriter().write("Dispatcher 동작 확인");
    }
}
