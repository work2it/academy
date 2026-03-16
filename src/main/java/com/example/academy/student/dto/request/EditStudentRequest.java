package com.example.academy.student.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EditStudentRequest {
    private String firstName;
    private String lastName;
}
