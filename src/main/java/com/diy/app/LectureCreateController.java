package com.diy.app;

import com.diy.app.service.LectureService;
import com.diy.framework.web.beans.annotation.Autowired;
import com.diy.framework.web.beans.annotation.Component;
import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.controller.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LectureCreateController implements Controller {

    private final LectureService lectureService;

    @Autowired
    public LectureCreateController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // todo: JSON 파싱 해야함 (getParameter 불가)
        String name = req.getParameter("name");
        String priceStr = req.getParameter("price");

        if (name != null && priceStr != null) {
            String lecture = name + " - " + priceStr;
            lectureService.createLecture(lecture);
        }

        return new ModelAndView("redirect:/lectures");
    }
}
