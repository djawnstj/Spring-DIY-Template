package com.diy.app;

import com.diy.framework.web.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LectureCreateController implements Controller {

    @Override
    public void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getWriter().write("강의 등록");
    }
}
