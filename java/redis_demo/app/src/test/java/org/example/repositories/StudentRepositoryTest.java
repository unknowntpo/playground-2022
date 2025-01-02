package org.example.repositories;

import jakarta.annotation.Resource;
import org.example.entities.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class StudentRepositoryTest {
    @Autowired
    private StudentRepository repo;

//    @Autowired
    // can not use @Autowired because we have generic
    // https://blog.csdn.net/weixin_63577740/article/details/133843898
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    public void setup() {
        try {
            redisTemplate.getConnectionFactory().getConnection().flushDb();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nested
    class GetByIdTests {
        @Test
        void shouldReturnStudentWhenFound() {
            String name = "Eric";
            Student student = repo.createStudent(name);
            assertTrue(student.getId() > 0);
            assertEquals(name, student.getName());

            // cache should be set
            String redisKey = "student:" + student.getId();
            assertTrue(redisTemplate.hasKey(redisKey));

            assertEquals(student, redisTemplate.opsForValue().get(redisKey));
        }

//        @Test
//        void shouldThrowExceptionWhenNotFound() {
//            when(studentRepository.findById(2L)).thenReturn(Optional.empty());
//            assertThrows(EntityNotFoundException.class, () -> studentService.getById(2L));
//        }
    }
}
