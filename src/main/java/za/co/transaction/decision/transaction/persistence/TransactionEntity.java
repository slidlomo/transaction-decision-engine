package za.co.transaction.decision.transaction.persistence;

import jakarta.persistence.*;
import za.co.transaction.decision.transaction.domain.DecisionReason;
import za.co.transaction.decision.transaction.domain.DecisionStatus;

import java.math.BigDecimal;

@Entity
@Table(name = "transactions",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_transaction_id", columnNames = "transaction_id"),
           @UniqueConstraint(name = "uk_idempotency_key", columnNames = "idempotency_key")
       })
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "idempotency_key", nullable = false)
    private String idempotencyKey;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DecisionStatus decision;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DecisionReason reason;

    protected TransactionEntity() {}

    public TransactionEntity(String transactionId, String idempotencyKey, BigDecimal amount,
                             String currency, DecisionStatus decision, DecisionReason reason) {
        this.transactionId = transactionId;
        this.idempotencyKey = idempotencyKey;
        this.amount = amount;
        this.currency = currency;
        this.decision = decision;
        this.reason = reason;
    }

    public String getTransactionId() { return transactionId; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public DecisionStatus getDecision() { return decision; }
    public DecisionReason getReason() { return reason; }
}
