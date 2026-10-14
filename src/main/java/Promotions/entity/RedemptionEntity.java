package Promotions.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "promotion_redemption")
public class RedemptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String eventId;            // unique -> the database itself blocks duplicates

    @Column(nullable = false)
    private String storeId;

    @Column(nullable = false)
    private Long promotionId;

    private String promotionName;

    private int discountAmount;

    @Column(nullable = false)
    private Instant redeemedAt;        // when the customer checked out

    @Column(nullable = false)
    private Instant processedAt;       // when the consumer saved it

    protected RedemptionEntity() {
    }

    public RedemptionEntity(String eventId, String storeId, Long promotionId, String promotionName,
                            int discountAmount, Instant redeemedAt, Instant processedAt) {
        this.eventId = eventId;
        this.storeId = storeId;
        this.promotionId = promotionId;
        this.promotionName = promotionName;
        this.discountAmount = discountAmount;
        this.redeemedAt = redeemedAt;
        this.processedAt = processedAt;
    }
}