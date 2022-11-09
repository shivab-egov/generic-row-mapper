package com.example.genericRowMapper.testclasses;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Employee2 {
    int id;
    String phonenumber;
    String name;
    Currency curr;
}
