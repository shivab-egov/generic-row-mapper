package com.example.genericRowMapper.testclasses;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Employee {
    int id;
    String phonenumber;
    String name;
    Amount amount;
}
