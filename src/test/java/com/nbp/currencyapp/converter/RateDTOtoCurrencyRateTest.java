package com.nbp.currencyapp.converter;

import com.nbp.currencyapp.domain.CurrencyRate;
import com.nbp.currencyapp.dto.RateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RateDTOtoCurrencyRateTest {


    private RateDTOtoCurrencyRate converter;

    @BeforeEach
    void setUp() {
        converter = new RateDTOtoCurrencyRate();
    }

    @Test
    void convert() {
        //given
        RateDTO PLNrateDTO = new RateDTO("polski złoty", "PLN", BigDecimal.ONE);
        //when
        CurrencyRate PLNCurrencyRate = converter.convert(PLNrateDTO);
        //then
        assertNotNull(PLNCurrencyRate);
        assertEquals("PLN" ,PLNCurrencyRate.getCode());
        assertEquals("polski złoty" ,PLNCurrencyRate.getCurrency());
        assertEquals(BigDecimal.ONE ,PLNCurrencyRate.getExchange());
    }
}