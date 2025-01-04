package org.example.services;

import org.example.dtos.StudentDTO;
import org.example.mappers.StudentMapper;
import org.example.repositories.jpa.StudentRepository;
import org.example.entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @see <a href="https://github.com/fahdarhalai/sample-spring-data-jpa-redis-caching/blob/90665bfa0e2d9b231a8c620fe31b3854096bca8f/src/main/java/dev/fahd/springredis/service/EmployeeService.java#L16">...</a>
 */
@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentMapper studentMapper;

    public StudentDTO getById(Integer studentId) {
        Student student = studentRepository.getById(studentId);
        return studentMapper.toDto(student);
    }
}
