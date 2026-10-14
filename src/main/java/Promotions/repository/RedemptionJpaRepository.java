package Promotions.repository;

import Promotions.entity.RedemptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RedemptionJpaRepository extends JpaRepository<RedemptionEntity, Long> {

    boolean existsByEventId(String eventId);
}