package za.co.transaction.decision.transaction.api.dto;

import za.co.transaction.decision.transaction.domain.DecisionReason;
import za.co.transaction.decision.transaction.domain.DecisionStatus;

public record TransactionResponseDto(
        String transactionId,
        DecisionStatus decision,
        DecisionReason reason
) {}
