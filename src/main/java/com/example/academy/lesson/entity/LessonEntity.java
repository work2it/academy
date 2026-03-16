package com.example.academy.lesson.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="lessons")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Long courseId;
}
