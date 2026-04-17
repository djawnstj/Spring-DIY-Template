package com.diy.app;

import com.diy.framework.web.beans.annotation.Component;
import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.annotation.GetMapping;
import com.diy.framework.web.mvc.annotation.PostMapping;

import java.util.List;

@Component
public class LectureController {

    @GetMapping("/lectures")
    public ModelAndView getLectures() {
        ModelAndView mv = new ModelAndView("lecture-list");
        mv.addObject("lectures", List.of());
        return mv;
    }

    @PostMapping("/lectures")
    public ModelAndView createLecture() {
        System.out.println("POST 들어옴!");
        return new ModelAndView("redirect:/lectures");
    }
}
