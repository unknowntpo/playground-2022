package org.example.services;

import org.example.repositories.StudentRepositories;
import org.example.entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    @Autowired
    private StudentRepositories studentRepositories;
    public Student select(Integer studentId) {
        return studentRepositories.getById(studentId);
    }
}
