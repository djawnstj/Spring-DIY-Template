package com.diy.framework.web.mvc.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Controller {
    void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException;
}
