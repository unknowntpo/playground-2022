package org.example.controllers;

import org.example.dtos.StudentDTO;
import org.example.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/{studentId}")
    public StudentDTO getById(@PathVariable Integer studentId) {
        return studentService.getById(studentId);
    }
}
