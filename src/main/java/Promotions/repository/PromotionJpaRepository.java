package Promotions.repository;

import Promotions.entity.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PromotionJpaRepository extends JpaRepository<PromotionEntity, Long> {

    @Query("""
            SELECT p FROM PromotionEntity p
            WHERE p.storeId = :storeId
              AND :today BETWEEN p.startDate AND p.endDate
            """)
    List<PromotionEntity> findActiveByStore(@Param("storeId") String storeId,
                                            @Param("today") LocalDate today);
}