package com.moinchallenge.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExchangeRateService {

    private final RestTemplate restTemplate = new RestTemplate();

    public double getExchangeRate(String baseCurrency, String targetCurrency) {
        String code = "FRX." + baseCurrency + targetCurrency;

        String url = "https://crix-api-cdn.upbit.com/v1/forex/recent?codes=" + code;

        try {
            String response = restTemplate.getForObject(url, String.class);

            JSONArray arr = new JSONArray(response);
            JSONObject obj = arr.getJSONObject(0);
            double basePrice = obj.getDouble("basePrice");
            int currencyUnit = obj.getInt("currencyUnit");

            return basePrice / currencyUnit;
        } catch (Exception exception) {
            throw new RuntimeException("환율 조회 실패 : " + exception.getMessage(), exception);
        }
    }

    public int getFractionDigits(String currency) {
        try {
            return java.util.Currency.getInstance(currency).getDefaultFractionDigits();
        } catch (Exception exception) {
            return  2;
        }
    }
}
