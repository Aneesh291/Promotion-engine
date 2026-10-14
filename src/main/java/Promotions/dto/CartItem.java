package Promotions.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record CartItem(
        @NotBlank String productId,
        @Positive int quantity,
        @PositiveOrZero int price
) {
}