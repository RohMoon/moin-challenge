package com.moinchallenge.service;

import com.moinchallenge.constant.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class ExchangeRateServiceTest {
    private ExchangeRateService exchangeRateService;
    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setup() {
        RestTemplate restTemplate = new RestTemplate();
        exchangeRateService = new ExchangeRateService();
        ReflectionTestUtils.setField(exchangeRateService, "restTemplate", restTemplate);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testGetExchangeRate_USD() {
        Currency targetCurrency = Currency.USD;
        String code = "FRX." + Currency.KRW + targetCurrency.getCode();
        String url = "https://crix-api-cdn.upbit.com/v1/forex/recent?codes=" + code;

        String fakeResponse = "[{\"code\":\"" + code + "\",\"currencyCode\":\"" + targetCurrency.getCode() + "\",\"basePrice\":1302.00,\"currencyUnit\":1}]";

        mockServer.expect(requestTo(url))
                .andRespond(withSuccess(fakeResponse, MediaType.APPLICATION_JSON));

        double rate = exchangeRateService.getExchangeRate(targetCurrency);
        assertEquals(1302.00, rate, 0.001);

        mockServer.verify();
    }
}