package com.example.kafka.batch;

import com.example.kafka.module.KafkaModuleConfiguration;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableBatchProcessing
@Import({KafkaModuleConfiguration.class})
public class KafkaBatchApplication {

  public static void main(String[] args) {
    SpringApplication.run(KafkaBatchApplication.class, args);
  }

}
