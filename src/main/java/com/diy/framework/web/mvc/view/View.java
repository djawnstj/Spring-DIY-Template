package com.diy.framework.web.mvc.view;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface View {
    void render(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException;
}
