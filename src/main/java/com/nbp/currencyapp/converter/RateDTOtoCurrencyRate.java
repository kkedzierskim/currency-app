package com.nbp.currencyapp.converter;


import com.nbp.currencyapp.domain.CurrencyRate;
import com.nbp.currencyapp.dto.RateDTO;
import com.sun.istack.Nullable;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RateDTOtoCurrencyRate implements Converter<RateDTO, CurrencyRate> {

    @Nullable
    @Synchronized
    @Override
    public CurrencyRate convert(RateDTO source) {
        if(source == null){
            return null;
        }

        final CurrencyRate currencyRate = new CurrencyRate();
        currencyRate.setCurrency(source.getCurrency());
        currencyRate.setExchange(source.getMid());
        currencyRate.setCode(source.getCode());

        return currencyRate;
    }
}

