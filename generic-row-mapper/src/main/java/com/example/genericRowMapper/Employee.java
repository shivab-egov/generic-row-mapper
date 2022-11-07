package com.example.genericRowMapper;

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
    Amount amount;
}
