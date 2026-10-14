package Promotions.service;

import Promotions.config.RabbitConfig;
import Promotions.dto.RedemptionEvent;
import Promotions.entity.RedemptionEntity;
import Promotions.repository.RedemptionJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;

@Component
public class RedemptionListener {

    private static final Logger log = LoggerFactory.getLogger(RedemptionListener.class);

    private final RedemptionJpaRepository redemptionRepository;
    private final JsonMapper jsonMapper;

    public RedemptionListener(RedemptionJpaRepository redemptionRepository, JsonMapper jsonMapper) {
        this.redemptionRepository = redemptionRepository;
        this.jsonMapper = jsonMapper;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE)
    @Transactional
    public void handle(String message) {
        // 1. JSON text -> Java object
        RedemptionEvent event = jsonMapper.readValue(message, RedemptionEvent.class);

        // 2. idempotency: already processed? skip it
        if (redemptionRepository.existsByEventId(event.eventId())) {
            log.warn("Duplicate redemption event, skipping eventId={}", event.eventId());
            return;
        }

        // 3. save the redemption
        redemptionRepository.save(new RedemptionEntity(
                event.eventId(),
                event.storeId(),
                event.promotionId(),
                event.promotionName(),
                event.discountAmount(),
                event.redeemedAt(),
                Instant.now()
        ));

        log.info("Saved redemption eventId={} promotionId={} discount={}",
                event.eventId(), event.promotionId(), event.discountAmount());

        // method returns normally -> Spring sends the ACK -> RabbitMQ deletes the message
        // method throws an exception -> no ACK -> retry -> eventually the dead letter queue
    }
}