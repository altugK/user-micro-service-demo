package com.kafein.followerservice.event;

import com.kafein.followerservice.dto.UserResponse;
import com.kafein.followerservice.service.FollowerService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka listener classıdır.
 *
 * @author altugKarakayalı
 */
@Component
public class UserConsumer {
    Logger logger = LoggerFactory.getLogger(UserConsumer.class);

    private final FollowerService followerService;

    public UserConsumer(FollowerService followerService) {
        this.followerService = followerService;
    }

    @KafkaListener(topics = "user", groupId = "user-group")
    public void consumer(final ConsumerRecord<String, UserResponse> consumerRecord) {
        logger.info("1 handle with followers: {}", consumerRecord.key());
        followerService.createOrUpdate(consumerRecord.value());
    }

}
