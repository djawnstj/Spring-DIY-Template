package com.diy.app;

import com.diy.app.service.LectureService;
import com.diy.framework.web.beans.annotation.Autowired;
import com.diy.framework.web.beans.annotation.Component;
import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.controller.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LectureListController implements Controller {

    private final LectureService lectureService;

    @Autowired
    public LectureListController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        ModelAndView mv = new ModelAndView("lecture-list");

        mv.addObject("lectures", lectureService.getLectures());

        return mv;
    }
}
