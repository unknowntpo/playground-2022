package org.example.repositories.redis;

import jakarta.annotation.Resource;
import jakarta.annotation.Resources;
import org.example.dtos.StudentDTO;
import org.example.entities.Student;
import org.example.mappers.StudentMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

@SpringBootTest
class StudentRedisRepositoryTest {
    @Autowired
    private StudentRedisRepository repo;

    @Autowired
    private StudentMapper studentMapper;

    @Resource
    private RedisTemplate<Student, Integer> redisTemplate;

    @BeforeEach
    public void setup() {
        try {
            redisTemplate.getConnectionFactory()
                    .getConnection()
                    .flushDb();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nested
    class GetByIdTests {
        @Test
        void shouldReturnStudentWhenFound() {
            String name = "Eric";
            StudentDTO studentDto = new StudentDTO(name);
            Student student = studentMapper.toEntity(studentDto);
            Integer id = 1;
            student.setId(id);
            assertNotNull(student);
            assertEquals(id, student.getId());

            student = repo.save(student);
            assertTrue(student.getId() > 0);
            assertEquals(name, student.getName());
        }
    }
}
