package com.example.genericRowMapper;

import com.example.genericRowMapper.testclasses.Employee;
import com.example.genericRowMapper.testclasses.Employee2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/test1")
    public ResponseEntity<List<Employee>> testGet(){
        List<Employee> employees = namedParameterJdbcTemplate.query("SELECT * FROM employee", new NestedRowMapper(Employee.class));

        return ResponseEntity.ok().body(employees);
    }

    @GetMapping("/test2")
    public ResponseEntity<List<Employee>> testGet2(){
        List<Employee> employees = namedParameterJdbcTemplate.query("SELECT * FROM employee", new NestedRowMapper(Employee2.class));

        return ResponseEntity.ok().body(employees);
    }
}
