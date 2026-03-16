package com.example.academy.lesson.controller;

import com.example.academy.course.dto.request.CourseRequest;
import com.example.academy.course.dto.response.CourseResponse;
import com.example.academy.course.entity.CourseEntity;
import com.example.academy.course.exception.CourseNotFoundException;
import com.example.academy.course.repository.CourseRepository;
import com.example.academy.course.routes.CourseRoutes;
import com.example.academy.lesson.dto.request.LessonRequest;
import com.example.academy.lesson.dto.response.LessonFullResponse;
import com.example.academy.lesson.dto.response.LessonResponse;
import com.example.academy.lesson.entity.LessonEntity;
import com.example.academy.lesson.exception.LessonNotFoundExcetpion;
import com.example.academy.lesson.repository.LessonRepository;
import com.example.academy.lesson.routes.LessonRoutes;
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
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class LessonApiController {

    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(LessonRoutes.CREATE)
    @Operation(summary = "Создание нового урока")
    public LessonFullResponse create(@RequestBody LessonRequest request) throws CourseNotFoundException {
        CourseEntity course = courseRepository.findById(request.getCourseId()).orElseThrow(CourseNotFoundException::new);
        LessonEntity lesson = LessonEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .courseId(request.getCourseId())
                .build();
        lesson = lessonRepository.save(lesson);
        return LessonFullResponse.of(lesson, course);
    }

    @PutMapping(LessonRoutes.BY_ID)
    @Operation(summary = "Обновление урока по Id")
    public LessonFullResponse update(@PathVariable Long id,  @RequestBody LessonRequest request) throws CourseNotFoundException, LessonNotFoundExcetpion {
        CourseEntity course = courseRepository.findById(request.getCourseId()).orElseThrow(CourseNotFoundException::new);
        LessonEntity lesson = lessonRepository.findById(id).orElseThrow(LessonNotFoundExcetpion::new);
        lesson.setTitle(request.getTitle());
        lesson.setDescription(request.getDescription());
        lesson.setCourseId(request.getCourseId());
        lesson = lessonRepository.save(lesson);
        return LessonFullResponse.of(lesson, course);
    }

    @GetMapping(LessonRoutes.BY_ID)
    @Operation(summary = "Информация об уроке по Id")
    public LessonFullResponse byId(@PathVariable Long id) throws CourseNotFoundException, LessonNotFoundExcetpion {
        LessonEntity lesson = lessonRepository.findById(id).orElseThrow(LessonNotFoundExcetpion::new);
        CourseEntity course = courseRepository.findById(lesson.getCourseId()).orElseThrow(CourseNotFoundException::new);
        return LessonFullResponse.of(lesson, course);
    }

    @DeleteMapping(LessonRoutes.BY_ID)
    @Operation(summary = "Удаление урока по Id")
    public String delete(@PathVariable Long id) {
        lessonRepository.deleteById(id);
        return HttpStatus.OK.name();
    }

    @GetMapping(LessonRoutes.SEARCH)
    @Operation(summary = "Поиск урока")
    public List<LessonResponse> search(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        ExampleMatcher ignoringExampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<LessonEntity> example = Example.of(
                LessonEntity.builder().title(query).description(query).build(),
                ignoringExampleMatcher);

        return lessonRepository
                .findAll(example, pageable)
                .stream()
                .map(LessonResponse::of)
                .collect(Collectors.toList());
    }
}
