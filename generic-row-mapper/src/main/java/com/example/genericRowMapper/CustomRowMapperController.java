package com.example.genericRowMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/crc/v1")
public class CustomRowMapperController {

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    CustomRowMapperController(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @GetMapping()
    public ResponseEntity<List<Employee>> testGet(){
        BeanPropertyRowMapper rowMapper = BeanPropertyRowMapper.newInstance(Employee.class);
        List<Employee> employees = namedParameterJdbcTemplate.query("SELECT * FROM employee", new NestedRowMapper(Employee.class));
        return ResponseEntity.ok().body(employees);
    }
}
