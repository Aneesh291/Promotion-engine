package Promotions.dto;

import java.time.Instant;

public record RedemptionEvent(
        String eventId,        // unique ID for this event (used later for idempotency)
        String storeId,
        Long promotionId,
        String promotionName,
        int discountAmount,
        Instant redeemedAt
) {
}