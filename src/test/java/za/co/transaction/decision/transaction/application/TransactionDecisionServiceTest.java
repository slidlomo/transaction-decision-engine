package za.co.transaction.decision.transaction.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.co.transaction.decision.transaction.api.dto.TransactionRequestDto;
import za.co.transaction.decision.transaction.api.dto.TransactionResponseDto;
import za.co.transaction.decision.transaction.domain.DecisionReason;
import za.co.transaction.decision.transaction.domain.DecisionStatus;
import za.co.transaction.decision.transaction.domain.TransactionChannel;
import za.co.transaction.decision.transaction.domain.TransactionType;
import za.co.transaction.decision.transaction.persistence.TransactionRepository;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TransactionDecisionServiceTest {

    @Autowired
    private TransactionDecisionService service;

    @Autowired
    private TransactionRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void shouldApproveNormalTransactionAndPreventDuplicateProcessing() {
        TransactionRequestDto request = request("TX-001", "1500.00");

        TransactionResponseDto first = service.decide("IDEMP-001", request);
        TransactionResponseDto retry = service.decide("IDEMP-001", request);

        assertThat(first.decision()).isEqualTo(DecisionStatus.APPROVED);
        assertThat(first.reason()).isEqualTo(DecisionReason.ALL_CHECKS_PASSED);
        assertThat(retry).isEqualTo(first);
        assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    void shouldSendHighValueTransactionForReview() {
        TransactionResponseDto response =
                service.decide("IDEMP-002", request("TX-002", "15000.00"));

        assertThat(response.decision()).isEqualTo(DecisionStatus.REVIEW);
        assertThat(response.reason()).isEqualTo(DecisionReason.HIGH_VALUE_TRANSACTION);
    }

    @Test
    void shouldDeclineTransactionAboveLimit() {
        TransactionResponseDto response =
                service.decide("IDEMP-003", request("TX-003", "60000.00"));

        assertThat(response.decision()).isEqualTo(DecisionStatus.DECLINED);
        assertThat(response.reason()).isEqualTo(DecisionReason.TRANSACTION_LIMIT_EXCEEDED);
    }

    private TransactionRequestDto request(String transactionId, String amount) {
        return new TransactionRequestDto(
                transactionId,
                "CUS-001",
                "ACC-001",
                new BigDecimal(amount),
                "ZAR",
                TransactionType.ONLINE_PURCHASE,
                "MER-001",
                "ELECTRONICS",
                TransactionChannel.WEB,
                "ZA",
                Instant.parse("2026-09-29T10:00:00Z")
        );
    }
}
