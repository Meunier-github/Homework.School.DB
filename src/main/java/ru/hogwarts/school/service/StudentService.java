package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.*;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public Integer getSumNumber() {
        int sum = 0;
        long startTime;
        long finishTime;
        final int LIMIT = 1_000_000;
        startTime = System.currentTimeMillis();
        sum = Stream.iterate(1, a -> a + 1)
                .limit(LIMIT)
                .reduce(0, (a, b) -> a + b);
        finishTime = System.currentTimeMillis();
        logger.info("Время выполнения алгоритма без paralle равно " + (finishTime - startTime) + " мс");

        sum=0;
        startTime = System.currentTimeMillis();
        sum = Stream.iterate(1, a -> a + 1)
                .limit(LIMIT)
                .parallel()
                .reduce(0, (a, b) -> a + b);
        finishTime = System.currentTimeMillis();
        logger.info("Время выполнения алгоритма с paralle равно " + (finishTime - startTime) + " мс");

        sum = 0;
        startTime = System.currentTimeMillis();
        for (int i = 0; i < LIMIT; i++) {
            sum += i;
        }
        finishTime = System.currentTimeMillis();
        logger.info("Время выполнения алгоритма с циклом for равно " + (finishTime - startTime) + " мс");

        return sum;
    }

    public void getPrintParallel() {
        List<Student> students = studentRepository.findAll();
        System.out.println(students.get(0).getName());
        System.out.println(students.get(1).getName());
        new Thread(() -> {
            System.out.println(students.get(2).getName());
            System.out.println(students.get(3).getName());
        }).start();
        new Thread(() -> {
            System.out.println(students.get(4).getName());
            System.out.println(students.get(5).getName());
        }).start();    }

    public void getPrintSynchronized() {

        List<Student> students = studentRepository.findAll();
        printNameOfStudent(students.get(0));
        printNameOfStudent(students.get(1));

        new Thread(() -> {
            printNameOfStudent(students.get(2));
            printNameOfStudent(students.get(3));
        }).start();
        new Thread(() -> {
            printNameOfStudent(students.get(4));
            printNameOfStudent(students.get(5));
        }).start();

    }

    private synchronized void printNameOfStudent(Student student) {
        System.out.println(student.getName());
    }
}

