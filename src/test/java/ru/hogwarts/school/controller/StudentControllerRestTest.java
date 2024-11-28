package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class StudentControllerRestTest {
    @LocalServerPort
    private int port;
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    FacultyRepository facultyRepository;
    Student student = new Student();
    Faculty faculty = new Faculty();

    @Test
    void getStudentInfo() {
        setStudent();
        ResponseEntity response = this.restTemplate.getForEntity(
                "http://localhost:" + port + "/student/"+student.getId(),
                Student.class);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(student);
        studentRepository.deleteById(student.getId());

    }

    @Test
    void getStudents() {
        setStudent();
        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                "http://localhost:" + port + "/student/all",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<Collection<Student>>() {
                }
        );
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        studentRepository.deleteById(student.getId());
    }

    @Test
    void createStudent() {
        setStudent();
        ResponseEntity response = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/student", student, Student.class);
        Assertions.assertThat(response).isNotNull();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(student);
        studentRepository.deleteById(student.getId());
    }

    @Test
    void editStudent() {
        setStudent();
        ResponseEntity response = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/student", student, Student.class);
        Assertions.assertThat(response).isNotNull();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(student);
        studentRepository.deleteById(student.getId());
    }

    @Test
    void deleteStudent() {
        setStudent();
        ResponseEntity response = this.restTemplate.getForEntity(
                "http://localhost:" + port + "/student/"+student.getId(),
                Student.class);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(student);

        restTemplate.delete("http://localhost:" + port + "/student/"+student.getId(),student);
    }

    @Test
    void findFacultyOfStudent() {
        setFaculty();
        student.setFaculty(faculty);
        setStudent();

        ResponseEntity response = this.restTemplate.getForEntity(
                "http://localhost:" + port + "/student/facultyOfStudent?id="+student.getId(),
                Student.class,Faculty.class);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        studentRepository.deleteById(student.getId());
        facultyRepository.deleteById(faculty.getId());
    }

    @Test
    void testGetStudents() {
        setStudent();
        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                "http://localhost:" + port + "/student?min=10&max=30",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<Collection<Student>>() {
                }
        );
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        response = restTemplate.exchange(
                "http://localhost:" + port + "/student?age=20",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<Collection<Student>>() {
                }
        );
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        studentRepository.deleteById(student.getId());
    }

    void setStudent() {
        student.setName("Ivan");
        student.setAge(20);
        studentRepository.save(student);
    }
    void setFaculty() {
        faculty.setName("1");
        faculty.setColor("red");
        facultyRepository.save(faculty);
    }

}