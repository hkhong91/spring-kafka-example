package com.example.kafka.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ListenerErrorHandler {

  @Bean
  public KafkaListenerErrorHandler kafkaListenerErrorHandler() {
    return (message, exception) -> {
      log.error(exception.getMessage(), exception);
      return message;
    };
  }
}
