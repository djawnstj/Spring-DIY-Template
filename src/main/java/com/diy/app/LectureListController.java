package com.diy.app;

import com.diy.framework.web.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LectureListController implements Controller {

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getWriter().write("강의 목록");
    }
}
