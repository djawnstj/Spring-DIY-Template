package com.diy.app;

import com.diy.framework.web.mvc.controller.Controller;
import com.diy.framework.web.mvc.view.JspView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LectureListController implements Controller {

    @Override
    public void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.getWriter().write("강의 목록");

        JspView view = new JspView("/lecture-list.jsp");
        view.render(req, resp);
    }
}
