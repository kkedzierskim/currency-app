package com.nbp.currencyapp.repository;

import com.nbp.currencyapp.domain.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {

    Optional<CurrencyRate> findByCode(String code);

}
