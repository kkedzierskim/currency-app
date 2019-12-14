package com.nbp.currencyapp.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.nbp.currencyapp.domain.Currency;
import com.nbp.currencyapp.domain.NbpTableType;
import org.springframework.stereotype.Component;

@Component
public class CurrenciesDAO {

    private final String url = "http://api.nbp.pl/api/exchangerates/tables/__TABLE__/?format=json";

    public List<Currency> parse(NbpTableType type) {

        System.out.println("CurrenciesParser:parse() - Start");

        // create a result object
        List<Currency> currencies = new ArrayList<>();
        // create a factory
        JsonFactory jf = new JsonFactory();

        try {
            // get data stream from url
            JsonParser jp = jf.createJsonParser(new URL(url.replaceAll("__TABLE__", type.toString())));

            while (jp.nextToken() != JsonToken.END_OBJECT) {
                // actual fieldname
                String fieldname = jp.getCurrentName();

                if ("table".equals(fieldname)) {
                    jp.nextToken();
                    System.out.println("TABLE TYPE IS: " + jp.getText());
                } else if ("effectiveDate".equals(fieldname)) {
                    jp.nextToken();
                    System.out.println("DATE: " + jp.getText());
                }
                if ("rates".equals(fieldname)) {
                    // Start iteration by rates
                    jp.nextToken();
                    // Prepare currency object
                    Currency cur = new Currency();
                    // iterate
                    while (jp.nextToken() != JsonToken.END_ARRAY) {
                        // actual rate fieldname
                        fieldname = jp.getCurrentName();

                        if (jp.getCurrentToken() == JsonToken.START_OBJECT) {
                            // start rate object starts a new currency object
                            cur = new Currency();
                        } else if (jp.getCurrentToken() == JsonToken.END_OBJECT) {
                            // end rate object end a currency object - save it to results
                            currencies.add(cur);
                        } else {
                            // adding currency data
                            if ("currency".equals(fieldname)) {
                                jp.nextToken();
                                cur.setName(jp.getText());
                            } else if ("code".equals(fieldname)) {
                                jp.nextToken();
                                cur.setISOcode(jp.getText());
                            } else if ("mid".equals(fieldname)) {
                                jp.nextToken();
                                cur.setMid(BigDecimal.valueOf(jp.getDoubleValue()));
                            }
                        }
                    }
                }
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return currencies;
    }
}
