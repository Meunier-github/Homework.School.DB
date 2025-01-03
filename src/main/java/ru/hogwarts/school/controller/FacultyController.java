package ru.hogwarts.school.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections;

@RestController()
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFacultyInfo(@PathVariable Long id) {
        Faculty faculty = facultyService.findFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty foundFaculty = facultyService.editFaculty(faculty);
        if (foundFaculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> findFaculties(@RequestParam(required = false) String color,
                                                             @RequestParam(required = false) String name) {
        if (color != null && !color.isBlank()) {
            return ResponseEntity.ok(facultyService.findFacultyByColorIgnoreCase(color));
        }
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(facultyService.findFacultyByNameIgnoreCase(name));
        }
        return ResponseEntity.ok(facultyService.getAll());
    }
    @GetMapping ("/getAllStudentOfFaculty")
    public ResponseEntity<Collection<Student>> getAllStudentOfFaculty(@RequestParam long id) {
        Collection<Student> studentsOfFaculty = facultyService.getStudentByFaculty(id);
        if (studentsOfFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentsOfFaculty);

    }
    @GetMapping("theLongestName")
    public String getTheLongestNameOfFaculty() {
        return facultyService.getTheLongestNameOfFaculty();
    }

}
