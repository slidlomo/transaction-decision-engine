package za.co.transaction.decision.transaction.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.transaction.decision.decision.DecisionEngine;
import za.co.transaction.decision.transaction.api.dto.TransactionRequestDto;
import za.co.transaction.decision.transaction.api.dto.TransactionResponseDto;
import za.co.transaction.decision.transaction.persistence.TransactionEntity;
import za.co.transaction.decision.transaction.persistence.TransactionRepository;

@Service
public class TransactionDecisionService {

    private final TransactionRepository repository;
    private final DecisionEngine decisionEngine;

    public TransactionDecisionService(TransactionRepository repository, DecisionEngine decisionEngine) {
        this.repository = repository;
        this.decisionEngine = decisionEngine;
    }

    @Transactional
    public TransactionResponseDto decide(String idempotencyKey, TransactionRequestDto request) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new IllegalArgumentException("Idempotency-Key header is required");
        }

        return repository.findByIdempotencyKey(idempotencyKey)
                .map(this::toResponse)
                .orElseGet(() -> createDecision(idempotencyKey, request));
    }

    @Transactional(readOnly = true)
    public TransactionResponseDto getByTransactionId(String transactionId) {
        return repository.findByTransactionId(transactionId)
                .map(this::toResponse)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
    }

    private TransactionResponseDto createDecision(String idempotencyKey, TransactionRequestDto request) {
        if (repository.findByTransactionId(request.transactionId()).isPresent()) {
            throw new DuplicateTransactionException(request.transactionId());
        }

        DecisionEngine.DecisionResult result = decisionEngine.evaluate(request);

        TransactionEntity saved = repository.save(new TransactionEntity(
                request.transactionId(),
                idempotencyKey,
                request.amount(),
                request.currency(),
                result.status(),
                result.reason()
        ));

        return toResponse(saved);
    }

    private TransactionResponseDto toResponse(TransactionEntity entity) {
        return new TransactionResponseDto(
                entity.getTransactionId(),
                entity.getDecision(),
                entity.getReason()
        );
    }
}
