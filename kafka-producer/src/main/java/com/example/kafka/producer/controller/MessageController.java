package com.example.kafka.producer.controller;

import com.example.kafka.module.constant.KafkaTopic;
import com.example.kafka.module.message.OriginalMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

  private final KafkaTemplate<String, OriginalMessage> kafkaTemplate;

  @PostMapping
  public void sendMessage(@RequestParam long number) {
    ProducerRecord<String, OriginalMessage> record = new ProducerRecord<>(KafkaTopic.ORIGINAL, new OriginalMessage(number));
    kafkaTemplate.send(record)
        .addCallback(new ListenableFutureCallback<>() {
          @Override
          public void onFailure(Throwable ex) {
            log.error(ex.getMessage(), ex);
          }

          @Override
          public void onSuccess(SendResult<String, OriginalMessage> result) {
            RecordMetadata data = result.getRecordMetadata();
            OriginalMessage value = result.getProducerRecord().value();
            log.info("produce >> topic: {}, partition: {}, offset: {}, message: {}", data.topic(), data.partition(), data.offset(), value.toString());
          }
        });
  }
}
