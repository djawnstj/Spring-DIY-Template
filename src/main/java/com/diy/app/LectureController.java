package com.diy.app;

import com.diy.framework.web.beans.annotation.Component;
import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.annotation.GetMapping;
import com.diy.framework.web.mvc.annotation.PostMapping;

@Component
public class LectureController {

    @GetMapping("/lectures")
    public ModelAndView getLectures() {
        return new ModelAndView("lecture-list");
    }

    @PostMapping("/lectures")
    public ModelAndView createLecture() {
        return new ModelAndView("redirect:/lectures");
    }
}
