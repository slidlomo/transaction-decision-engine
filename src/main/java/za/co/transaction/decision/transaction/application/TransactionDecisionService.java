package za.co.transaction.decision.transaction.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.transaction.decision.decision.DecisionEngine;
import za.co.transaction.decision.transaction.api.dto.TransactionRequestDto;
import za.co.transaction.decision.transaction.api.dto.TransactionResponseDto;
import za.co.transaction.decision.transaction.persistence.TransactionEntity;
import za.co.transaction.decision.transaction.persistence.TransactionRepository;

import java.util.Optional;

@Service
public class TransactionDecisionService {

    private final TransactionRepository repository;
    private final DecisionEngine decisionEngine;

    public TransactionDecisionService(TransactionRepository repository, DecisionEngine decisionEngine) {
        this.repository = repository;
        this.decisionEngine = decisionEngine;
    }

    //Check if the idempotencyKeyis not null
    @Transactional
    public TransactionResponseDto decide(String idempotencyKey, TransactionRequestDto request) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new IllegalArgumentException("Idempotency-Key header is required");
        }


        //Find existing record by idempotencyKey
        Optional<TransactionEntity> existingTransaction =
                repository.findByIdempotencyKey(idempotencyKey);

        //If we find it we return that existing record as a response
        if (existingTransaction.isPresent()) {
            return toResponse(existingTransaction.get(), "Transaction already processed, Returning the existing record.");
        }

        //else we will create a new record with the unique identifiers
        return createDecision(idempotencyKey, request);

    }

    @Transactional(readOnly = true)
    public TransactionResponseDto getByTransactionId(String transactionId) {

        Optional<TransactionEntity> transaction =
                repository.findByTransactionId(transactionId);

        //Throw exception if transaction field is empty
        if (transaction.isEmpty()) {
            throw new TransactionNotFoundException(transactionId);
        }

        return toResponse(transaction.get(),"");
    }

    private TransactionResponseDto createDecision(String idempotencyKey, TransactionRequestDto request) {

        //In case we find a an existing TransactionId we return an exception
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

        return toResponse(saved,"");
    }

    private TransactionResponseDto toResponse(TransactionEntity entity, String message) {
        return new TransactionResponseDto(
                entity.getTransactionId(),
                entity.getDecision(),
                entity.getReason(),
                message

                //To add this record was already existing could not create a new record
        );
    }
}
