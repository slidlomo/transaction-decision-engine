package za.co.transaction.decision.transaction.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    Optional<TransactionEntity> findByIdempotencyKey(String idempotencyKey);
    Optional<TransactionEntity> findByTransactionId(String transactionId);
}
