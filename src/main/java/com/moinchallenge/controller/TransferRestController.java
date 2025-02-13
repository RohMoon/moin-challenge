package com.moinchallenge.controller;

import com.moinchallenge.dto.request.TransferRequest;
import com.moinchallenge.dto.response.BaseResponse;
import com.moinchallenge.dto.response.TransferListResponse;
import com.moinchallenge.dto.response.TransferListWrapperResponse;
import com.moinchallenge.service.TransferHistoryService;
import com.moinchallenge.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
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
    private final TransferHistoryService transferHistoryService;

    @Operation(
            summary = "송금 접수 요청 API",
            security = @SecurityRequirement(name = "bearer-key")
    )
    @PostMapping("/request")
    public ResponseEntity<BaseResponse> requestTransfer(@RequestBody @Valid TransferRequest request) {
        transferService.requestTransfer(request.getQuoteId());
        return ResponseEntity.ok(
                BaseResponse.of(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase())
        );
    }

    @Operation(
            summary = "회원의 거래 이력을 가지고 오는 API",
            security = @SecurityRequirement(name = "bearer-key")
    )
    @GetMapping("/list")
    public ResponseEntity<TransferListWrapperResponse> getTransferHistory() {

        TransferListResponse transferListResponse = transferHistoryService.getTransferHistory();
        return ResponseEntity.ok()
                .body(TransferListWrapperResponse.of(
                        HttpStatus.OK.value(),
                        HttpStatus.OK.getReasonPhrase(),
                        transferListResponse)
                );
    }
}
