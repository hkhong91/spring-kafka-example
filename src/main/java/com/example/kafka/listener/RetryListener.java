package com.example.kafka.listener;

import com.example.kafka.constant.KafkaTopic;
import com.example.kafka.message.OriginalMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.SendTo;

@Configuration
@Slf4j
public class RetryListener implements AcknowledgingMessageListener<String, OriginalMessage> {

  @Override
  @KafkaListener(topics = {KafkaTopic.RETRY}, errorHandler = "kafkaListenerErrorHandler")
  @SendTo(KafkaTopic.DEAD)
  public void onMessage(ConsumerRecord<String, OriginalMessage> demo, Acknowledgment acknowledgment) {
    OriginalMessage message = demo.value();
    log.info("consume >> topic: {}, partition: {}, offset: {}, message: {}", demo.topic(), demo.partition(), demo.offset(), message.toString());
    acknowledgment.acknowledge();
  }
}
