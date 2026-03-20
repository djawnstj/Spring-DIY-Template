package com.diy.app;

import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.controller.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LectureUpdateController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        return new ModelAndView("lecture-list");
    }
}
