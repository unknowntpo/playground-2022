package org.example.services;

import org.example.dtos.StudentDTO;
import org.example.mappers.StudentMapper;
import org.example.repositories.jpa.StudentRepository;
import org.example.entities.Student;
import org.example.repositories.redis.StudentRedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private StudentRedisRepository studentRedisRepository;

    @Autowired
    private StudentMapper studentMapper;

    private static final Logger logger
            = LoggerFactory.getLogger(StudentService.class);

    public StudentDTO getById(Integer studentId) {
        logger.info("getting student by id: {}, toString: {}", studentId, studentId.toString());
        if (studentRedisRepository.existsById(studentId)) {
            Student student = studentRedisRepository.getStudentById(studentId);
            logger.info("got student in redis: {}", student);
            StudentDTO dto = studentMapper.toDto(student);

            logger.info("got studentDto: {}", dto);

            return dto;
        }

        Student student = studentRepository.getById(studentId);
        studentRedisRepository.save(student);

        var dto = studentMapper.toDto(student);

        logger.info("got studentDto: {}", dto);

        return dto;
//        return studentMapper.toDto(student);
    }
}
