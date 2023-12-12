package org.poc.longpolling.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageSender {

    Logger logger = LoggerFactory.getLogger(KafkaMessageSender.class);


    private KafkaTemplate<String, AccountStatusChangeEvent> kafkaTemplate;

    private String kafkaTopicName;

    KafkaMessageSender(
            @Autowired KafkaTemplate<String, AccountStatusChangeEvent> kafkaTemplate,
            @Value(value = "${app.kafka.topic}") String kafkaTopicName
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTopicName = kafkaTopicName;
    }

    public void publishKafkaMessage(AccountStatusChangeEvent event) {
        kafkaTemplate.send(kafkaTopicName, event);
        logger.info("Message sent to Kafka topic {}", kafkaTopicName);
    }
}
