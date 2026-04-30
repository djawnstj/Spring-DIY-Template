package com.diy.app;

import com.diy.framework.context.Autowired;
import com.diy.framework.context.Component;

import java.util.List;

@Component
public class LectureService {
    private final LectureRepository lectureRepository;

    @Autowired
    public LectureService(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public List<Lecture> getLectures() {
        return lectureRepository.values();
    }

    public Lecture getLecture(Long id) {
        return lectureRepository.findById(id);
    }

    public void saveLecture(String title) {
        Long nextId = lectureRepository.nextId();
        lectureRepository.save(new Lecture(nextId, title));
    }

    public void updateLecture(Lecture lecture) {
        lectureRepository.save(lecture);
    }

    public void deleteLecture(Long id) {
        lectureRepository.delete(id);
    }
}
