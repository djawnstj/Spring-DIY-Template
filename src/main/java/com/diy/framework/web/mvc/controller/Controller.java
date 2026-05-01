package com.diy.framework.web.mvc.controller;

import com.diy.framework.web.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
