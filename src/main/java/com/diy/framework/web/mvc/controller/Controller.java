package com.diy.framework.web.mvc.controller;

import com.diy.framework.web.mvc.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Controller {
    ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException;
}
