package com.nbp.currencyapp.controller;

import com.nbp.currencyapp.dao.ExchangeDAO;
import com.nbp.currencyapp.service.ExchangeService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/convert")
public class ConvertController {

    ExchangeService exchangeService;

    public ConvertController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping()
    public ExchangeDAO exchangeGet(
            @RequestParam(name = "amount") BigDecimal amount,
            @RequestParam(name = "baseCurrency") String baseCurrency,
            @RequestParam(name = "targetCurrency") String targetCurrency) {
        return exchangeService.convert(amount, baseCurrency, targetCurrency);
    }
}
