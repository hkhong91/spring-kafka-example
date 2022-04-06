package com.example.kafka.listener;

import com.example.kafka.constant.KafkaHandlerName;
import com.example.kafka.constant.KafkaTopic;
import com.example.kafka.message.OriginalMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;

@Configuration
@Slf4j
public class OriginalListener {

  @KafkaListener(topics = {KafkaTopic.ORIGINAL}, errorHandler = KafkaHandlerName.DEFAULT)
  @SendTo(KafkaTopic.ORIGINAL_RETRY)
  public void consumeMessage(OriginalMessage message,
                                     @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                     @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                     @Header(KafkaHeaders.OFFSET) long offset) {
    log.info("consume >> topic: {}, partition: {}, offset: {}, message: {}", topic, partition, offset, message.toString());
  }

  @KafkaListener(topics = {KafkaTopic.ORIGINAL_RETRY}, errorHandler = KafkaHandlerName.DEFAULT)
  @SendTo(KafkaTopic.ORIGINAL_DEAD)
  public void consumeRetryMessage(OriginalMessage message,
                                  @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                  @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                  @Header(KafkaHeaders.OFFSET) long offset) {
    log.info("consume >> topic: {}, partition: {}, offset: {}, message: {}", topic, partition, offset, message.toString());
  }
}
