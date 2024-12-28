package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.Comparator;

@Service
public class FacultyService {
    private final Logger logger = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        logger.info("Was invoked method for add faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        logger.info("Was invoked method  for find faculty");
        return facultyRepository.findById(id).orElseThrow(() ->
                {
                    logger.error("There is not faculty with id = {}", id);
                    return new NullPointerException("Пустое значение!");
                }
        );
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method for edit faculty");
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id) {
        logger.info("Was invoked method for delete faculty");
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> findFacultyByColorIgnoreCase(String color) {
        logger.info("Was invoked method for find faculty by color");
        return facultyRepository.findFacultyByColorIgnoreCase(color);
    }

    public Collection<Faculty> findFacultyByNameIgnoreCase(String name) {
        logger.info("Was invoked method for find faculty by name");
        return facultyRepository.findFacultyByNameIgnoreCase(name);
    }

    public Collection<Faculty> getAll() {
        logger.info("Was invoked method for get all faculties");
        return facultyRepository.findAll();
    }

    public Collection<Student> getStudentByFaculty(Long Id) {
        logger.info("Was invoked method for get students by faculty");
        Faculty faculty = facultyRepository.findById(Id).orElseThrow();
        return faculty.getStudents();
    }
    public String getTheLongestNameOfFaculty() {
        return facultyRepository.findAll().stream()

                .map(s -> s.getName())
                .max(Comparator.comparingInt(String::length))
                .orElse(null);
    }
}
