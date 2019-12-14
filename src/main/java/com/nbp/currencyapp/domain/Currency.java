package com.nbp.currencyapp.domain;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Currency {

    private String ISOcode;
    private String name;
    private BigDecimal mid;
}
