package za.co.transaction.decision.transaction.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import za.co.transaction.decision.transaction.domain.DecisionReason;
import za.co.transaction.decision.transaction.domain.DecisionStatus;

public record TransactionResponseDto(
        String transactionId,
        DecisionStatus decision,
        DecisionReason reason,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String  message

) {}
