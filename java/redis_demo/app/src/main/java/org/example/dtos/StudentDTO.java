package org.example.dtos;

import java.io.Serializable;

public class StudentDTO implements Serializable {
    private String name;

    public StudentDTO(String name) {
        this.name = name;
    }
}
