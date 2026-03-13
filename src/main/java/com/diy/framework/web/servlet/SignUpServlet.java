package com.diy.framework.web.servlet;

import com.diy.app.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@WebServlet("/sign-up")
public class SignUpServlet extends HttpServlet {

    private final Map<Long, User> userRepository = new HashMap<>();

    @Override
    public void init(final ServletConfig config) throws ServletException {
        System.out.println("user servlet init");
        super.init(config);
    }

    @Override
    protected void doPost(HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String pw = request.getParameter("pw");

        //아이디 중복 검사

        //없으면 id, pw로 유저 생성

    }



    private static User extractUser(HttpServletRequest request) throws IOException {
        byte[] bodyBytes = request.getInputStream().readAllBytes();
        String body = new String(bodyBytes, StandardCharsets.UTF_8);
        return new ObjectMapper().readValue(body, User.class);
    }
}
