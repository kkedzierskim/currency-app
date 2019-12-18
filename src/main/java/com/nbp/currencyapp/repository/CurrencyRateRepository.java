package com.nbp.currencyapp.repository;

import com.nbp.currencyapp.domain.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {

    Optional<CurrencyRate> findByCode(String code);

    @Transactional
    @Modifying
    @Query("Update CurrencyRate c SET c.exchange=:exchange WHERE c.code=:code")
    void updateExchange(@Param("code") String code, @Param("exchange") BigDecimal exchange);

}
