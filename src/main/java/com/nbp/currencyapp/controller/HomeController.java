package com.nbp.currencyapp.controller;


import com.nbp.currencyapp.domain.Currency;
import com.nbp.currencyapp.service.CurrenciesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {

    private final CurrenciesService currenciesService;

    public HomeController(CurrenciesService currenciesService) {
        this.currenciesService = currenciesService;
    }

    @GetMapping
    public List<Currency> getCurrencies(){
    return currenciesService.getCurrencies();
    }
}
