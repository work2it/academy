package com.example.academy.course.controller;

import com.example.academy.course.dto.request.CourseRequest;
import com.example.academy.course.dto.response.CourseFullResponse;
import com.example.academy.course.dto.response.CourseResponse;
import com.example.academy.course.entity.CourseEntity;
import com.example.academy.course.exception.CourseNotFoundException;
import com.example.academy.course.repository.CourseRepository;
import com.example.academy.course.routes.CourseRoutes;
import com.example.academy.lesson.entity.LessonEntity;
import com.example.academy.lesson.repository.LessonRepository;
import com.example.academy.student.dto.response.StudentResponse;
import com.example.academy.student.entity.StudentEntity;
import com.example.academy.student.repository.StudentRepository;
import com.example.academy.student.routes.UserRoutes;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class CourseApiController {

    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    @PostMapping(CourseRoutes.CREATE)
    @Operation(summary = "Создание нового курса")
    public CourseResponse create(@RequestBody CourseRequest request) {
        CourseEntity course = CourseEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .build();
        course = courseRepository.save(course);
        return CourseResponse.of(course);
    }

    @PutMapping(CourseRoutes.BY_ID)
    @Operation(summary = "Обновление курса")
    public CourseFullResponse update(@PathVariable Long id, @RequestBody CourseRequest request) throws CourseNotFoundException {
        Optional<CourseEntity> courseEntityOptional = courseRepository.findById(id);
        if (courseEntityOptional.isEmpty()) throw new CourseNotFoundException();
        CourseEntity course = courseEntityOptional.get();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course = courseRepository.save(course);
        List<LessonEntity> lessons = lessonRepository.findByCourseId(course.getId());
        return CourseFullResponse.of(course, lessons);
    }

    @GetMapping(CourseRoutes.BY_ID)
    @Operation(summary = "Информация о курсе по Id")
    public CourseFullResponse byId(@PathVariable Long id) throws CourseNotFoundException {
        Optional<CourseEntity> courseEntityOptional = courseRepository.findById(id);
        if (courseEntityOptional.isEmpty()) throw new CourseNotFoundException();
        CourseEntity course = courseEntityOptional.get();
        List<LessonEntity> lessons = lessonRepository.findByCourseId(course.getId());
        return CourseFullResponse.of(course, lessons);
    }

    @DeleteMapping(CourseRoutes.BY_ID)
    @Operation(summary = "Удаление курса по Id")
    public String delete(@PathVariable Long id) throws CourseNotFoundException {
        Optional<CourseEntity> courseEntityOptional = courseRepository.findById(id);
        if (courseEntityOptional.isEmpty()) throw new CourseNotFoundException();
        courseRepository.delete(courseEntityOptional.get());
        return HttpStatus.OK.name();
    }

    @GetMapping(CourseRoutes.SEARCH)
    @Operation(summary = "Поиск курсов")
    public List<CourseResponse> search(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        ExampleMatcher ignoringExampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<CourseEntity> example = Example.of(
                CourseEntity.builder().title(query).description(query).build(),
                ignoringExampleMatcher);

        return courseRepository
                .findAll(example, pageable)
                .stream()
                .map(CourseResponse::of)
                .collect(Collectors.toList());
    }
}
