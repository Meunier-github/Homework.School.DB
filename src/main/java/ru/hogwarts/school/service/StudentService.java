package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.*;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class StudentService {


    private final StudentRepository studentRepository;
    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        logger.info("Was invoked method  for find student");
        return studentRepository.findById(id).orElseThrow(() ->
                {
                    logger.error("There is not student with id = {}", id);
                    return new NullPointerException("Пустое значение!");
                }
        );

    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student");
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete student");
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAll() {
        logger.info("Was invoked method for get all students");
        return studentRepository.findAll();
    }

    public Collection<Student> findByAge(int age) {
        logger.info("Was invoked method for find student by age");
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findByAgeBetween(Integer min, Integer max) {
        logger.info("Was invoked method for find student by age between " + min + " and " + max);
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getFacultyOfStudent(long id) {
        logger.info("Was invoked method for find faculty by student");
        return studentRepository.getReferenceById(id).getFaculty();
    }


    public CountOfStudents getCountOfStudents() {
        logger.info("Was invoked method for get count of students");
        return studentRepository.getCountOfStudents();
    }

    public AverageAgeOfStudents getAverageAgeOfStudents() {
        logger.info("Was invoked method for get average age of students");
        return studentRepository.getAverageAgeOfStudents();
    }

    public Collection<FiveLastStudents> getFiveLastStudents() {
        logger.info("Was invoked method for get last five students");
        return studentRepository.getFiveLastStudents();
    }
    public List<String> getNamesOfStudentsByLetterA() {
        return studentRepository.findAll()
                .stream()
                .parallel()
                .filter(s -> s.getName().startsWith("A"))
                .map(s -> s.getName().toUpperCase())
                .sorted()
                .collect(Collectors.toList());
    }

    public double getAverageAgeOfStudentsStream() {
        return studentRepository.findAll()
                .stream()
                .parallel()
                .mapToInt(s -> s.getAge())
                .average()
                .orElse(0);
    }
}

