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
        ObjectMapper objectMapper = new ObjectMapper();
        Collection<Lecture> lectures = lectureList.values();
        String json = objectMapper.writeValueAsString(lectures);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json"); // 응답 형식 지정
        resp.setStatus(200);
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
