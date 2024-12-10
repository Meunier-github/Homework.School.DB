package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.AverageAgeOfStudents;
import ru.hogwarts.school.model.CountOfStudents;
import ru.hogwarts.school.model.FiveLastStudents;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findByAge(int age);

    Collection<Student> findByAgeBetween(Integer min, Integer max);

    @Query(value = "SELECT COUNT(*) as count_of_students FROM student", nativeQuery = true)
    CountOfStudents getCountOfStudents();

    @Query(value = "SELECT AVG(age) as average_age_of_students FROM student", nativeQuery = true)
    AverageAgeOfStudents getAverageAgeOfStudents();

    @Query(value = "SELECT id as idStudent,name as nameStudent,age as ageStudent FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    Collection<FiveLastStudents> getFiveLastStudents();

}
