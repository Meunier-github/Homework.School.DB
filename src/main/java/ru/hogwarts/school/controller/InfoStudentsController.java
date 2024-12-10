package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.model.AverageAgeOfStudents;
import ru.hogwarts.school.model.CountOfStudents;
import ru.hogwarts.school.model.FiveLastStudents;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
public class InfoStudentsController {
    private final StudentService studentService;

    public InfoStudentsController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/countOfStudents")
    public CountOfStudents getCountOfStudents() {
        return studentService.getCountOfStudents();
    }

    @GetMapping("/averageAgeOfStudents")
    public AverageAgeOfStudents getAverageAgeOfStudents() {
        return studentService.getAverageAgeOfStudents();
    }

    @GetMapping("/fiveLastStudents")
    public Collection<FiveLastStudents> getFiveLastStudents() {
        return studentService.getFiveLastStudents();
    }
}
