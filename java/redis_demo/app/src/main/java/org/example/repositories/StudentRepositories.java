package org.example.repositories;

import org.example.entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StudentRepositories {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Student getById(Integer id) {
        String sql = "SELECT id, name FROM student WHERE id = :studentId";

        Map<String, Integer> map = new HashMap<>();
        map.put("studentId", id);

        List<Student> rows = namedParameterJdbcTemplate.query(sql, map, new StudentRowMapper());

        return rows.isEmpty() ? rows.getFirst() : null;
    }
}
