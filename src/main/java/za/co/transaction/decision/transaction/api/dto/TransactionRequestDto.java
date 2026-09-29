package za.co.transaction.decision.transaction.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import za.co.transaction.decision.transaction.domain.TransactionChannel;
import za.co.transaction.decision.transaction.domain.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionRequestDto(
        @NotBlank String transactionId,
        @NotBlank String customerId,
        @NotBlank String accountId,
        @NotNull @DecimalMin(value = "0.01") BigDecimal amount,
        @NotBlank @Size(min = 3, max = 3) String currency,
        @NotNull TransactionType transactionType,
        String merchantId,
        String merchantCategory,
        @NotNull TransactionChannel channel,
        @NotBlank @Size(min = 2, max = 2) String countryCode,
        @NotNull Instant transactionTimestamp
) {}
