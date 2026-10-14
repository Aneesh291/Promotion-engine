package Promotions.config;

import Promotions.entity.PromotionEntity;
import Promotions.model.PromotionType;
import Promotions.repository.PromotionJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final PromotionJpaRepository promotionRepository;

    public DataSeeder(PromotionJpaRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public void run(String... args) {
        if (promotionRepository.count() > 0) {
            return;   // already seeded, don't add duplicates on every restart
        }

        LocalDate start = LocalDate.of(2026, 10, 1);
        LocalDate end = LocalDate.of(2026, 12, 31);

        promotionRepository.saveAll(List.of(
                new PromotionEntity("Buy 2 Get 1 Maggi", PromotionType.BUY_X_GET_Y,
                        "DMART-MUMBAI-12", "MAGGI-70G", 2, 0, 1, start, end),

                new PromotionEntity("Rs 20 off Coke", PromotionType.FLAT_OFF,
                        "DMART-MUMBAI-12", "COKE-1L", 1, 0, 20, start, end),

                new PromotionEntity("10% off above Rs 1000", PromotionType.PERCENT_OFF_CART,
                        "DMART-MUMBAI-12", null, 0, 1000, 10, start, end),

                new PromotionEntity("Expired Diwali offer", PromotionType.PERCENT_OFF_CART,
                        "DMART-MUMBAI-12", null, 0, 500, 50,
                        LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 31)),

                new PromotionEntity("Pune-only Maggi offer", PromotionType.FLAT_OFF,
                        "DMART-PUNE-04", "MAGGI-70G", 1, 0, 30, start, end)
        ));
    }
}