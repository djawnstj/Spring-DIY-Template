package com.diy.app;

import com.diy.app.entity.Lecture;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LectureServlet extends HttpServlet {

    private final Map<Long, Lecture> lectureList = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private long nextId = 2L;

    @Override
    public void init() {
        // TODO: 서블릿이 재시작되면 데이터가 날라가므로, 별도의 저장소 클래스(레포지토리 등)을 두기
        lectureList.put(1L, new Lecture("스프링 기초", "김뿡뿡", "프로그래밍",
                200000));
        lectureList.put(2L, new Lecture("스프링 심화", "김뿡뿡", "프로그래밍",
                300000));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<Lecture> lectures = lectureList.values();
        setCommonResponseSettings(resp, lectures, 200);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Lecture lecture = objectMapper.readValue(req.getReader(), Lecture.class);
        lectureList.put(++nextId, lecture);
        setCommonResponseSettings(resp, lecture, 201);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        Long id = Long.parseLong(pathInfo.substring(1));
        Lecture lecture = objectMapper.readValue(req.getReader(), Lecture.class);
        lectureList.put(id, lecture);
        setCommonResponseSettings(resp, lectureList.get(id), 200);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        Long id = Long.parseLong(pathInfo.substring(1));
        lectureList.remove(id);
        setCommonResponseSettings(resp, null, 200);
    }

    private void setCommonResponseSettings(HttpServletResponse resp, Object data, int statusCode) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setStatus(statusCode);
        String json = objectMapper.writeValueAsString(data);
        resp.getWriter().write(json);
    }
}
