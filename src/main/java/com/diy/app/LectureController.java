package com.diy.app;

import com.diy.app.service.LectureService;
import com.diy.framework.web.beans.annotation.Autowired;
import com.diy.framework.web.beans.annotation.Component;
import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.annotation.GetMapping;
import com.diy.framework.web.mvc.annotation.PostMapping;

import java.util.List;

@Component
public class LectureController {

    private final LectureService lectureService;

    @Autowired
    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping("/lectures")
    public ModelAndView getLectures() {
        ModelAndView mv = new ModelAndView("lecture-list");
        mv.addObject("lectures", lectureService.getLectures());
        return mv;
    }

    @PostMapping("/lectures")
    public ModelAndView createLecture() {
        System.out.println("POST 들어옴!");
        return new ModelAndView("redirect:/lectures");
    }
}
