package com.nbp.currencyapp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String currency;
    private String code;

    @Column(precision = 10, scale = 4)
    private BigDecimal exchange;

    public CurrencyRate(String currency, String code, BigDecimal exchange) {
        this.currency = currency;
        this.code = code;
        this.exchange = exchange;
    }
}
