package com.example.kafka.application.listener;

import com.example.kafka.application.constant.KafkaHandlerName;
import com.example.kafka.application.constant.KafkaTopic;
import com.example.kafka.application.message.OriginalMessage;
import com.example.kafka.domain.repository.OriginalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class OriginalListener {

  private final OriginalRepository originalRepository;

  @KafkaListener(topics = {KafkaTopic.ORIGINAL}, errorHandler = KafkaHandlerName.DEFAULT)
  @SendTo(KafkaTopic.ORIGINAL_RETRY)
  public void onMessage(OriginalMessage message,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                        @Header(KafkaHeaders.OFFSET) long offset) {
    log.info("consume >> topic: {}, partition: {}, offset: {}, message: {}", topic, partition, offset, message.toString());
    originalRepository.save(message.toEntity());
  }

  @KafkaListener(topics = {KafkaTopic.ORIGINAL_RETRY}, errorHandler = KafkaHandlerName.DEFAULT)
  @SendTo(KafkaTopic.ORIGINAL_DEAD)
  public void onMessageRetry(OriginalMessage message,
                             @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                             @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                             @Header(KafkaHeaders.OFFSET) long offset) {
    log.info("consume >> topic: {}, partition: {}, offset: {}, message: {}", topic, partition, offset, message.toString());
    originalRepository.save(message.toEntity());
  }
}
