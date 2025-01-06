package org.example.repositories.jpa;

import org.example.dtos.StudentDTO;
import org.example.entities.Student;
import org.example.mappers.StudentMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class StudentRepositoryTest {
    @Autowired
    private StudentRepository repo;

    @Autowired
    private StudentMapper studentMapper;

    @Nested
    class GetByIdTests {
        @Test
        void shouldReturnStudentWhenFound() {
            String name = "Eric";
            StudentDTO studentDto = new StudentDTO(name);
            Student student = studentMapper.toEntity(studentDto);
            assertNotNull(student.getName());

            student = repo.save(student);
            assertTrue(student.getId() > 0);
            assertEquals(name, student.getName());
        }

//        @Test
//        void shouldThrowExceptionWhenNotFound() {
//            when(studentRepository.findById(2L)).thenReturn(Optional.empty());
//            assertThrows(EntityNotFoundException.class, () -> studentService.getById(2L));
//        }
    }
}
