package com.moinchallenge.service;

import com.moinchallenge.constant.Currency;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExchangeRateService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Currency BASE_CURRENCY = Currency.KRW;

    public double getExchangeRate(Currency targetCurrency) {
        String code = "FRX." + BASE_CURRENCY + targetCurrency.getCode();
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
}
