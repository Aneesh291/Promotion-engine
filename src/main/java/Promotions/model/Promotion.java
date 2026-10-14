package Promotions.model;

import java.time.LocalDate;

public record Promotion(
        Long id,
        String name,
        PromotionType type,
        String storeId,         // which store this offer runs in
        String productId,       // which product (null for whole-cart offers)
        int minQuantity,        // BUY_X_GET_Y: buy this many; FLAT_OFF: need at least this many
        int minCartAmount,      // PERCENT_OFF_CART: bill must be at least this much
        int rewardValue,        // free items, rupees off, or percent off (depends on type)
        LocalDate startDate,
        LocalDate endDate
) {
}