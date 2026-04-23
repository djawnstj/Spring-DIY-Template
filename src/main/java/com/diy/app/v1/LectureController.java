package com.diy.app.v1;

import com.diy.framework.web.beans.annotation.Component;
import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.annotation.RequestMapping;
import com.diy.framework.web.mvc.controller.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequestMapping("/v1/lectures")
public class LectureController implements Controller {

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String method = request.getMethod();

        if (method.equals("GET")) {
            return new ModelAndView("lecture-list");
        }

        System.out.println("POST 들어옴! (인터페이스 기반)");
        return new ModelAndView("redirect:/lectures");
    }
}
