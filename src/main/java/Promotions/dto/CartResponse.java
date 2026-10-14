package Promotions.dto;

public record CartResponse(
        int totalBeforeDiscount,
        int totalDiscount,
        int finalAmount,
        Long appliedPromotionId,
        String appliedPromotionName
) {
}