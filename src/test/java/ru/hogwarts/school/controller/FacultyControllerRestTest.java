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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FacultyControllerRestTest {
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
    void getFacultyInfo() {
        setFaculty();
        ResponseEntity response = this.restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/" + faculty.getId(),
                Faculty.class);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(faculty);
        facultyRepository.deleteById(faculty.getId());

    }

    @Test
    void createFaculty() {
        setFaculty();

        ResponseEntity response = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(faculty);
        facultyRepository.deleteById(faculty.getId());
    }

    @Test
    void editFaculty() {
        setFaculty();
        ResponseEntity response = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(faculty);
        facultyRepository.deleteById(faculty.getId());
    }

    @Test
    void deleteFaculty() {
        setFaculty();
        ResponseEntity response = this.restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/" + faculty.getId(),
                Faculty.class);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(faculty);

        restTemplate.delete("http://localhost:" + port + "/faculty/" + faculty.getId(), faculty);
    }

    @Test
    void findFaculties() {
        setFaculty();
        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<Collection<Faculty>>() {
                }
        );
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();

        response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty?name=" + faculty.getName(),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<Collection<Faculty>>() {
                }
        );
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty?color=" + faculty.getColor(),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<Collection<Faculty>>() {
                }
        );
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();

        facultyRepository.deleteById(faculty.getId());
    }

    @Test
    void getAllStudentOfFaculty() {
        setFaculty();
        student.setFaculty(faculty);
        setStudent();

        ResponseEntity<Collection<Faculty>> response =
                restTemplate.exchange(
                        "http://localhost:" + port + "/faculty/getAllStudentOfFaculty?id=" + faculty.getId(),
                        HttpMethod.GET,
                        new HttpEntity<>(new HttpHeaders()),
                        new ParameterizedTypeReference<Collection<Faculty>>() {
                        });

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        studentRepository.deleteById(student.getId());
        facultyRepository.deleteById(faculty.getId());
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