package com.diy.framework.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Controller {
    void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException;
}
