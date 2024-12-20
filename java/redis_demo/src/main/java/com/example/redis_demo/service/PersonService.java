package com.example.redis_demo.service;

import com.example.redis_demo.model.Person;
import com.example.redis_demo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    public Person getPerson(String id) {
        return personRepository.findById(id).orElse(null);
    }
}

