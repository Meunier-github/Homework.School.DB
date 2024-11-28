package ru.hogwarts.school.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@ActiveProfiles("test")
class StudentControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @SpyBean
    StudentService studentService;
    @MockBean
    StudentRepository studentRepository;
    @InjectMocks
    private StudentController studentController;
    @MockBean
    FacultyRepository facultyRepository;
    Student student = new Student();
    Faculty faculty = new Faculty();

    @Test
    void getStudentInfo() throws Exception {
        setStudent();
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        mockMvc.perform(get("/student/" + nextInt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()));
    }

    @Test
    void getStudents() throws Exception {
        setStudent();
        when(studentRepository.findAll()).thenReturn(List.of(student));
        mockMvc.perform(get("/student/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].name").value(student.getName()))
                .andExpect(jsonPath("$[0].age").value(student.getAge()));
    }

    @Test
    void createStudent() throws Exception {

        String content = objectMapper.writeValueAsString(student);
        setStudent();
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()))
        ;
    }

    @Test
    void editStudent() throws Exception {
        String content = objectMapper.writeValueAsString(student);
        setStudent();
        when(studentRepository.existsById(anyLong())).thenReturn(true);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(put("/student")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()));

    }

    @Test
    void deleteStudent() throws Exception {
        when(studentRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(studentRepository).deleteById(anyLong());
        mockMvc.perform(delete("/student/" + nextInt()))
                .andExpect(status().isOk());
    }

    @Test
    void findFacultyOfStudent() throws Exception {
        setFaculty();
        student.setFaculty(faculty);
        setStudent();

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        when(studentRepository.getReferenceById(anyLong())).thenReturn(student);
        when(facultyRepository.findById(anyLong())).thenReturn(Optional.of(faculty));

        mockMvc.perform(get("/student/facultyOfStudent?id=" + nextInt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }

    @Test
    void testGetStudents() throws Exception {
        setStudent();
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));

        mockMvc.perform(get("/student?min=" + nextInt() + "&max=" + nextInt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
        mockMvc.perform(get("/student?age=" + nextInt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    void setStudent() {
        student.setId(1L);
        student.setName("Ivan");
        student.setAge(20);
    }

    void setFaculty() {
        faculty.setName("1");
        faculty.setColor("red");
        faculty.setId(1L);
    }

}