package com.example.kafka.application.controller;

import com.example.kafka.application.constant.KafkaTopic;
import com.example.kafka.application.message.OriginalMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

  private final KafkaTemplate<String, OriginalMessage> kafkaTemplate;
  private final Job originalDlqJob;
  private final JobLauncher jobLauncher;

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

  @PostMapping("/dlq-processing")
  public void processDlq() throws Exception {
    jobLauncher.run(originalDlqJob, new JobParametersBuilder()
        .addDate("date", new Date())
        .toJobParameters());
  }
}
