package za.co.transaction.decision.decision;

import org.springframework.stereotype.Component;
import za.co.transaction.decision.transaction.api.dto.TransactionRequestDto;
import za.co.transaction.decision.transaction.domain.DecisionReason;
import za.co.transaction.decision.transaction.domain.DecisionStatus;

import java.math.BigDecimal;

@Component
public class DecisionEngine {

    private static final BigDecimal REVIEW_THRESHOLD = new BigDecimal("10000.00");
    private static final BigDecimal DECLINE_THRESHOLD = new BigDecimal("50000.00");

    public DecisionResult evaluate(TransactionRequestDto request) {
        if (request.amount().compareTo(DECLINE_THRESHOLD) > 0) {
            return new DecisionResult(
                    DecisionStatus.DECLINED,
                    DecisionReason.TRANSACTION_LIMIT_EXCEEDED
            );
        }

        if (request.amount().compareTo(REVIEW_THRESHOLD) > 0) {
            return new DecisionResult(
                    DecisionStatus.REVIEW,
                    DecisionReason.HIGH_VALUE_TRANSACTION
            );
        }

        return new DecisionResult(
                DecisionStatus.APPROVED,
                DecisionReason.ALL_CHECKS_PASSED
        );
    }

    public record DecisionResult(DecisionStatus status, DecisionReason reason) {}
}
