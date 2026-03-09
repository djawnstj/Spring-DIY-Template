package com.diy.app.lecture;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 강의 삭제 서블릿
 *
 */
@WebServlet("/lectures/*")
public class LectureDeleteServlet extends HttpServlet {

    private final LectureService lectureService = new LectureService();

    /*
     * 강의 삭제하기
     * */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        long id = Long.parseLong(pathInfo.substring(1));

        lectureService.deleteLecture(id);

        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
