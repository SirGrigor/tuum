package com.ilgrig.tuum.integration.config;

import com.ilgrig.tuum.util.MessagePublisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class RabbitMockConfig {
    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Bean
    public MessagePublisher messagePublisher() {
        return new MessagePublisher(rabbitTemplate);
    }
}