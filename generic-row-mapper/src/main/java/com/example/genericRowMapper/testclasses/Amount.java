package com.example.genericRowMapper.testclasses;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Amount {
    int price;
    Currency currency;
}
