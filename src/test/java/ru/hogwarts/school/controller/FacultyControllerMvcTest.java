package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.*;

import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
class FacultyControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @SpyBean
    FacultyService facultyService;
    @MockBean
    StudentRepository studentRepository;
    @MockBean
    FacultyRepository facultyRepository;
    @InjectMocks
    private FacultyController facultyController;
    Student student = new Student();
    Faculty faculty = new Faculty();

    @Test
    void getFacultyInfo() throws Exception {
        setFaculty();
        when(facultyRepository.findById(anyLong())).thenReturn(Optional.of(faculty));
        mockMvc.perform(get("/faculty/" + nextInt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }

    @Test
    void createFaculty() throws Exception {
        String content = objectMapper.writeValueAsString(faculty);
        setFaculty();
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()))
        ;
    }

    @Test
    void editFaculty() throws Exception {
        String content = objectMapper.writeValueAsString(faculty);
        setFaculty();
        when(facultyRepository.existsById(anyLong())).thenReturn(true);
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(put("/faculty")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }

    @Test
    void deleteFaculty() throws Exception {
        when(facultyRepository.existsById(anyLong())).thenReturn(true);

        doNothing().when(facultyRepository).deleteById(anyLong());
        mockMvc.perform(delete("/faculty/" + nextInt()))
                .andExpect(status().isOk());
    }

    @Test
    void findFaculties() throws Exception {
        setFaculty();
        when(facultyRepository.findAll()).thenReturn(List.of(faculty));
        when(facultyRepository.findFacultyByColorIgnoreCase(anyString())).thenReturn(List.of(faculty));
        when(facultyRepository.findFacultyByNameIgnoreCase(anyString())).thenReturn(List.of(faculty));
        mockMvc.perform(get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].name").value(faculty.getName()))
                .andExpect(jsonPath("$[0].color").value(faculty.getColor()));
        mockMvc.perform(get("/faculty?color=" + nextInt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].name").value(faculty.getName()))
                .andExpect(jsonPath("$[0].color").value(faculty.getColor()));
        mockMvc.perform(get("/faculty?name=" + nextInt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].name").value(faculty.getName()))
                .andExpect(jsonPath("$[0].color").value(faculty.getColor()));

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