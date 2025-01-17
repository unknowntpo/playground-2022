package org.example.dtos;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
public class StudentDTO implements Serializable {
    private Integer id;
    private String name;

    public StudentDTO(String name) {
        this.name = name;
    }
}
