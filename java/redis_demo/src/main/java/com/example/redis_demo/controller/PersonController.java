package com.example.redis_demo.controller;

import com.example.redis_demo.model.Person;
import com.example.redis_demo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @PostMapping
    public Person savePerson(@RequestBody Person person) {
        return personRepository.save(person);
    }

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable String id) {
        return personRepository.findById(id).orElse(null);
    }
}

