package org.example.controllers;

import org.example.entities.Student;
import org.example.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;

    public Student select(@PathVariable Integer studentId) {
        return studentService.select(studentId);
    }
}
