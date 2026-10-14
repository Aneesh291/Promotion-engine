package Promotions.entity;

import Promotions.model.Promotion;
import Promotions.model.PromotionType;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "promotion")
public class PromotionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PromotionType type;

    @Column(nullable = false)
    private String storeId;

    private String productId;      // null for whole-cart offers

    private int minQuantity;
    private int minCartAmount;
    private int rewardValue;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    // JPA needs an empty constructor to create objects from database rows
    protected PromotionEntity() {
    }

    public PromotionEntity(String name, PromotionType type, String storeId, String productId,
                           int minQuantity, int minCartAmount, int rewardValue,
                           LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.type = type;
        this.storeId = storeId;
        this.productId = productId;
        this.minQuantity = minQuantity;
        this.minCartAmount = minCartAmount;
        this.rewardValue = rewardValue;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Convert a database row into the simple Promotion object our calculator uses
    public Promotion toModel() {
        return new Promotion(id, name, type, storeId, productId,
                minQuantity, minCartAmount, rewardValue, startDate, endDate);
    }

    public Long getId() {
        return id;
    }
}