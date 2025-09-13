package homeWork7.payments.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PaymentRequest(
        @NotNull Long userId,
        @NotNull Long productId,
        @NotNull @Positive BigDecimal amount,
        @NotBlank String externalId
) {
}