package org.example.controllers;

import org.example.dtos.StudentDTO;
import org.example.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentDTO> getById(@PathVariable Integer studentId) {
        StudentDTO studentDto = studentService.getById(studentId);
       return ResponseEntity.ok(studentDto);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<StudentDTO> create(@RequestBody StudentDTO body) {
        // FIXME: validation
        StudentDTO savedStudent = studentService.save(body);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedStudent.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedStudent);
    }
}
