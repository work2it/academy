package com.example.academy.course.dto.response;

import com.example.academy.course.entity.CourseEntity;
import com.example.academy.lesson.dto.response.LessonResponse;
import com.example.academy.lesson.entity.LessonEntity;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;

@Data
@Getter
@Setter
@Builder
public class CourseFullResponse {
    private Long id;
    private String title;
    private String description;
    List<LessonResponse> lessons = new ArrayList<>();

    public static CourseFullResponse of(CourseEntity entity, List<LessonEntity> lessonEntity) {
        return CourseFullResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .lessons(lessonEntity.stream().map(LessonResponse::of).toList())
                .build();
    }
}
