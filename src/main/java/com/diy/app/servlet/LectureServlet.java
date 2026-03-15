package com.diy.app.servlet;

import com.diy.app.domain.Lecture;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

// 강의 목록 조회
//

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {

    private final Map<Long, Lecture> lectureRepository = new HashMap<>();

    @Override
    public void init(final ServletConfig config) throws ServletException {
        System.out.println("home servlet init");
        super.init(config);
    }

    @Override
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws ServletException, java.io.IOException {
        final Collection<Lecture> lectures = lectureRepository.values();

//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().println(new ObjectMapper().writeValueAsString(lectures));

        final RequestDispatcher dispatcher = request.getRequestDispatcher("/lecture-list.jsp");

        final Map<String, Object> model = new HashMap<>();
        request.setAttribute("lectures", lectures);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws ServletException, java.io.IOException {
        Lecture lecture = extractLecture(request);

        final long id = lectureRepository.size();
        lecture.setId(id);
        lectureRepository.put(id, lecture);

    }


    @Override
    protected void doPut(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws ServletException, java.io.IOException {
        Lecture lecture = extractLecture(request);

        if (lecture == null || lecture.getId() == null) {
            throw new IllegalArgumentException("lecture is null or seq is null");
        }

        Lecture target = lectureRepository.get(lecture.getId());
        if (target == null) {
            throw new NoSuchElementException("No such element");
        }

        lectureRepository.put(lecture.getId(), lecture);

    }

    @Override
    protected void doDelete(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws ServletException, java.io.IOException {
        Lecture lecture = extractLecture(request);
        if (lecture == null || lecture.getId() == null) {
            throw new IllegalArgumentException("lecture is null or seq is null");
        }

        Lecture target = lectureRepository.get(lecture.getId());
        if (target == null) {
            throw new NoSuchElementException("No such element");
        }

        lectureRepository.remove(lecture.getId());
    }

    private static Lecture extractLecture(HttpServletRequest request) throws IOException {
        byte[] bodyBytes = request.getInputStream().readAllBytes();
        String body = new String(bodyBytes, StandardCharsets.UTF_8);
        return new ObjectMapper().readValue(body, Lecture.class);
    }
}
