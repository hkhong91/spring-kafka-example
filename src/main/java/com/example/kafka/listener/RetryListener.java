package com.example.kafka.listener;

import com.example.kafka.constant.KafkaTopic;
import com.example.kafka.message.DemoMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.SendTo;

@Configuration
@Slf4j
public class DemoRetryListener implements AcknowledgingMessageListener<String, DemoMessage> {

  @Override
  @KafkaListener(topics = {KafkaTopic.ORIGINAL_RETRY}, errorHandler = "kafkaListenerErrorHandler")
  @SendTo(KafkaTopic.ORIGINAL_DEAD)
  public void onMessage(ConsumerRecord<String, DemoMessage> demo, Acknowledgment acknowledgment) {
    DemoMessage message = demo.value();
    log.info("consume >> topic: {}, partition: {}, offset: {}, message: {}", demo.topic(), demo.partition(), demo.offset(), message.toString());
    acknowledgment.acknowledge();
  }
}
