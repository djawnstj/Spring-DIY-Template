package com.diy.app.repository;

import com.diy.framework.web.beans.annotation.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LectureRepository {

    private final List<String> lectures = new ArrayList<>();

    public void save(String lecture) {
        lectures.add(lecture);
    }

    public List<String> findAll() {
        return lectures;
    }
}
