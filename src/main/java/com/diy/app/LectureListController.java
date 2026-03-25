package com.diy.app;

import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.controller.Controller;
import com.diy.framework.web.mvc.view.JspView;
import com.diy.framework.web.mvc.view.View;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LectureListController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        ModelAndView mv = new ModelAndView("lecture-list");

        mv.addObject("message", "강의 목록");

        return mv;
    }
}
