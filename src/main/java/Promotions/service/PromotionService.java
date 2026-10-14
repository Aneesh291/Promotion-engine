package Promotions.service;

import Promotions.dto.CartRequest;
import Promotions.dto.CartResponse;
import Promotions.entity.PromotionEntity;
import Promotions.model.Promotion;
import Promotions.repository.PromotionJpaRepository;
import org.springframework.stereotype.Service;
import Promotions.dto.RedemptionEvent;
import java.time.Instant;
import java.util.UUID;
import java.time.LocalDate;
import java.util.List;

@Service
public class PromotionService {

    private final PromotionJpaRepository promotionRepository;
    private final DiscountCalculator discountCalculator;
    private final RedemptionEventPublisher redemptionEventPublisher;

    public PromotionService(PromotionJpaRepository promotionRepository,
                            DiscountCalculator discountCalculator,
                            RedemptionEventPublisher redemptionEventPublisher) {
        this.promotionRepository = promotionRepository;
        this.discountCalculator = discountCalculator;
        this.redemptionEventPublisher = redemptionEventPublisher;
    }

    public CartResponse apply(CartRequest cart) {
        // 1. total before any discount
        int total = discountCalculator.cartTotal(cart);

        // 2. active offers for this store, read from PostgreSQL
        List<Promotion> activePromotions = promotionRepository
                .findActiveByStore(cart.storeId(), LocalDate.now())
                .stream()
                .map(PromotionEntity::toModel)
                .toList();

        // 3. calculate each offer's discount and remember the biggest
        Promotion bestPromotion = null;
        int bestDiscount = 0;

        for (Promotion promo : activePromotions) {
            int discount = discountCalculator.calculate(promo, cart);
            if (discount > bestDiscount) {
                bestDiscount = discount;
                bestPromotion = promo;
            }
        }

        // 4. build the answer
        if (bestPromotion == null) {
            return new CartResponse(total, 0, total, null, null);
        }
        // 5. announce the redemption (background work happens elsewhere)
        redemptionEventPublisher.publish(new RedemptionEvent(
                UUID.randomUUID().toString(),
                cart.storeId(),
                bestPromotion.id(),
                bestPromotion.name(),
                bestDiscount,
                Instant.now()
        ));

        return new CartResponse(total, bestDiscount, total - bestDiscount,
                bestPromotion.id(), bestPromotion.name());
    }
}