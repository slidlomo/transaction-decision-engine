package za.co.transaction.decision.transaction.application;

public class DuplicateTransactionException extends RuntimeException {
    public DuplicateTransactionException(String transactionId) {
        super("Transaction ID already exists with a different idempotency key: " + transactionId);
    }
}
