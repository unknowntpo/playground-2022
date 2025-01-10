package org.example.services;

import org.example.dtos.StudentDTO;
import org.example.entities.Student;
import org.example.mappers.StudentMapper;
import org.example.repositories.jpa.StudentRepository;
import org.example.repositories.redis.StudentRedisRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentRedisRepository studentRedisRepository;

    /**
     * Should use Mappers.getMapper to get the right mapper
     * Ref: https://www.reddit.com/r/javahelp/comments/nfcm9y/comment/gykyq3p/?utm_source=share&utm_medium=web3x&utm_name=web3xcss&utm_term=1&utm_content=share_button
     */
    @Spy
    private StudentMapper studentMapper = Mappers.getMapper(StudentMapper.class);

    @InjectMocks
    private StudentService studentService;

    @Test
    void testGetById() {
        Integer studentId = 100;
        String name = "mock name";
        Student mockStudent = new Student();
        mockStudent.setId(studentId);
        mockStudent.setName(name);
        when(studentRepository.getById(studentId)).thenReturn(mockStudent);
        when(studentRedisRepository.save(mockStudent)).thenReturn(mockStudent);

        StudentDTO mockStudentDto =  new StudentDTO(name);
        mockStudentDto.setId(studentId);

        assertEquals(mockStudentDto, studentService.getById(studentId));
    }


}
