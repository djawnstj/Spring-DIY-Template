package com.diy.app.service;

import com.diy.app.repository.LectureRepository;
import com.diy.framework.web.beans.annotation.Autowired;
import com.diy.framework.web.beans.annotation.Component;

import java.util.List;

@Component
public class LectureService {

    private final LectureRepository lectureRepository;

    @Autowired
    public LectureService(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public void createLecture(String lecture) {
        lectureRepository.save(lecture);
    }

    public List<String> getLectures() {
        return lectureRepository.findAll();
    }
}
