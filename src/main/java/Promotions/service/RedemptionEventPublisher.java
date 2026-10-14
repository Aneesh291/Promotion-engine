package Promotions.service;

import Promotions.config.RabbitConfig;
import Promotions.dto.RedemptionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
public class RedemptionEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(RedemptionEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final JsonMapper jsonMapper;

    public RedemptionEventPublisher(RabbitTemplate rabbitTemplate, JsonMapper jsonMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.jsonMapper = jsonMapper;
    }

    public void publish(RedemptionEvent event) {
        String json = jsonMapper.writeValueAsString(event);   // Java object -> JSON text
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, json);
        log.info("Published redemption event eventId={} promotionId={} store={}",
                event.eventId(), event.promotionId(), event.storeId());
    }
}