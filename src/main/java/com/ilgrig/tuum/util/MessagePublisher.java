package com.ilgrig.tuum.util;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class MessagePublisher {

    private final RabbitTemplate rabbitTemplate;
    private static final String QUEUE_NAME = "transactionQueue";

    @Autowired
    public MessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishEvent(String eventType, Map<String, Object> details) {
        Map<String, Object> eventDetails = new HashMap<>(details);
        eventDetails.put("eventType", eventType);
        rabbitTemplate.convertAndSend(QUEUE_NAME, eventDetails);
    }
}
