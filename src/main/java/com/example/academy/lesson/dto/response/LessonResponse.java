package com.example.academy.lesson.dto.response;

import com.example.academy.course.dto.response.CourseResponse;
import com.example.academy.lesson.entity.LessonEntity;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class LessonResponse {
    private Long id;
    private String title;
    private String description;
    private Long courseId;

    public static LessonResponse of(LessonEntity entity) {
        return LessonResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .courseId(entity.getCourseId())
                .build();

    }
}
