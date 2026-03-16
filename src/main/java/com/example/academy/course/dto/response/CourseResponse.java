package com.example.academy.course.dto.response;

import com.example.academy.course.entity.CourseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CourseResponse {
    private Long id;
    private String title;
    private String description;

    public static CourseResponse of(CourseEntity entity) {
        return CourseResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .build();
    }
}
