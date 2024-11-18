package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        return facultyRepository.findById(id).orElseThrow(() -> new NullPointerException("Пустое значение!"));
    }

    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id) {
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> findFacultyByColorIgnoreCase(String color) {
        return facultyRepository.findFacultyByColorIgnoreCase(color);
    }

    public Collection<Faculty> findFacultyByNameIgnoreCase(String name) {
        return facultyRepository.findFacultyByNameIgnoreCase(name);
    }

    public Collection<Faculty> getAll() {
        return facultyRepository.findAll();
    }

    public Collection<Student> getStudentByFaculty(Long Id) {
        Faculty faculty = facultyRepository.findById(Id).orElseThrow();
        return faculty.getStudents();
    }
}
