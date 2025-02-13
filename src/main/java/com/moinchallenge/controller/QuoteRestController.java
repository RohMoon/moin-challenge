package com.moinchallenge.controller;

import com.moinchallenge.dto.request.QuoteRequest;
import com.moinchallenge.dto.response.BaseResponse;
import com.moinchallenge.dto.response.QuoteResponse;
import com.moinchallenge.dto.response.QuoteWrapperResponse;
import com.moinchallenge.service.QuoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("transfer")
public class QuoteRestController {

    private final QuoteService quoteService;

    @Operation(
            summary = "송금 견적서를 갖고 오는 API",
            security = @SecurityRequirement(name = "bearer-key")
    )
    @PostMapping("/quote")
    public ResponseEntity<QuoteWrapperResponse> getQuote(@RequestBody @Valid QuoteRequest request) {
        QuoteResponse quoteResponse = quoteService.calculateQuote(request);
        return ResponseEntity.ok()
                .body(QuoteWrapperResponse.of(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), quoteResponse));

    }
}