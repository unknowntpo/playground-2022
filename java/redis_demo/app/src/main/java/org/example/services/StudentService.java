package org.example.services;

import org.example.repositories.StudentRepository;
import org.example.entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    public Student select(Integer studentId) {
        return studentRepository.getById(studentId);
    }
}
