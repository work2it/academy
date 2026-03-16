package com.example.academy.lesson.dto.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class LessonRequest {
    private String title;
    private String description;
    private Long courseId;
}
