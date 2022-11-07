package com.example.genericRowMapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.RowMapper;

import java.sql.SQLException;

@SpringBootApplication
public class RowMapperDemoApplication {

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(RowMapperDemoApplication.class, args);

		RowMapper rowMapper = new GenericRowMapper(Employee.class);
		rowMapper.mapRow(null, 0);
	}

}
