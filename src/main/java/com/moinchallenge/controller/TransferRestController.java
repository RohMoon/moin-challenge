package com.moinchallenge.controller;

import com.moinchallenge.dto.request.QuoteRequest;
import com.moinchallenge.dto.request.TransferRequest;
import com.moinchallenge.dto.response.ApiResponse;
import com.moinchallenge.dto.response.DataResponse;
import com.moinchallenge.dto.response.QuoteResponse;
import com.moinchallenge.service.QuoteService;
import com.moinchallenge.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("transfer")
public class TransferRestController {

    private final TransferService transferService;

    @PostMapping("/request")
    public ResponseEntity<?> requestTransfer(@RequestBody @Valid TransferRequest request) {
        try {
            transferService.requestTransfer(request.getQuoteId());
            return ResponseEntity.ok(ApiResponse.of(200, "OK"));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.of(400, exception.getMessage()));
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.of(500,"알 수 없는 에러 입니다."));
        }
    }

}
