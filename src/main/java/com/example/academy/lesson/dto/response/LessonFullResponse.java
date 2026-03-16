package com.example.academy.lesson.dto.response;

import com.example.academy.course.dto.response.CourseResponse;
import com.example.academy.course.entity.CourseEntity;
import com.example.academy.lesson.entity.LessonEntity;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class LessonFullResponse {
    private Long id;
    private String title;
    private String description;
    private Long courseId;
    private CourseResponse course;

    public static LessonFullResponse of(LessonEntity entity, CourseEntity course) {
        return LessonFullResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .courseId(entity.getCourseId())
                .course(CourseResponse.of(course))
                .build();

    }
}
