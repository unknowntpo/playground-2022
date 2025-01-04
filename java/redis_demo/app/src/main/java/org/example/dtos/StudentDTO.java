package org.example.dtos;

import java.io.Serializable;

public class StudentDTO implements Serializable {
    private String name;

    public StudentDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
