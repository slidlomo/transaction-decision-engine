package za.co.transaction.decision.transaction.api.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.transaction.decision.transaction.api.dto.TransactionRequestDto;
import za.co.transaction.decision.transaction.api.dto.TransactionResponseDto;
import za.co.transaction.decision.transaction.application.TransactionDecisionService;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionDecisionService service;

    public TransactionController(TransactionDecisionService service) {
        this.service = service;
    }

    @PostMapping("/decisions")
    public ResponseEntity<TransactionResponseDto> decide(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody TransactionRequestDto request) {
        return ResponseEntity.ok(service.decide(idempotencyKey, request));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> get(@PathVariable String transactionId) {
        return ResponseEntity.ok(service.getByTransactionId(transactionId));
    }
}
