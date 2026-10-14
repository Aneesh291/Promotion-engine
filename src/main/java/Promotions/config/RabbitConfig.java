package Promotions.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "promotion.exchange";
    public static final String QUEUE = "redemption.queue";
    public static final String ROUTING_KEY = "promotion.redeemed";

    public static final String DEAD_LETTER_EXCHANGE = "promotion.dlx";
    public static final String DEAD_LETTER_QUEUE = "redemption.dlq";
    public static final String DEAD_LETTER_ROUTING_KEY = "redemption.failed";

    // ---------- main flow ----------

    @Bean
    public TopicExchange promotionExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue redemptionQueue() {
        return QueueBuilder.durable(QUEUE)
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding redemptionBinding() {
        return BindingBuilder.bind(redemptionQueue())
                .to(promotionExchange())
                .with(ROUTING_KEY);
    }

    // ---------- dead letter flow ----------

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(DEAD_LETTER_ROUTING_KEY);
    }
}