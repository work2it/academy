package com.example.academy.student.dto.response;

import com.example.academy.student.entity.StudentEntity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class StudentResponse {
    protected Long id;
    protected String firsName;
    protected String lastName;
    protected String email;

    public static StudentResponse of(StudentEntity entity){
        return StudentResponse.builder()
                .id(entity.getId())
                .firsName(entity.getFirsName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .build();
    }
}
