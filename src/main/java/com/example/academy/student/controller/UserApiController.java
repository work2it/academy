package com.example.academy.student.controller;

import com.example.academy.base.routes.BaseRoutes;
import com.example.academy.student.dto.request.EditStudentRequest;
import com.example.academy.student.dto.request.RegistrationRequest;
import com.example.academy.student.dto.response.StudentResponse;
import com.example.academy.student.entity.StudentEntity;
import com.example.academy.student.exception.BadRequestException;
import com.example.academy.student.exception.StudentAlredyExistException;
import com.example.academy.student.exception.StudentNotFoundException;
import com.example.academy.student.repository.StudentRepository;
import com.example.academy.student.routes.UserRoutes;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(UserRoutes.REG)
    @Operation(summary = "Тест")
    public String test() {
        return "OK";
    }

    @PostMapping(UserRoutes.REG)
    @Operation(summary = "Регистрация студента")
    public StudentResponse registration(@RequestBody RegistrationRequest request) throws BadRequestException, StudentAlredyExistException {

        Optional<StudentEntity> check =  studentRepository.findByEmail(request.getEmail());
        if (check.isPresent()) throw new StudentAlredyExistException();

        StudentEntity student = StudentEntity.builder()
                .firsName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        student = studentRepository.save(student);
        return StudentResponse.of(student);
    }

    @PutMapping(UserRoutes.ROOT)
    @Operation(summary = "Обновление студентом собственных данных")
    public StudentResponse update(Principal principal, @RequestBody EditStudentRequest request) throws StudentNotFoundException {
        StudentEntity student = studentRepository.findByEmail(principal.getName()).orElseThrow(StudentNotFoundException::new);

        student.setFirsName(request.getFirstName());
        student.setLastName(request.getLastName());

        student = studentRepository.save(student);
        return StudentResponse.of(student);
    }

    @GetMapping(UserRoutes.BY_ID)
    @Operation(summary = "Просмотр студента по Id")
    public StudentResponse byId(@PathVariable Long id) throws StudentNotFoundException {
        Optional<StudentEntity> studentEntityOptional = studentRepository.findById(id);
        if (studentEntityOptional.isEmpty()) throw new StudentNotFoundException();

        return StudentResponse.of(studentEntityOptional.get());
    }

    @GetMapping(UserRoutes.SEARCH)
    @Operation(summary = "Поиск студентов")
    public List<StudentResponse> search(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        ExampleMatcher ignoringExampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<StudentEntity> example = Example.of(
                StudentEntity.builder().lastName(query).firsName(query).build(),
                ignoringExampleMatcher);

        return studentRepository
                .findAll(example, pageable)
                .stream()
                .map(StudentResponse::of)
                .collect(Collectors.toList());
    }

}
