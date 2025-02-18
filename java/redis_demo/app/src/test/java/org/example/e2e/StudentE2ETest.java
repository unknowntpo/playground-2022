package org.example.e2e;

import org.example.dtos.StudentDTO;
import org.example.entities.Student;
import org.example.repositories.jpa.StudentRepository;
import org.example.repositories.redis.StudentRedisRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    // Test methods will go here
    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        studentRepository.deleteAll();
    }

    @Test
    void shouldCreateAndRetrieveUser() {
        // Given
        StudentDTO studentDto = new StudentDTO("Eric");

        // When
        ResponseEntity<StudentDTO> createResponse = restTemplate.postForEntity("/students", studentDto, StudentDTO.class);

        // Then
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertNotNull(createResponse.getBody().getId());

        // Verify user is in the database
        Optional<Student> savedStudent = studentRepository.findById(createResponse.getBody().getId());
        assertNotNull(savedStudent);
        assertTrue(savedStudent.isPresent());
        assertEquals(studentDto.getName(), savedStudent.get().getName());

        // Retrieve the user via API
        ResponseEntity<StudentDTO> getResponse = restTemplate.getForEntity("/students/{id}", StudentDTO.class, createResponse.getBody().getId());

        // Then
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(studentDto.getName(), getResponse.getBody().getName());
    }

    @Test
    void shouldGetFromDbWhenStudentIsNotInCache() {
        // Given
        StudentDTO studentDto = new StudentDTO("Eric");

        // When
        ResponseEntity<StudentDTO> createResponse = restTemplate.postForEntity("/students", studentDto, StudentDTO.class);

        // Then
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertNotNull(createResponse.getBody().getId());

        // Verify user is in the database
        Optional<Student> savedStudentOpt = studentRepository.findById(createResponse.getBody().getId());
        assertTrue(savedStudentOpt.isPresent());
        Student savedStudent = savedStudentOpt.get();
        assertEquals(studentDto.getName(), savedStudent.getName());

        // Delete from Redis
        redisTemplate.opsForValue().getOperations().delete(savedStudent.getRedisKey());
        assertFalse(redisTemplate.hasKey(savedStudent.getRedisKey()));

        // Retrieve the Student via API
        ResponseEntity<StudentDTO> getResponse = restTemplate.getForEntity("/students/{id}", StudentDTO.class, createResponse.getBody().getId());

        Student studentFromRedis = studentRedisRepository.getById(savedStudent.getId());
        assertNotNull(studentFromRedis);

        // Then
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(studentDto.getName(), getResponse.getBody().getName());

        // Make sure student has been set to cache
        assertTrue(redisTemplate.hasKey(savedStudent.getRedisKey()));
    }
}
