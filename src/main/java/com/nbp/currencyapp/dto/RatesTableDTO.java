package com.nbp.currencyapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RatesTableDTO implements Serializable {

    private String table;
    private String no;
    private String effectiveDate;
    private List<RateDTO> rates;

}