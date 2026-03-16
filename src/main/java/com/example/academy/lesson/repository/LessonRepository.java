package com.example.academy.lesson.repository;

import com.example.academy.lesson.entity.LessonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<LessonEntity, Long> {
    List<LessonEntity> findByCourseId(Long courseId);
}
